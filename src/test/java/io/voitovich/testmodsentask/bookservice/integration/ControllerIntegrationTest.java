package io.voitovich.testmodsentask.bookservice.integration;

import io.voitovich.testmodsentask.bookservice.dto.BookDto;
import io.voitovich.testmodsentask.bookservice.entity.Book;
import io.voitovich.testmodsentask.bookservice.repository.BookRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
@ActiveProfiles("test")
public class ControllerIntegrationTest {



    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Autowired
    private BookRepository bookRepository;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Container
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:latest")
    );


    @BeforeEach
    public void beforeEach() {
        bookRepository.deleteAll();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry propertyRegistry) {
        propertyRegistry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        propertyRegistry.add("spring.datasource.url", postgres::getJdbcUrl);
        propertyRegistry.add("spring.datasource.username", postgres::getUsername);
        propertyRegistry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    @WithMockUser(roles = "modsen-user")
    public void testGetAllBooksShouldReturnAllBooks() throws Exception {

        bookRepository.save(Book
                .builder()
                .author("author")
                .genre("genre")
                .ISBN("12345678")
                .name("name")
                .build());

        bookRepository.save(Book
                .builder()
                .author("author")
                .genre("genre")
                .ISBN("12345678")
                .name("name")
                .build());

        mockMvc.perform(MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

    }


    @Test
    @WithMockUser(roles = "modsen-user")
    public void testGetBookByIdShouldReturnBookWithValidId() throws Exception {
        UUID uuid = bookRepository.save(Book
                .builder()
                .author("author")
                .genre("genre")
                .ISBN("12")
                .name("name")
                .build()).getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/book/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(uuid.toString()));
    }

    @Test
    @WithMockUser(roles = "modsen-user")
    public void testGetBookByIsbnShouldReturnBookWithValidIsbn() throws Exception {
        String isbn = "1234567890";
        bookRepository.save(Book
                .builder()
                .author("author")
                .genre("genre")
                .ISBN(isbn)
                .name("name")
                .build());
        mockMvc.perform(MockMvcRequestBuilders.get("/book-isbn/" + isbn)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.isbn").value(isbn));
    }

    @Test
    @WithMockUser(roles = "modsen-user")
    public void testTakeBookShouldReturnOkStatus() throws Exception {
        UUID uuid = bookRepository.save(Book
                .builder()
                .author("author")
                .genre("genre")
                .ISBN("12345678")
                .name("name")
                .build()).getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/book/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "modsen-admin")
    public void testUpdateBookShouldReturnOkStatus() throws Exception {
        BookDto bookDto = new BookDto();
        UUID uuid = UUID.randomUUID();
        bookRepository.save(Book
                .builder()
                .id(uuid)
                .author("author")
                .genre("genre")
                .ISBN("12345678")
                .name("name")
                .build());
        String bookDtoJson = "{ \"id\": \"" + uuid.toString() + "\"" +
                ", \"isbn\": \"1234567890\"" +
                ", \"name\": \"BookName\"" +
                ", \"genre\": \"Fiction\"" +
                ", \"author\": \"AuthorName\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithMockUser(roles = "modsen-admin")
    public void testAddBookShouldReturnCreatedStatusAndValidBook() throws Exception {
        // Create a BookDto and convert it to JSON
        BookDto bookDto = new BookDto();
        String bookDtoJson =
                "{ \"isbn\": \"1234567890\"" +
                ", \"name\": \"BookName\"" +
                ", \"genre\": \"Fiction\"" +
                ", \"author\": \"AuthorName\" }";
        mockMvc.perform(MockMvcRequestBuilders.put("/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookDtoJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.genre").value("Fiction"))
                .andExpect(jsonPath("$.isbn").value("1234567890"))
                .andExpect(jsonPath("$.name").value("BookName"))
                .andExpect(jsonPath("$.author").value("AuthorName"));
    }

    @Test
    @WithMockUser(roles = "modsen-admin")
    public void testDeleteBookShouldReturnOkStatus() throws Exception {
        UUID uuid = UUID.randomUUID();
        bookRepository.save(Book
                .builder()
                .id(uuid)
                .author("author")
                .genre("genre")
                .ISBN("12345678")
                .name("name")
                .build());
        mockMvc.perform(MockMvcRequestBuilders.delete("/book/" + uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
