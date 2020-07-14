package com.peachkite.rest.controller;

import com.peachkite.db.domain.Faq;
import com.peachkite.db.repository.FaqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/faq")
public class FaqPublicController {

    @Autowired
    private FaqRepository faqRepository;

    @RequestMapping("")
    ResponseEntity<List<Faq>> getFaqs(@RequestParam String companyId,
                                      @RequestParam(required = false) Faq.Status status) {
        if (status != null)
            return new ResponseEntity<>(faqRepository.findByCompanyIdAndStatusOrderBySerialNumber(companyId, status),
                    HttpStatus.OK);
        else
            return new ResponseEntity<>(faqRepository.findByCompanyIdOrderBySerialNumber(companyId),
                    HttpStatus.OK);
    }
}