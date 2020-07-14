package com.peachkite.rest.controller;

import com.peachkite.db.constants.MongoFields;
import com.peachkite.db.domain.Company;
import com.peachkite.db.domain.Feedback;
import com.peachkite.db.domain.MissingCompany;
import com.peachkite.service.CompanyService;
import com.peachkite.service.FeedbackService;
import com.peachkite.vo.SearchCompanyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/public/company")
public class CompanyPublicController {

    @Autowired
    CompanyService companyService;

    @Autowired
    FeedbackService feedbackService;

    @RequestMapping("/search")
    ResponseEntity<List<SearchCompanyVO>> searchCompany(@RequestParam(value="key") String searchString) throws UnknownHostException {
        return new ResponseEntity<>(companyService.searchCompanyName(searchString), HttpStatus.OK);
    }

    @RequestMapping("/{id}")
    ResponseEntity<Company> findCompany(@PathVariable String id){
        Company company=this.companyService.findCompanyById(id);
        if(company == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(company,HttpStatus.OK);
    }

    @RequestMapping("/{companyId}/review")
    ResponseEntity<?> getReviews(@PathVariable String companyId){
        Pageable pageable=new PageRequest(0,4,new Sort(Sort.Direction.DESC, MongoFields.CREATED_AT));
        Page<Feedback> feedbacks=feedbackService.getFeedbackByCompanyId(companyId,false,pageable);
        List<com.peachkite.vo.ReviewVO> reviews=new ArrayList<>();
        for(Feedback feedback:feedbacks){
            com.peachkite.vo.ReviewVO review=new com.peachkite.vo.ReviewVO();
            review.setMartialStatus(feedback.getUserMartialStatus());
            review.setDate(feedback.getCreatedAt());
            review.setComment(feedback.getComment());
            reviews.add(review);
        }
        return new ResponseEntity<>(new PageImpl<>(reviews,pageable,feedbacks.getTotalElements()),HttpStatus.OK);
    }

    @RequestMapping(value="/missing",method = RequestMethod.POST)
    ResponseEntity<?> saveMissingCompany(@RequestBody MissingCompany company) throws UnknownHostException {
        companyService.saveMissingCompany(company);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
