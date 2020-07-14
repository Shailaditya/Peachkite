package com.peachkite.rest.controller.secure;

import com.fasterxml.jackson.core.type.TypeReference;
import com.peachkite.db.domain.Faq;
import com.peachkite.db.repository.FaqRepository;
import com.peachkite.util.JSONUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/secure/faq")
public class FaqController {

    @Autowired
    private FaqRepository faqRepository;

    /*@RequestMapping("")
    ResponseEntity<List<Faq>> getFaqs(@RequestParam String companyId,
                                      @RequestParam(required=false) Boolean isSelected){
        if(isSelected != null)
            return new ResponseEntity<>(faqRepository.findByCompanyIdAndIsSelectedOrderBySerialNumber(companyId,isSelected),
                    HttpStatus.OK);
        else
            return new ResponseEntity<>(faqRepository.findByCompanyId(companyId),
                    HttpStatus.OK);
    }*/

    @RequestMapping(value="",method= RequestMethod.POST)
    ResponseEntity<?> save(@RequestBody String body){
        if(body == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        boolean isList=body.startsWith("[")?true:false;
        try{
            if(isList)
                return new ResponseEntity<>(faqRepository.save(JSONUtils.convertJSONToObject(new TypeReference<List<Faq>>() {},body)),
                        HttpStatus.CREATED);
            else
                return new ResponseEntity<>(faqRepository.save(JSONUtils.convertJSONToObject(Faq.class,body)),
                        HttpStatus.CREATED);
        }catch(IOException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/{faqId}",method= RequestMethod.PUT)
    ResponseEntity<?> update(@RequestParam String faqId,@RequestBody Faq faq){
        if(faq.getId() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(faqRepository.save(faq),HttpStatus.OK);
    }
}
