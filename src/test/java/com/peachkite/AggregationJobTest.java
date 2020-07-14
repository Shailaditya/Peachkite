package com.peachkite;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import com.peachkite.db.constants.MongoFields;
import com.peachkite.db.domain.Benefit;
import com.peachkite.db.domain.Company;
import com.peachkite.db.repository.CompanyRepository;
import com.peachkite.service.CompanyBatchService;
import com.peachkite.service.impl.CompanyBatchServiceImpl;
import lombok.Data;
import org.bson.BSON;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class AggregationJobTest {

    @Autowired
    MongoClient mongo;

    @Autowired
    MongoTemplate template;

    @Autowired
    CompanyBatchService batchService;

    @Autowired
    private CompanyRepository repository;

    private static final String AVG_BENEFITS_RATING_QUERY="db.Feedback.aggregate([\n" +
            "    {\n" +
            "        $unwind: '$bfts'\n" +
            "    },\n" +
            "    {\n" +
            "        $match: {\n" +
            "            'bfts.rt': {\n" +
            "                $ne: 0.0\n" +
            "            }\n" +
            "        }\n" +
            "    },\n" +
            "    {\n" +
            "        $project: {\n" +
            "            'bfts.rt': 1,\n" +
            "            'bfts._id': 1,\n" +
            "            'cmpid': 1\n" +
            "        }\n" +
            "    },\n" +
            "    {\n" +
            "        $group: {\n" +
            "            '_id': {\n" +
            "                cmpid: '$cmpid',\n" +
            "                'bftid': '$bfts._id'\n" +
            "            },\n" +
            "            averageQuantity: {\n" +
            "                $avg: '$bfts.rt'\n" +
            "            },\n" +
            "            sum: {\n" +
            "                $sum: 1\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "])";

//    @Test
    public void testQuery(){
        String companyId="5a5b04dad5fb1d22b5f85c12";
        List<AggregationOperation> aggs= Arrays.asList(Aggregation.unwind(MongoFields.BENEFITS),
                Aggregation.match(Criteria.where(MongoFields.BENEFITS+"."+MongoFields.RATING).ne(0.0D).andOperator(Criteria.where(MongoFields.COMPANY_ID).is(companyId))),
                Aggregation.project(MongoFields.COMPANY_ID).and(MongoFields.BENEFITS+"."+MongoFields.ID).as("bftsId").and(MongoFields.BENEFITS+"."+MongoFields.RATING).as("bftsrt"),
                Aggregation.group(Fields.from(Fields.field("companyId","cmpid"),Fields.field("benefitId","bftsId"))).avg("bftsrt").as("rating"));
        Aggregation agg=Aggregation.newAggregation(aggs);
        List<AverageBenefitRatingVO> ratingVOs=template.aggregate(agg, "Feedback", AverageBenefitRatingVO.class).getMappedResults();
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

        Company company=repository.findOne(companyId);
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
        repository.save(company);
    }

    @Data
    private class AverageBenefitRatingVO{
        private String companyId;
        private String benefitId;
        private Double rating;
    }
}
