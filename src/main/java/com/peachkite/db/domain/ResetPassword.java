package com.peachkite.db.domain;

import com.peachkite.db.constants.MongoFields;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Date;

@Data
@Document(collection="ResetPassword")
public class ResetPassword {

    @Id
    private String id;

    @Field(MongoFields.EMAIL)
    private String email;

    @Field(MongoFields.CREATED_AT)
    private Date createdAt;

    @Field(MongoFields.IS_USED)
    private Boolean isUsed;
}
