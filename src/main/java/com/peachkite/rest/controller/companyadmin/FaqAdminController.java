package com.peachkite.rest.controller.companyadmin;

import com.peachkite.db.domain.Faq;
import com.peachkite.db.repository.FaqRepository;
import com.peachkite.exception.FaqPublishLimitReachedException;
import com.peachkite.service.FaqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/faq")
public class FaqAdminController {

    @Autowired
    protected FaqRepository faqRepository;

    @Autowired
    protected FaqService faqService;

    @RequestMapping("")
    ResponseEntity<List<Faq>> getCompanyFaqs(@RequestParam String companyId){
        return  new ResponseEntity<List<Faq>>(faqRepository.findByCompanyIdOrderBySerialNumber(companyId), HttpStatus.OK);
    }

    @RequestMapping(value="",method = RequestMethod.POST)
    ResponseEntity<?> postFaq(@RequestBody Faq faq){
        if(faq.getId() != null)
            return new ResponseEntity<Faq>(HttpStatus.METHOD_NOT_ALLOWED);
        try {
            return  new ResponseEntity<Faq>(faqService.createFaq(faq), HttpStatus.CREATED);
        } catch (FaqPublishLimitReachedException e) {
            Map<String,Integer> response=new HashMap<String,Integer>();
            response.put("maxCount",faqService.getMaxPublishedFaqs());
            return new ResponseEntity<>(response,HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value="/{id}",method = RequestMethod.PUT)
    ResponseEntity<?> putFaq(@RequestBody Faq faq,@PathVariable String id){
        if(faq.getId() == null)
            return new ResponseEntity<Faq>(HttpStatus.METHOD_NOT_ALLOWED);
        if(!faq.getId().equals(id))
            return new ResponseEntity<Faq>(HttpStatus.BAD_REQUEST);
        try {
            return  new ResponseEntity<Faq>(faqService.updateFaq(faq), HttpStatus.OK);
        } catch (FaqPublishLimitReachedException e) {

            return new ResponseEntity<>(new HashMap<String,Integer>(){{put("maxCount",faqService.getMaxPublishedFaqs());}},HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    ResponseEntity<?> putFaq(@PathVariable String id){
        return  new ResponseEntity<Faq>(faqRepository.findOne(id), HttpStatus.OK);
    }

}
