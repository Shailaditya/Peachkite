package com.peachkite.db.domain;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.peachkite.db.constants.MongoFields;
import com.peachkite.db.domain.Company.CompanyBenefit;
import com.peachkite.enums.MartialStatus;

import lombok.Data;

@Data
@Document(collection="Feedback")
public class Feedback {
	
	@Id
	private String id;
	
	@Field(MongoFields.USER_ID)
	private String userId;
	
	@Field(MongoFields.USER_MARTIAL_STATUS)
	private MartialStatus userMartialStatus;
	
	@Field(MongoFields.COMPANY_ID)
	private String companyId;
	
	@Field(MongoFields.LOCATION)
	private String location;
	
	@Field(MongoFields.USER_ASSOCIATION_DATE)
	private Date userAssociationDate;
	
	@Field(MongoFields.USER_ASSOCIATION_YEAR)
	private String userAssociationYear;

	@Field(MongoFields.USER_DESIGNATION)
	private String userDesignation;
	
	@Field(MongoFields.COMMENT)
	private String comment;
	
	@Field(MongoFields.RATING)
	private Double rating;
	
	@Field(MongoFields.BENEFITS)
	private List<CompanyBenefit> benefits;
	
	@Field(MongoFields.CREATED_AT)
	private Date createdAt;
}
