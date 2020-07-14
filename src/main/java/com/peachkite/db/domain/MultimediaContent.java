package com.peachkite.db.domain;

import com.peachkite.db.constants.MongoFields;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection = "MultimediaContent")
public class MultimediaContent {

    public enum Status{
        ACTIVE,SAVED
    }

    public enum Type{
        LINK,YOUTUBE_VIDEO
    }

    @Id
    private String id;

    @Field(MongoFields.COMPANY_ID)
    private String companyId;

    @Field(MongoFields.TYPE)
    private Type type;

    @Field(MongoFields.TITLE)
    private String title;

    @Field(MongoFields.URL)
    private String url;

    @Field(MongoFields.THUMBNAIL)
    private String thumbnail;

    @Field(MongoFields.START_DATE)
    private Date startDate;

    @Field(MongoFields.END_DATE)
    private Date endDate;

    @Field(MongoFields.STATUS)
    private Status status;

}
