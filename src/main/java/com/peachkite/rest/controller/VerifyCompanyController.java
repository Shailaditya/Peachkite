package com.peachkite.rest.controller;

import com.peachkite.service.ClaimCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/verify_company")
@RestController
public class VerifyCompanyController {

    @Autowired
    private ClaimCompanyService claimCompanyService;

    @RequestMapping("/{id}")
    public ResponseEntity<?> verifyCompany(@PathVariable String id) throws Exception{
        if(claimCompanyService.verifyClaimRequest(id)){
            return new ResponseEntity<>("Company Claim Successfull", HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>("Invalid request", HttpStatus.BAD_REQUEST);
        }
    }
}
