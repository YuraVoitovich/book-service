package io.voitovich.testmodsentask.bookservice.service.impl;

import io.voitovich.testmodsentask.bookservice.dto.BookDto;
import io.voitovich.testmodsentask.bookservice.dto.mapper.BookMapper;
import io.voitovich.testmodsentask.bookservice.entity.Book;
import io.voitovich.testmodsentask.bookservice.events.service.KafkaProducerService;
import io.voitovich.testmodsentask.bookservice.exception.NoSuchRecordException;
import io.voitovich.testmodsentask.bookservice.repository.BookRepository;
import io.voitovich.testmodsentask.bookservice.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final KafkaProducerService kafkaProducerService;
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository, KafkaProducerService kafkaProducerService) {
        this.bookRepository = bookRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Override
    public void deleteBook(UUID uuid) {
        log.info("Deleting book with UUID: {}", uuid);
        bookRepository.deleteById(uuid);
    }

    @Override
    public void takeBook(UUID uuid) {
        log.info("Taking book with UUID: {}", uuid);
        Optional<Book> bookOptional = bookRepository.getBookById(uuid);
        kafkaProducerService.send(bookOptional
                .orElseThrow(() -> new NoSuchRecordException(String
                .format("Book with uuid: {%s} not found", uuid)))
                .getId()
                .toString());
    }

    @Override
    public List<BookDto> getAllBooks() {
        log.info("Getting all books");
        return bookRepository.findAll().stream()
                .map(BookMapper.INSTANCE::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDto getBookById(UUID uuid) {
        log.info("Getting book by UUID: {}", uuid);
        Optional<Book> bookOptional = bookRepository.getBookById(uuid);
        return BookMapper.INSTANCE.toDto(bookOptional
                .orElseThrow(() -> new NoSuchRecordException(String
                        .format("Book with uuid: {%s} not found", uuid))));
    }

    @Override
    public BookDto getBookByISBN(String isbn) {
        log.info("Getting book by ISBN: {}", isbn);
        Optional<Book> bookOptional = bookRepository.getBookByISBN(isbn);
        return BookMapper.INSTANCE.toDto(bookOptional
                .orElseThrow(() -> new NoSuchRecordException(String
                        .format("Book with isbn: {%s} not found", isbn))));
    }

    @Override
    public BookDto addBook(BookDto bookDto) {
        log.info("Adding a new book: {}", bookDto);
        return BookMapper.INSTANCE
                .toDto(bookRepository
                        .save(BookMapper.INSTANCE
                                .toEntity(bookDto)));
    }

    @Override
    public void updateBook(BookDto bookDto) {
        log.info("Updating book: {}", bookDto);
        this.bookRepository.save(BookMapper.INSTANCE.toEntity(bookDto));
    }
}
