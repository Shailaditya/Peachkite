package com.peachkite.db.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.peachkite.db.constants.MongoFields;
import lombok.Data;

@Data
@Document(collection="Benefit")
public class Benefit {

	public static Integer CATEGORY_FLEXIBILITY=1;
	public static Integer CATEGORY_DIVERSITY=2;
	public static Integer CATEGORY_WORK_LIFE_BALANCE=3;
	public static Integer CATEGORY_ENRICHMENT=4;
	
	@Id
	private String id;

	@Field(MongoFields.LABEL)
	private String label;

	@Field(MongoFields.CATEGORY)
	private Integer category;
}
