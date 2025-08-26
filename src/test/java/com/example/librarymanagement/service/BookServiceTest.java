package com.example.librarymanagement.service;

import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.BorrowRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock BookRepository bookRepo;
    @Mock BorrowRecordRepository borrowRepo;
    @InjectMocks BookService svc;

    @Test
    void addShouldSaveBook() {
        Book b = new Book("T","A");
        when(bookRepo.save(b)).thenReturn(b);

        assertThat(svc.add(b)).isSameAs(b);
        verify(bookRepo).save(b);
    }

    @Test
    void findAllShouldReturnList() {
        List<Book> all = List.of(new Book("X","Y"));
        when(bookRepo.findAll()).thenReturn(all);

        assertThat(svc.findAll()).isEqualTo(all);
    }

    @Test
    void deleteRemovesBorrowRecordsThenBook() {
        long id = 7L;
        when(bookRepo.existsById(id)).thenReturn(true);

        svc.delete(id);

        InOrder ord = inOrder(borrowRepo, bookRepo);
        ord.verify(borrowRepo).deleteByBookId(id);
        ord.verify(bookRepo).deleteById(id);
    }

    @Test
    void deleteMissingShouldThrow() {
        when(bookRepo.existsById(9L)).thenReturn(false);

        assertThatThrownBy(() -> svc.delete(9L))
          .isInstanceOf(NoSuchElementException.class);
    }
}
