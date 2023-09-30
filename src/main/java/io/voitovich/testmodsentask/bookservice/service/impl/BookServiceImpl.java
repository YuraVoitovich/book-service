package io.voitovich.testmodsentask.bookservice.service.impl;

import io.voitovich.testmodsentask.bookservice.dto.BookDto;
import io.voitovich.testmodsentask.bookservice.dto.mapper.BookMapper;
import io.voitovich.testmodsentask.bookservice.entity.Book;
import io.voitovich.testmodsentask.bookservice.events.source.KafkaProducerService;
import io.voitovich.testmodsentask.bookservice.repository.BookRepository;
import io.voitovich.testmodsentask.bookservice.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


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
        bookRepository.deleteById(uuid);
    }

    @Override
    public void takeBook(UUID uuid) {
        this.kafkaProducerService.send(uuid.toString());
    }

    @Override
    public List<BookDto> getAllBooks() {
        return bookRepository.findAll().stream().map(BookMapper.INSTANCE::toDto).collect(Collectors.toList());
    }

    @Override
    public BookDto getBookById(UUID uuid) {

        Optional<Book> bookOptional = bookRepository.getBookById(uuid);

        return BookMapper.INSTANCE.toDto(bookOptional.orElseThrow(RuntimeException::new));
    }

    @Override
    public BookDto getBookByISBN(String isbn) {

        Optional<Book> bookOptional = bookRepository.getBookByISBN(isbn);


        return BookMapper.INSTANCE.toDto(bookOptional.orElseThrow());
    }

    @Override
    public void addBook(BookDto bookDto) {
        this.bookRepository.save(BookMapper.INSTANCE.toEntity(bookDto));
    }

    @Override
    public void updateBook(BookDto bookDto) {
        this.bookRepository.save(BookMapper.INSTANCE.toEntity(bookDto));
    }
}
