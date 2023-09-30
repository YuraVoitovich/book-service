package io.voitovich.testmodsentask.bookservice.service;

import io.voitovich.testmodsentask.bookservice.dto.BookDto;

import java.util.List;
import java.util.UUID;

public interface BookService {

    List<BookDto> getAllBooks();

    BookDto getBookById(UUID uuid);

    BookDto getBookByISBN(String isbn);

    void addBook(BookDto bookDto);

    void updateBook(BookDto bookDto);

    void deleteBook(UUID uuid);

    void takeBook(UUID uuid);
}
