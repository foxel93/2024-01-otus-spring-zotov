package ru.otus.hw.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = @PersistenceCreator)
@Document(collection = "comments")
public class Comment {
    @Id
    private String id;

    @Field(name = "text")
    private String text;

    @DBRef(lazy = true)
    private Book book;
}
