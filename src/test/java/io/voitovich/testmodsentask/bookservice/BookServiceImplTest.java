package io.voitovich.testmodsentask.bookservice;


import io.voitovich.testmodsentask.bookservice.dto.BookDto;
import io.voitovich.testmodsentask.bookservice.dto.mapper.BookMapper;
import io.voitovich.testmodsentask.bookservice.entity.Book;
import io.voitovich.testmodsentask.bookservice.events.service.KafkaProducerService;
import io.voitovich.testmodsentask.bookservice.repository.BookRepository;
import io.voitovich.testmodsentask.bookservice.service.impl.BookServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private KafkaProducerService kafkaProducerService;

    @InjectMocks
    private BookServiceImpl bookService;
    private AutoCloseable closeable;

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        bookService = new BookServiceImpl(bookRepository, kafkaProducerService);
    }

    @AfterEach
    public void tearDown() throws Exception {
        // Закрыть моки после каждого теста
        closeable.close();
    }

    @Test
    public void testDeleteBook() {
        UUID uuid = UUID.randomUUID();
        bookService.deleteBook(uuid);
        verify(bookRepository, times(1)).deleteById(uuid);
    }

    @Test
    public void testTakeBook() {
        UUID uuid = UUID.randomUUID();
        bookService.takeBook(uuid);
        verify(kafkaProducerService, times(1)).send(uuid.toString());
    }

    @Test
    public void testGetAllBooks() {
        List<Book> books = new ArrayList<>();
        books.add(new Book());
        when(bookRepository.findAll()).thenReturn(books);

        List<BookDto> bookDtos = bookService.getAllBooks();
        assertEquals(1, bookDtos.size());
    }

    @Test
    public void testGetBookById() {
        UUID uuid = UUID.randomUUID();
        Book book = new Book();
        book.setId(uuid);
        when(bookRepository.getBookById(uuid)).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.getBookById(uuid);
        assertEquals(uuid, bookDto.getId());
    }

    @Test
    public void testGetBookByISBN() {
        String isbn = "1234567890";
        Book book = new Book();
        book.setISBN(isbn);
        when(bookRepository.getBookByISBN(isbn)).thenReturn(Optional.of(book));

        BookDto bookDto = bookService.getBookByISBN(isbn);
        assertEquals(isbn, bookDto.getISBN());
    }

    @Test
    public void testAddBook() {
        BookDto bookDto = new BookDto();
        bookService.addBook(bookDto);
        verify(bookRepository, times(1)).save(BookMapper.INSTANCE.toEntity(bookDto));
    }

    @Test
    public void testUpdateBook() {
        BookDto bookDto = new BookDto();
        bookService.updateBook(bookDto);
        verify(bookRepository, times(1)).save(BookMapper.INSTANCE.toEntity(bookDto));
    }
}