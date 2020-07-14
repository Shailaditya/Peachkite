package com.peachkite;

import com.peachkite.db.domain.Benefit;
import com.peachkite.db.domain.Company;
import com.peachkite.db.repository.BenefitRepository;
import com.peachkite.db.repository.CompanyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddNewFieldsPhase2 {

    @Autowired
    protected CompanyRepository companyRepository;

    @Autowired
    protected BenefitRepository benefitRepository;

    @Test
    public void addNewFields(){

        Map<Integer,Double> categoryRatings=new HashMap<Integer,Double>(){{
            put(1,0.0d);
            put(2,0.0d);
            put(3,0.0d);
            put(4,0.0d);
        }};

        List<Company> companies=companyRepository.findAll();
        List<Benefit> benefits=benefitRepository.findAll();
        Map<String,Integer> benefitIdCategoryMap=new HashMap<>();
        for(Benefit benefit:benefits){
            benefitIdCategoryMap.put(benefit.getId(),benefit.getCategory());
        }

        for(Company company:companies){
            company.setCategoryRatings(categoryRatings);
            for(Company.CompanyBenefit companyBenefit:company.getBenefits()){
                if(!benefitIdCategoryMap.containsKey(companyBenefit.getId()))
                    throw new RuntimeException("Benefits id missing in benefitIdCategoryMap : "+companyBenefit.getId()+"------"+benefitIdCategoryMap);
                companyBenefit.setCategory(benefitIdCategoryMap.get(companyBenefit.getId()));
                companyBenefit.setRating(0.0);
                companyBenefit.setIsSelected(true);
                companyBenefit.setType(Company.CompanyBenefit.TYPE_STANDARD);
                companyBenefit.setIsSelected(true);
            }
        }

        companyRepository.save(companies);
    }

}
