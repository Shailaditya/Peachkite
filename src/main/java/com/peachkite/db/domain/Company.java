package com.peachkite.db.domain;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.peachkite.db.constants.MongoFields;
import lombok.Data;

@Document(collection="Company")
@Data
public class Company {
	
	@Id
	private String id;
	
	@Field(MongoFields.NAME)
	private String name;
	
	@Field(MongoFields.DESCRIPTION)
	private String description;

	@Field(MongoFields.LOGO)
	private String logo;

	@Field(MongoFields.LOCATION_HQ)
	private String locationHq;

	@Field(MongoFields.EMPLOYEE_RANGE)
	private String employeeRange;

	@Field(MongoFields.OFFICES)
	private List<Address> offices;

	@Field(MongoFields.SIZE)
	private Integer size;

	@Field(MongoFields.INDUSTRY)
	private String industry;

	@Field(MongoFields.ABOUT_US)
	private String aboutUs;

	@Field(MongoFields.ABOUT_US_IMAGE)
	private String aboutUsImage;

	@Field(MongoFields.IS_VERIFIED)
	private Boolean isVerified;

	@Field(MongoFields.RATING)
	private Double rating;

	@Field(MongoFields.CATEGORY_RATINGS)
	private Map<Integer,Double> categoryRatings;

	@Field(MongoFields.BENEFITS)
	private List<CompanyBenefit> benefits;

	@Field(MongoFields.FEEDBACK_COUNT)
	private Long feedbackCount;

	@Field(MongoFields.COMMENT_COUNT)
	private Long commentCount;

	@Field(MongoFields.SEARCH_COUNT)
	private Long searchCount;

	@Field(MongoFields.BENEFIT_DESCRIPTION)
	private String benefitDescription;

	@Field(MongoFields.ADMIN_USER_ID)
	private String adminUserId;
	
	@Data
	public static class Address{
		@Field(MongoFields.ADDRESS_LINE_1)
		private String addressLine1;
		@Field(MongoFields.ADDRESS_LINE_2)
		private String addressLine2;
		@Field(MongoFields.ZIPCODE)
		private String zipcode;
		@Field(MongoFields.CITY)
		private String city;
		@Field(MongoFields.STATE)
		private String state;
		@Field(MongoFields.COUNTRY)
		private String country;
	}
	
	@Data
	public static class CompanyBenefit {

		public static Integer TYPE_STANDARD=1;
		public static Integer TYPE_COMPANY_SPECIFIC=2;

		private String id;

		@Field(MongoFields.LABEL)
		private String label;

		@Field(MongoFields.RATING)
		private Double rating;

		@Field(MongoFields.TYPE)
		private Integer type;

		@Field(MongoFields.CATEGORY)
		private Integer category;

		@Field(MongoFields.IS_SELECTED)
		private Boolean isSelected;
	}
}
