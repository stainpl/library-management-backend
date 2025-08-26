package com.example.librarymanagement.integration;

import com.example.librarymanagement.LibraryManagementApplication;
import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.model.BorrowRecord;
import com.example.librarymanagement.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest(classes = LibraryManagementApplication.class,
                webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ApplicationIntegrationTest {

    @Autowired
    private TestRestTemplate rest;

    private HttpHeaders headers;

    @BeforeEach
    void beforeEach() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void fullCrudBorrowReturnFlow() {
        // 1) Create a user
        User createUser = new User("Integration User", "integration@example.com");
        ResponseEntity<User> userResp = rest.postForEntity("/api/users", createUser, User.class);
        assertThat(userResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        User createdUser = userResp.getBody();
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getId()).isNotNull();

        Long userId = createdUser.getId();

        // 2) Create a book
        Book createBook = new Book("Integration Testing with Spring", "Tester");
        ResponseEntity<Book> bookResp = rest.postForEntity("/api/books", createBook, Book.class);
        assertThat(bookResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        Book createdBook = bookResp.getBody();
        assertThat(createdBook).isNotNull();
        assertThat(createdBook.getId()).isNotNull();
        assertThat(createdBook.isAvailable()).isTrue();

        Long bookId = createdBook.getId();

        // 3) Fetch all books and assert our book is present
        ResponseEntity<List<Book>> booksListResp = rest.exchange(
                "/api/books",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Book>>() {}
        );
        assertThat(booksListResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Book> books = booksListResp.getBody();
        assertThat(books).extracting(Book::getId).contains(bookId);

        // 4) Borrow the book
        ResponseEntity<BorrowRecord> borrowResp = rest.postForEntity(
                String.format("/api/borrow?userId=%d&bookId=%d", userId, bookId),
                null,
                BorrowRecord.class
        );
        assertThat(borrowResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        BorrowRecord borrowed = borrowResp.getBody();
        assertThat(borrowed).isNotNull();
        assertThat(borrowed.getBook()).isNotNull();
        assertThat(borrowed.getBook().getId()).isEqualTo(bookId);
        assertThat(borrowed.getReturnedAt()).isNull();

        // 5) Fetch book by id and check availability is false
        ResponseEntity<Book> bookAfterBorrowResp = rest.getForEntity("/api/books/" + bookId, Book.class);
        assertThat(bookAfterBorrowResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        Book bookAfterBorrow = bookAfterBorrowResp.getBody();
        assertThat(bookAfterBorrow).isNotNull();
        assertThat(bookAfterBorrow.isAvailable()).isFalse();

        // 6) Return the book
        ResponseEntity<BorrowRecord> returnResp = rest.postForEntity(
                String.format("/api/borrow/return?bookId=%d", bookId),
                null,
                BorrowRecord.class
        );
        // return should succeed (200 OK) and returnedAt should be set
        assertThat(returnResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        BorrowRecord returned = returnResp.getBody();
        assertThat(returned).isNotNull();
        assertThat(returned.getReturnedAt()).isNotNull();

        // 7) Book should be available again
        ResponseEntity<Book> bookAfterReturnResp = rest.getForEntity("/api/books/" + bookId, Book.class);
        assertThat(bookAfterReturnResp.getStatusCode()).isEqualTo(HttpStatus.OK);
        Book bookAfterReturn = bookAfterReturnResp.getBody();
        assertThat(bookAfterReturn).isNotNull();
        assertThat(bookAfterReturn.isAvailable()).isTrue();

        // 8) Delete the book (should succeed, 204 No Content expected)
        ResponseEntity<Void> deleteBookResp = rest.exchange("/api/books/" + bookId, HttpMethod.DELETE, null, Void.class);
        assertThat(deleteBookResp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // 9) Delete the user
        ResponseEntity<Void> deleteUserResp = rest.exchange("/api/users/" + userId, HttpMethod.DELETE, null, Void.class);
        assertThat(deleteUserResp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // 10) Final: lists should not contain deleted entries
        List<User> remainingUsers = rest.exchange("/api/users", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<User>>() {}).getBody();
        List<Book> remainingBooks = rest.exchange("/api/books", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Book>>() {}).getBody();

        assertThat(remainingUsers).doesNotContain(createdUser);
        assertThat(remainingBooks).doesNotContain(createdBook);
    }
}
