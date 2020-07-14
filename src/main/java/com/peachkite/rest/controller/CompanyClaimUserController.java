package com.peachkite.rest.controller;

import com.peachkite.service.ClaimCompanyService;
import com.peachkite.vo.ClaimCompanyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/api/public/claim_company")
@RestController
public class CompanyClaimUserController {

    @Autowired
    private ClaimCompanyService claimCompanyService;

    @RequestMapping(value="",method= RequestMethod.POST)
    ResponseEntity<?> generateCompanyClaim(@RequestBody ClaimCompanyVO vo, HttpServletRequest request) throws Exception {
        String hostname=request.getRequestURL().substring(0, request.getRequestURL().indexOf(request.getRequestURI()));
        vo.setHostname(hostname);
        claimCompanyService.processClaimRequest(vo);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
