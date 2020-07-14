package com.peachkite.db.domain;

import com.peachkite.db.constants.MongoFields;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Document(collection="CompanyQuery")
@Data
public class CompanyQuery {

    public enum Status{
        PENDNG,REPLIED
    }

    @Id
    private String id;

    @Field(MongoFields.COMPANY_ID)
    private String companyId;

    @Field(MongoFields.USER_ID)
    private String userId;

    @Field(MongoFields.QUESTION)
    private String queryText;

    @Field(MongoFields.STATUS)
    private Status status;

    @Field(MongoFields.RESPONSE)
    private String response;

    @Field(MongoFields.CREATED_AT)
    private Date createdAt;

    @Field(MongoFields.REPLIED_DATE)
    private Date repliedDate;

}
