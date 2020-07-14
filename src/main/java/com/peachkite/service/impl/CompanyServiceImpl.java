package com.peachkite.service.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import javax.annotation.PreDestroy;
import javax.mail.MessagingException;

import com.peachkite.db.constants.MongoFields;
import com.peachkite.db.domain.*;
import com.peachkite.db.repository.CompanyQueryRepository;
import com.peachkite.db.repository.NotificationRepository;
import com.peachkite.service.EmailService;
import com.peachkite.service.UserService;
import com.peachkite.vo.AverageBenefitRatingVO;
import com.peachkite.vo.QueryVO;
import com.peachkite.db.repository.MissingCompanyRepository;
import org.bson.types.ObjectId;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.peachkite.db.repository.CompanyRepository;
import com.peachkite.service.CompanyService;
import com.peachkite.util.JSONUtils;
import com.peachkite.vo.SearchCompanyVO;
import org.springframework.util.Assert;

@Service
public class CompanyServiceImpl implements CompanyService{
	
	@Autowired
	protected CompanyRepository companyRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	protected UserService userService;

	@Autowired
	protected NotificationRepository notificationRepository;

	@Autowired
	protected CompanyQueryRepository companyQueryRepository;

	@Autowired
	protected EmailService emailService;

	@Autowired
	protected MissingCompanyRepository missingCompanyRepository;

	@Value("${elastic.search.host}")
    private String host;

    @Value("${elastic.search.port}")
    private Integer port;
    
    @Value("${elastic.search.index.name}")
    private String indexName;

    @Value("${elastic.search.index.type}")
    private String indexType;
    
    @Value("${elastic.search.cluster.name}")
    private String clusterName;
    
    private TransportClient client=null;
    
    private static final Log logger = LogFactory.getLog(CompanyServiceImpl.class);
    
	@SuppressWarnings("resource")
	private TransportClient getClient() throws UnknownHostException {
		if(client == null) {
			Settings settings = Settings.builder()
			        .put("cluster.name", clusterName).build();
			client= new PreBuiltTransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
		}
		return client;
	}
	
	@Override
	public void addToIndex(SearchCompanyVO companyVO) {
		try{
			String json=JSONUtils.convertObjectToJSON(companyVO);
			getClient().prepareIndex(indexName,indexType,companyVO.getId())
					.setSource(json, XContentType.JSON)
					.get();
		}catch(UnknownHostException e){
			throw new RuntimeException(e);
		}

	}
	
	@Override
	public List<SearchCompanyVO> searchCompanyName(String searchString) throws UnknownHostException {
		SearchResponse scrollResp = getClient().prepareSearch()
				.setFetchSource(true)
				.setSearchType(SearchType.QUERY_THEN_FETCH)
		        .setQuery(QueryBuilders.queryStringQuery("*"+searchString.toLowerCase()+"*"))
		        .setSize(10).execute().actionGet();
		Iterator<SearchHit> iterator=scrollResp.getHits().iterator();
		List<SearchCompanyVO> searchResult=new ArrayList<SearchCompanyVO>();
		while(iterator.hasNext()) {
			SearchHit hit=iterator.next();
			Map<String,Object> responseFields=JSONUtils.convertJsonToMap(new String(BytesReference.toBytes(hit.getSourceRef())));
			if(responseFields.get("name") != null) {
				searchResult.add(new SearchCompanyVO(responseFields.get("name").toString(),hit.getId()));
			}
		}
		return searchResult;
	}
	
	@Override
	public Company findCompanyById(String id) {
		return this.companyRepository.findOne(id);
	}

	@Override
	public Company save(Company company) {
		company=this.companyRepository.save(company);
		addToIndex(new SearchCompanyVO(company.getName(),company.getId()));
		return this.companyRepository.save(company);
	}

	@Override
	public void incrementFeedbackCount(String companyId,int count) {
		mongoTemplate.updateFirst(new Query(Criteria.where("_id").is(new ObjectId(companyId))),
				new Update().inc(MongoFields.FEEDBACK_COUNT, count),
				Company.class);
	}

	@Override
	public void createQuery(String companyId,String query)throws MessagingException{
		Company company=companyRepository.findOne(companyId);
		if(company.getIsVerified() != null){
			try{
				User user=userService.get(company.getAdminUserId());
				QueryVO queryVO=new QueryVO();
				queryVO.setEmail(userService.getLoggedInUser().getEmail());
				queryVO.setQuery(query);
				emailService.sendCompanyQueryMail(queryVO,user.getEmail());
			}catch(Exception e){
				logger.error("Exception occurred",e);
			}
			CompanyQuery companyQuery=createCompanyQuery(companyId,query);
			createQueryNotification(companyId,query,companyQuery.getId());
		}
	}

	@Override
	public List<Company.CompanyBenefit> saveBenefits(List<Company.CompanyBenefit> benefits,String companyId) {
		Company company = companyRepository.findOne(companyId);
		for (Company.CompanyBenefit benefit : benefits) {
			if (Company.CompanyBenefit.TYPE_COMPANY_SPECIFIC.equals(benefit.getType())) {
				if (benefit.getId() == null)
					benefit.setId(new ObjectId().toString());
				benefit.setIsSelected(true);
			} else {
				Assert.notNull(benefit.getId(), "Standard benefit cannot have null id");
			}
		}
		company.setBenefits(benefits);
		company = companyRepository.save(company);
		return company.getBenefits();
	}

