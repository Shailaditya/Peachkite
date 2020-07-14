package com.peachkite.rest.controller.secure;

import com.peachkite.service.CompanyService;
import com.peachkite.service.FeedbackService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.peachkite.db.domain.Feedback;

@RestController
@RequestMapping("/api/secure/feedback")
public class FeedbackController {

	@Autowired
	FeedbackService feedbackService;

	@Autowired
	CompanyService companyService;
	
	@RequestMapping(value="",method=RequestMethod.POST)
	ResponseEntity<?> createFeedback(@RequestBody Feedback feedback) throws Exception{
		if(this.feedbackService.checkFeedbackExists(feedback.getUserId(),feedback.getCompanyId()))
			return new ResponseEntity<Object>(HttpStatus.CONFLICT);
		this.feedbackService.create(feedback);
		this.companyService.incrementFeedbackCount(feedback.getCompanyId(),1);
		this.companyService.updateRating(feedback.getCompanyId());
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@RequestMapping(value="/validate",method=RequestMethod.POST)
	ResponseEntity<?> checkAlreadyReviewed(@RequestBody CheckFeedbackBody body) throws Exception{
		HttpStatus status=this.feedbackService.checkFeedbackExists(body.getUserId(),body.getCompanyId())?
				HttpStatus.CONFLICT:HttpStatus.OK;
		return new ResponseEntity<>(status);
	}


}

@Data
class CheckFeedbackBody{
	private String userId;
	private String companyId;
}