package com.peachkite.rest.controller;

import com.peachkite.db.domain.MultimediaContent;
import com.peachkite.service.MultimediaContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/content")
public class MultimediaContentPublicController {

    @Autowired
    private MultimediaContentService multimediaService;

    @RequestMapping("")
    ResponseEntity<?> getContent(@RequestParam String companyId,
                                 @RequestParam Integer pageNumber,
                                 @RequestParam Integer pageSize){
        Pageable pageable=new PageRequest(pageNumber,pageSize);
        return new ResponseEntity<>(multimediaService.findByCompanyIdAndStatus(companyId,
                MultimediaContent.Status.ACTIVE,pageable), HttpStatus.OK);
    }

}
