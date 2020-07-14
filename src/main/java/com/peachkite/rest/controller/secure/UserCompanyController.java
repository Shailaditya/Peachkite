package com.peachkite.rest.controller.secure;

import com.peachkite.db.constants.MongoFields;
import com.peachkite.db.domain.Feedback;
import com.peachkite.service.FeedbackService;
import com.peachkite.vo.ReviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.peachkite.db.domain.Company;
import com.peachkite.service.CompanyService;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/secure/company")
public class UserCompanyController {
	
	@Autowired
	CompanyService companyService;

	@Autowired
	FeedbackService feedbackService;

	@RequestMapping("/{id}")
	ResponseEntity<Company> findCompany(@PathVariable String id){
		Company company=this.companyService.findCompanyById(id);
		if(company == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		else
			return new ResponseEntity<>(company,HttpStatus.OK);
	}

	@RequestMapping(value="/{companyId}/query",method= RequestMethod.POST)
	ResponseEntity<?> companyQuery(@RequestBody String query,@PathVariable String companyId) throws MessagingException {
		companyService.createQuery(companyId,query);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@RequestMapping(value="/{companyId}/view",method= RequestMethod.POST)
	ResponseEntity<?> view(@PathVariable String companyId) throws MessagingException {
		companyService.viewCompany(companyId);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	}

	@RequestMapping("/{companyId}/review")
	ResponseEntity<?> getReviews(@PathVariable String companyId,
										@RequestParam Integer pageNumber,
									   @RequestParam Integer pageSize ){
		Pageable pageable=new PageRequest(pageNumber,pageSize,new Sort(Sort.Direction.DESC, MongoFields.CREATED_AT));
		Page<Feedback> feedbacks=feedbackService.getFeedbackByCompanyId(companyId,false,pageable);
		List<ReviewVO> reviews=new ArrayList<>();
		for(Feedback feedback:feedbacks){
			ReviewVO review=new ReviewVO();
			review.setMartialStatus(feedback.getUserMartialStatus());
			review.setDate(feedback.getCreatedAt());
			review.setComment(feedback.getComment());
			reviews.add(review);
		}
		return new ResponseEntity<>(new PageImpl<>(reviews,pageable,feedbacks.getTotalElements()),HttpStatus.OK);
	}

}

