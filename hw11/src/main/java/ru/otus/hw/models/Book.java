package ru.otus.hw.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@AllArgsConstructor(onConstructor_ = @PersistenceCreator)
@NoArgsConstructor
@Document(collation = "books")
public class Book {
    @Id
    private String id;

    @Field(name = "title")
    private String title;

    @Field(name = "author")
    private Author author;

    @Field(name = "genre")
    private List<Genre> genres;
}
