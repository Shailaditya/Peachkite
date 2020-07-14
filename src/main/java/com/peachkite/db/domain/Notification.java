package com.peachkite.db.domain;

import com.peachkite.db.constants.MongoFields;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection="Notification")
public class Notification {

    public enum Status {
        Pending,Replied,NA;
    }

    public enum Type {
        Query,Review;
    }

    @Id
    private String id;

    @Field(MongoFields.FROM)
    private String from;

    @Field(MongoFields.COMPANY_ID)
    private String companyId;

    @Field(MongoFields.DETAIL)
    private String detail;

    @Field(MongoFields.RESPONSE)
    private String response;

    @Field(MongoFields.STATUS)
    private Status status;

    @Field(MongoFields.TYPE)
    private Type type;

    @Field(MongoFields.CREATED_AT)
    private Date createdAt;

    @Field(MongoFields.REPLIED_DATE)
    private Date repliedDate;

    @Field(MongoFields.FEEDBACK_ID)
    private String feedbackId;

    @Field(MongoFields.QUERY_ID)
    private String queryId;
}
