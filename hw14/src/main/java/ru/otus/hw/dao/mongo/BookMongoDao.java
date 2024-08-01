package ru.otus.hw.dao.mongo;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "books")
@Builder
public class BookMongoDao {
    @Id
    private String id;

    private String title;

    private AuthorMongoDao author;

    private List<GenreMongoDao> genres;
}
