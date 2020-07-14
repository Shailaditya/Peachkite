package com.peachkite.db.domain;

import com.peachkite.db.constants.MongoFields;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection="CompanyClaimRequest")
@Data
public class CompanyClaimRequest {

    @Id
    private String id;

    @Field(MongoFields.NAME)
    private String companName;

    @Field(MongoFields.EMAIL)
    private String userEmail;

    @Field(MongoFields.COMPANY_ID)
    private String companyId;

    @Field(MongoFields.CREATED_AT)
    private Date createdAt;

    @Field(MongoFields.IS_APPROVED)
    private Boolean isApproved;
}
