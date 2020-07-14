package com.peachkite.service.impl;

import com.peachkite.db.constants.MongoFields;
import com.peachkite.db.domain.Benefit;
import com.peachkite.db.domain.Company;
import com.peachkite.db.repository.CompanyRepository;
import com.peachkite.service.CompanyBatchService;
import com.peachkite.vo.AverageBenefitRatingVO;
import lombok.Data;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompanyBatchServiceImpl implements CompanyBatchService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyBatchServiceImpl.class);

    @Autowired
    private MongoTemplate template;

    @Autowired
    private CompanyRepository repository;

    @Override
//    @Scheduled(fixedRate=60*60*1000)
    public void updateStatistics() {

        logger.info("Company Statistics Job execution started....");

        Map<String,Long> feedbackCountHash=getFeedbackCountHash();

        List<Benefit> benefits=template.findAll(Benefit.class);
        Map<String,Benefit> benefitHash=new HashMap<>();

        //Map of standard benefits which are rated
        for(Benefit benefit:benefits){
            benefitHash.put(benefit.getId(),benefit);
        }

        List<AggregationOperation> aggs= Arrays.asList(Aggregation.unwind(MongoFields.BENEFITS),
                Aggregation.match(Criteria.where(MongoFields.BENEFITS+"."+MongoFields.RATING).ne(0.0D)),
                Aggregation.project(MongoFields.COMPANY_ID).and(MongoFields.BENEFITS+"."+MongoFields.ID).as("bftsId").and(MongoFields.BENEFITS+"."+MongoFields.RATING).as("bftsrt"),
                Aggregation.group(Fields.from(Fields.field("companyId","cmpid"),Fields.field("benefitId","bftsId"))).avg("bftsrt").as("rating"));
        Aggregation agg=Aggregation.newAggregation(aggs);
        List<AverageBenefitRatingVO> ratingVOs=template.aggregate(agg, "Feedback", AverageBenefitRatingVO.class).getMappedResults();

        List<ObjectId> companyIds=new ArrayList<>();
        //Key 1 companyId Key 2 benefitId
        Map<String,Map<String,AverageBenefitRatingVO>> ratingHash=new HashMap<>();

        for(AverageBenefitRatingVO ratingVO:ratingVOs){
            companyIds.add(new ObjectId(ratingVO.getCompanyId()));
            if(!ratingHash.containsKey(ratingVO.getCompanyId()))
                ratingHash.put(ratingVO.getCompanyId(),new HashMap<>());
            ratingHash.get(ratingVO.getCompanyId()).put(ratingVO.getBenefitId(),ratingVO);
        }
        List<Company> companies=template.find(new Query(Criteria.where(MongoFields.ID).in(companyIds)),Company.class);
        for(Company company:companies){
            Map<String,AverageBenefitRatingVO> companyRatings=ratingHash.get(company.getId());

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
            company.setFeedbackCount(feedbackCountHash.get(company.getId()));
            Map<Integer,Double> catRatings=new HashMap<>();
            for(Map.Entry<Integer,Double> entry: categoryRatingSumMap.entrySet()){
                Long rtcount=categoryRatingCountMap.get(entry.getKey());
                double rating=0.0d;
                if(rtcount != 0)
                    rating=categoryRatingSumMap.get(entry.getKey())/rtcount;
                catRatings.put(entry.getKey(),rating);
            }
            company.setCategoryRatings(catRatings);
        }
        repository.save(companies);
        logger.info("Company Statistics Job execution ended....");
    }

    private Map<String,Long> getFeedbackCountHash(){
        List<AggregationOperation> aggs= Arrays.asList(
                Aggregation.project(MongoFields.COMPANY_ID),
                Aggregation.group(Fields.from(Fields.field("companyId",MongoFields.COMPANY_ID))).count().as("count"));
        Aggregation agg=Aggregation.newAggregation(aggs);
        Map<String,Long> feedbackCountHash=new HashMap<>();
        List<FeedbackCountVO> feedbackVOs= template.aggregate(agg, "Feedback", FeedbackCountVO.class).getMappedResults();
        for(FeedbackCountVO feedbackCountVO:feedbackVOs)
            feedbackCountHash.put(feedbackCountVO.get_id(),feedbackCountVO.getCount());
        return feedbackCountHash;
    }

    @Data
    private class FeedbackCountVO{
        private String _id;
        private Long count;
    }
}

