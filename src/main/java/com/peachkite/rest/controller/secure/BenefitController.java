package com.peachkite.rest.controller.secure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.peachkite.db.domain.Benefit;
import com.peachkite.db.repository.BenefitRepository;

@RestController
@RequestMapping("/api/secure/benefit")
public class BenefitController {
	
	@Autowired
	BenefitRepository benefitRepository;
	
	@RequestMapping("")
	ResponseEntity<List<Benefit>> getAll(){
		return new ResponseEntity<>(this.benefitRepository.findAll(),HttpStatus.OK);
	}

}
