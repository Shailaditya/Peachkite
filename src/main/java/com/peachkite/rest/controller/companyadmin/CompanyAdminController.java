package com.peachkite.rest.controller.companyadmin;


import com.peachkite.db.constants.MongoFields;
import com.peachkite.db.domain.Feedback;
import com.peachkite.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.peachkite.db.domain.Company;
import com.peachkite.service.CompanyService;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/company")
public class CompanyAdminController {

    @Autowired
    CompanyService companyService;

    @RequestMapping("/{id}")
    ResponseEntity<Company> findCompany(@PathVariable String id){
        Company company=this.companyService.findCompanyById(id);
        if(company == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(company,HttpStatus.OK);
    }

    @RequestMapping(value="/{id}",method= RequestMethod.PUT)
    ResponseEntity<Company> update(@PathVariable String id,
                                   @RequestBody Company company){
        if(!id.equals(company.getId()))
            return new ResponseEntity<Company>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(this.companyService.save(company),HttpStatus.OK);
    }

    @RequestMapping(value="/{companyId}/benefit",method= RequestMethod.POST)
    ResponseEntity<?> saveBenefits(@RequestBody List<Company.CompanyBenefit> benefits, @PathVariable String companyId) throws MessagingException {
        return new ResponseEntity<>(companyService.saveBenefits(benefits,companyId),HttpStatus.OK);
    }

}
