package io.voitovich.testmodsentask.bookservice.repository;

import io.voitovich.testmodsentask.bookservice.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {
    Optional<Book> getBookById(UUID uuid);

    Optional<Book> getBookByISBN(String isbn);


}