	@Override
	public MissingCompany saveMissingCompany(MissingCompany company){
		return missingCompanyRepository.save(company);
	}

	@Override
	@Async
	public void updateRating(String companyId){
		List<AggregationOperation> aggs= Arrays.asList(Aggregation.unwind(MongoFields.BENEFITS),
				Aggregation.match(Criteria.where(MongoFields.BENEFITS+"."+MongoFields.RATING).ne(0.0D).andOperator(Criteria.where(MongoFields.COMPANY_ID).is(companyId))),
				Aggregation.project(MongoFields.COMPANY_ID).and(MongoFields.BENEFITS+"."+MongoFields.ID).as("bftsId").and(MongoFields.BENEFITS+"."+MongoFields.RATING).as("bftsrt"),
				Aggregation.group(Fields.from(Fields.field("companyId","cmpid"),Fields.field("benefitId","bftsId"))).avg("bftsrt").as("rating"));
		Aggregation agg=Aggregation.newAggregation(aggs);
		List<AverageBenefitRatingVO> ratingVOs=mongoTemplate.aggregate(agg, "Feedback", AverageBenefitRatingVO.class).getMappedResults();
		Map<String,AverageBenefitRatingVO> companyRatings=new HashMap<>();
		for(AverageBenefitRatingVO ratingVO:ratingVOs)
			companyRatings.put(ratingVO.getBenefitId(),ratingVO);

		double sum=0.0d;
		int count=0;
		Map<Integer,Double> categoryRatingSumMap=new HashMap<>();
		categoryRatingSumMap.put(Benefit.CATEGORY_DIVERSITY,0.0D);
		categoryRatingSumMap.put(Benefit.CATEGORY_ENRICHMENT,0.0D);
		categoryRatingSumMap.put(Benefit.CATEGORY_FLEXIBILITY,0.0D);
		categoryRatingSumMap.put(Benefit.CATEGORY_WORK_LIFE_BALANCE,0.0D);

		Map<Integer,Long> categoryRatingCountMap=new HashMap<>();
		categoryRatingCountMap.put(Benefit.CATEGORY_DIVERSITY,0l);
		categoryRatingCountMap.put(Benefit.CATEGORY_ENRICHMENT,0l);
		categoryRatingCountMap.put(Benefit.CATEGORY_FLEXIBILITY,0l);
		categoryRatingCountMap.put(Benefit.CATEGORY_WORK_LIFE_BALANCE,0l);

		Company company=companyRepository.findOne(companyId);
		for(Company.CompanyBenefit benefit:company.getBenefits()){
			if(Company.CompanyBenefit.TYPE_STANDARD.equals(benefit.getType()) && companyRatings.containsKey(benefit.getId())){
				benefit.setRating(companyRatings.get(benefit.getId()).getRating());
				categoryRatingCountMap.put(benefit.getCategory(),categoryRatingCountMap.get(benefit.getCategory())+1);
				categoryRatingSumMap.put(benefit.getCategory(),categoryRatingSumMap.get(benefit.getCategory())+benefit.getRating());
				count++;
				sum+=benefit.getRating();
			}
		}

		company.setRating(count == 0?0.0D:sum/count);
		Map<Integer,Double> catRatings=new HashMap<>();
		for(Map.Entry<Integer,Double> entry: categoryRatingSumMap.entrySet()){
			Long rtcount=categoryRatingCountMap.get(entry.getKey());
			double rating=0.0d;
			if(rtcount != 0)
				rating=categoryRatingSumMap.get(entry.getKey())/rtcount;
			catRatings.put(entry.getKey(),rating);
		}
		company.setCategoryRatings(catRatings);
		companyRepository.save(company);
	}

	public void viewCompany(String companyId){
		Query query=new Query();
		query.addCriteria(Criteria.where("_id").is(new ObjectId(companyId)));
		Update update=new Update();
		update.inc(MongoFields.SEARCH_COUNT,1);
		mongoTemplate.updateFirst(query,update,Company.class);
	}

	@PreDestroy
	private void destroy() {
		if(client != null)
			client.close();
	}

	private CompanyQuery createCompanyQuery(String companyId,String queryText){
		if(userService.getLoggedInUser() != null){
			CompanyQuery companyQuery=new CompanyQuery();
			companyQuery.setUserId(userService.getLoggedInUser().getId());
			companyQuery.setCreatedAt(new Date());
			companyQuery.setStatus(CompanyQuery.Status.PENDNG);
			companyQuery.setCompanyId(companyId);
			companyQuery.setQueryText(queryText);
			return companyQueryRepository.save(companyQuery);
		}else{
			throw new RuntimeException("No User Session found");
		}
	}

	private void createQueryNotification(String companyId,String queryText,String queryId){
		if(userService.getLoggedInUser() != null){
			Notification notification=new Notification();
			notification.setCompanyId(companyId);
			notification.setCreatedAt(new Date());
			notification.setDetail(queryText);
			notification.setFrom(userService.getLoggedInUser().getEmail());
			notification.setStatus(Notification.Status.Pending);
			notification.setType(Notification.Type.Query);
			notification.setQueryId(queryId);
			notificationRepository.save(notification);
		}else{
			throw new RuntimeException("No User Session found");
		}
	}

}
