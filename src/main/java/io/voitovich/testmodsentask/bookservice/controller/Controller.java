package io.voitovich.testmodsentask.bookservice.controller;


import io.voitovich.testmodsentask.bookservice.dto.BookDto;
import io.voitovich.testmodsentask.bookservice.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static io.voitovich.testmodsentask.bookservice.controller.utils.UUIDUtils.getUUIDFromString;

@RestController("/api")
public class Controller {

    private final BookService bookService;

    public Controller(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('modsen-user')")
    ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }


    @GetMapping("/book/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('modsen-user')")
    ResponseEntity<BookDto> getBookById(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(bookService.getBookById(getUUIDFromString(id)));
    }

    @GetMapping("/book-isbn/{isbn}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('modsen-user')")
    ResponseEntity<BookDto> getBookByIsbn(@PathVariable(name = "isbn") String isbn) {
        return ResponseEntity.ok(bookService.getBookByISBN(isbn));
    }

    @PostMapping("book/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('modsen-user')")
    void takeBook(@PathVariable(name = "id") String id) {
        bookService.takeBook(getUUIDFromString(id));
    }
    @PostMapping("/book")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('modsen-admin')")
    void updateBook(@RequestBody BookDto bookDto) {
        bookService.updateBook(bookDto);
    }

    @PutMapping("/book")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('modsen-admin')")
    void addBook(@RequestBody BookDto bookDto) {
        bookService.addBook(bookDto);
    }

    @DeleteMapping("/book/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('modsen-admin')")
    void deleteBook(@PathVariable(name = "id") String id) {
        bookService.deleteBook(getUUIDFromString(id));
    }

}
