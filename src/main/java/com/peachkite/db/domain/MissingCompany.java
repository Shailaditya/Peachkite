package com.peachkite.db.domain;

import com.peachkite.db.constants.MongoFields;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection="MissingCompany")
public class MissingCompany {

    @Id
    private String id;

    @Field(MongoFields.NAME)
    private String name;

    @Field(MongoFields.EMAIL)
    private String email;
}
