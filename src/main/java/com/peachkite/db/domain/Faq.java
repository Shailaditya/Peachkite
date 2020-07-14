package com.peachkite.db.domain;

import com.peachkite.db.constants.MongoFields;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection="Faq")
public class Faq {

    public enum Status {
        Saved,Published;
    }

    @Id
    private String id;

    @Field(MongoFields.SERIAL_NUMBER)
    private Integer serialNumber;

    @Field(MongoFields.COMPANY_ID)
    private String companyId;

    @Field(MongoFields.QUESTION)
    private String question;

    @Field(MongoFields.RESPONSE)
    private String response;

    @Field(MongoFields.STATUS)
    private Status status;

    @Field(MongoFields.IS_SELECTED)
    private Boolean isSelected;
}
