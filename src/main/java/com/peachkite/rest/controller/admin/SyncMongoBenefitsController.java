package com.peachkite.rest.controller.admin;

import com.peachkite.db.domain.Benefit;
import com.peachkite.db.domain.Company;
import com.peachkite.db.repository.BenefitRepository;
import com.peachkite.db.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/super_admin/sync/benefits")
public class SyncMongoBenefitsController {

    @Autowired
    private BenefitRepository benefitRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @RequestMapping("")
    ResponseEntity<?> syncBenefits(){
        List<Benefit> benefitList=benefitRepository.findAll();

        List<Company> companyList=companyRepository.findAll();

        for(Company company:companyList){

            Map<String,Company.CompanyBenefit> companyBenefits = new HashMap<>();
            for(Company.CompanyBenefit companyBenefit:company.getBenefits())
                companyBenefits.put(companyBenefit.getId(),companyBenefit);

            for(Benefit benefit:benefitList){
                Company.CompanyBenefit cbf=companyBenefits.get(benefit.getId());
                if(cbf == null){
                    cbf=new Company.CompanyBenefit();
                    cbf.setId(benefit.getId());
                    cbf.setLabel(benefit.getLabel());
                    cbf.setIsSelected(true);
                    cbf.setRating(0.0D);
                    cbf.setCategory(benefit.getCategory());
                    cbf.setType(Company.CompanyBenefit.TYPE_STANDARD);
                    company.getBenefits().add(cbf);
                }else{
                    cbf.setLabel(benefit.getLabel());
                    cbf.setCategory(benefit.getCategory());
                    cbf.setType(Company.CompanyBenefit.TYPE_STANDARD);
                }
            }

        }
        companyRepository.save(companyList);
        return new ResponseEntity<String>("Success", HttpStatus.OK);
    }

}
