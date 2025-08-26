package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.model.BorrowRecord;
import com.example.librarymanagement.model.User;
import com.example.librarymanagement.service.BorrowService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BorrowController.class)
class BorrowControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BorrowService borrowService;

    @Test
    void borrow_shouldReturnBorrowRecord() throws Exception {
        User u = new User("Alice", "a@x.com");
        u.setId(1L);
        Book b = new Book("The Book", "Author");
        b.setId(2L);

        BorrowRecord rec = new BorrowRecord();
        rec.setId(100L);
        rec.setUser(u);
        rec.setBook(b);
        rec.setBorrowedAt(LocalDateTime.now());

        when(borrowService.borrow(1L, 2L)).thenReturn(rec);

        mvc.perform(post("/api/borrow")
                .param("userId", "1")
                .param("bookId", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(100))
            .andExpect(jsonPath("$.book.id").value(2))
            .andExpect(jsonPath("$.user.id").value(1));

        verify(borrowService).borrow(1L, 2L);
    }

    @Test
    void return_shouldReturnBorrowRecordWithReturnedAt() throws Exception {
        BorrowRecord rec = new BorrowRecord();
        rec.setId(200L);
        Book b = new Book("Returnable", "Auth");
        b.setId(2L);
        rec.setBook(b);
        rec.setReturnedAt(LocalDateTime.now());

        when(borrowService.returnBook(2L)).thenReturn(rec);

        mvc.perform(post("/api/borrow/return")
                .param("bookId", "2"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(200))
            .andExpect(jsonPath("$.returnedAt").isNotEmpty());

        verify(borrowService).returnBook(2L);
    }

    @Test
    void getActiveBorrows_shouldReturnList() throws Exception {
        BorrowRecord r1 = new BorrowRecord();
        User u1 = new User("U1","u1@x");
        u1.setId(1L);
        Book b1 = new Book("B1","A1"); b1.setId(11L);
        r1.setId(501L); r1.setUser(u1); r1.setBook(b1);

        BorrowRecord r2 = new BorrowRecord();
        User u2 = new User("U2","u2@x");
        u2.setId(2L);
        Book b2 = new Book("B2","A2"); b2.setId(12L);
        r2.setId(502L); r2.setUser(u2); r2.setBook(b2);

        when(borrowService.getActiveBorrows()).thenReturn(List.of(r1, r2));

        mvc.perform(get("/api/borrow/active"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
            .andExpect(jsonPath("$[0].id").value(501))
            .andExpect(jsonPath("$[1].id").value(502));

        verify(borrowService).getActiveBorrows();
    }
}
