package io.voitovich.testmodsentask.bookservice.dto;


import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private UUID id;

    private String ISBN;

    private String name;

    private String genre;

    private String author;

}
