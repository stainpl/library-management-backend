package com.example.librarymanagement.service.controller;

import com.example.librarymanagement.controller.BookController;
import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addBook_shouldReturnSavedBook() throws Exception {
        Book toSave = new Book("Clean Code", "Robert C. Martin");
        Book saved = new Book("Clean Code", "Robert C. Martin");
        // simulate generated id
        // depending on your Book implementation you may need a setter for id
        // using reflection lightly here isn't ideal — better if your model has setId
        saved.setTitle("Clean Code"); // ensure values present
        when(bookService.add(any(Book.class))).thenReturn(saved);

        mvc.perform(post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toSave)))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.title").value("Clean Code"))
            .andExpect(jsonPath("$.author").value("Robert C. Martin"));

        verify(bookService, times(1)).add(any(Book.class));
    }

    @Test
    void getAllBooks_shouldReturnList() throws Exception {
        Book b1 = new Book("Refactoring", "Martin Fowler");
        Book b2 = new Book("Domain-Driven Design", "Eric Evans");
        when(bookService.findAll()).thenReturn(List.of(b1, b2));

        mvc.perform(get("/api/books"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].title", is("Refactoring")))
            .andExpect(jsonPath("$[1].title", is("Domain-Driven Design")));

        verify(bookService, times(1)).findAll();
    }

    @Test
    void getBookById_whenFound_shouldReturnBook() throws Exception {
        Book b = new Book("The Pragmatic Programmer", "Andy Hunt");
        when(bookService.findById(1L)).thenReturn(Optional.of(b));

        mvc.perform(get("/api/books/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("The Pragmatic Programmer"))
            .andExpect(jsonPath("$.author").value("Andy Hunt"));

        verify(bookService).findById(1L);
    }

    @Test
    void getBookById_whenNotFound_shouldReturn404() throws Exception {
        when(bookService.findById(99L)).thenReturn(Optional.empty());

        mvc.perform(get("/api/books/99"))
            .andExpect(status().isNotFound());

        verify(bookService).findById(99L);
    }

    @Test
    void deleteBook_shouldCallServiceAndReturnOk() throws Exception {
        // No return expected; just ensure service method is called.
        doNothing().when(bookService).delete(5L);

        mvc.perform(delete("/api/books/5"))
            .andExpect(status().isOk()); // adjust to isNoContent() if your controller returns 204

        verify(bookService).delete(5L);
    }
}
