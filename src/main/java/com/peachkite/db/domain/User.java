package com.peachkite.db.domain;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.peachkite.db.constants.MongoFields;
import com.peachkite.enums.MartialStatus;
import com.peachkite.vo.UserVO;

import lombok.Data;

@Document(collection="User")
public @Data class User {
	
	public static final String ROLE_USER="ROLE_USER";
	public static final String ROLE_COMPANY_ADMIN="ROLE_COMPANY_ADMIN";
	public static final String ROLE_SUPER_ADMIN="ROLE_SUPER_ADMIN";
	
	@Id
	private String id;
	
	@Field(MongoFields.EMAIL)
	private String email;
	
	@Field(MongoFields.PASSWORD)
	private String password;
	
	@Field(MongoFields.FIRST_NAME)
	private String firstName;
	
	@Field(MongoFields.MIDDLE_NAME)
	private String middleName;
	
	@Field(MongoFields.LAST_NAME)
	private String lastName;
	
	@Field(MongoFields.COMPANY_ID)
	private String companyId;
	
	@Field(MongoFields.MARTIAL_STATUS)
	private MartialStatus martialStatus; 
	
	@Field(MongoFields.ROLES)
	private List<String> roles;
	
	public User() {
		
	}
	
	public User(UserVO userVO) {
		this.id=userVO.getUserId();
		this.email=userVO.getEmail();
		this.firstName=userVO.getFirstName();
		this.middleName=userVO.getMiddleName();
		this.lastName=userVO.getLastName();
		this.companyId=userVO.getCompanyId();
		this.martialStatus=userVO.getMartialStatus();
		this.roles=userVO.getRoles();
	}
}
