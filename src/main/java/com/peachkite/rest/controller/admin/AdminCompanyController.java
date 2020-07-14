package com.peachkite.rest.controller.admin;

import com.peachkite.db.domain.Benefit;
import com.peachkite.db.domain.Company;
import com.peachkite.db.repository.BenefitRepository;
import com.peachkite.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/super_admin/company")
public class AdminCompanyController {

    @Autowired
    CompanyService companyService;

    @Autowired
    BenefitRepository benefitRepository;

    @RequestMapping(value="",method= RequestMethod.POST)
    ResponseEntity<Company> findCompany(@RequestBody Company company){
        List<Benefit> benefits=benefitRepository.findAll();
        List<Company.CompanyBenefit> companyBenefits=new ArrayList<>();
        for(Benefit benefit:benefits){
            Company.CompanyBenefit companyBenefit=new Company.CompanyBenefit();
            companyBenefit.setId(benefit.getId());
            companyBenefit.setCategory(benefit.getCategory());
            companyBenefit.setIsSelected(true);
            companyBenefit.setType(Company.CompanyBenefit.TYPE_STANDARD);
            companyBenefit.setLabel(benefit.getLabel());
            companyBenefit.setRating(0.0D);
            companyBenefits.add(companyBenefit);
        }
        company.setBenefits(companyBenefits);
        company.setRating(0.0D);
        company.setFeedbackCount(0l);
        company.setCommentCount(0l);
        company.setIsVerified(false);
        company.setSearchCount(0l);
        company.setCategoryRatings(new HashMap<Integer,Double>(){{
            put(1,0.0D);
            put(2,0.0D);
            put(3,0.0D);
            put(4,0.0D);
        }});
        company=companyService.save(company);
        return new ResponseEntity<>(company,HttpStatus.OK);
    }
}
