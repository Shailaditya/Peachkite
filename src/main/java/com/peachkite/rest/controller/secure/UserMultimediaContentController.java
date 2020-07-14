package com.peachkite.rest.controller.secure;

import com.peachkite.db.domain.MultimediaContent;
import com.peachkite.service.MultimediaContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/secure/content")
public class UserMultimediaContentController {

    @Autowired
    private MultimediaContentService multimediaService;

    @RequestMapping("")
    ResponseEntity<?> getContent(@RequestParam String companyId,
                                  @RequestParam Integer pageNumber,
                                  @RequestParam Integer pageSize){
        Pageable pageable=new PageRequest(pageNumber,pageSize);
        return new ResponseEntity<>(multimediaService.findByCompanyIdAndStatus(companyId,
                MultimediaContent.Status.ACTIVE,pageable),HttpStatus.OK);
    }

    @RequestMapping("/{id}")
    ResponseEntity<?> getContentById(@PathVariable String id){
        MultimediaContent content=multimediaService.findOne(id);
        if(content == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<>(content,HttpStatus.OK);
    }
}
