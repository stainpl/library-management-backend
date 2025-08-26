package com.example.librarymanagement.service;

import org.mockito.junit.jupiter.MockitoExtension;
import com.example.librarymanagement.model.*;
import com.example.librarymanagement.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BorrowServiceTest {

    @Mock UserRepository userRepo;
    @Mock BookRepository bookRepo;
    @Mock BorrowRecordRepository borrowRepo;
    @InjectMocks BorrowService svc;

    @Test
    void borrowHappyPath() {
        User u = new User("U", "u@u");
        Book b = new Book("Title", "Auth");
        b.setAvailable(true);

        when(userRepo.findById(1L)).thenReturn(Optional.of(u));
        when(bookRepo.findById(2L)).thenReturn(Optional.of(b));
        when(borrowRepo.save(any())).thenAnswer(a -> a.getArgument(0));

        BorrowRecord rec = svc.borrow(1L, 2L);

        assertThat(rec.getUser()).isSameAs(u);
        assertThat(rec.getBook()).isSameAs(b);
        assertThat(b.isAvailable()).isFalse();
    }

    @Test
    void borrowWhenNotAvailableShouldThrow() {
        Book b = new Book();
        b.setAvailable(false);
        when(userRepo.findById(1L)).thenReturn(Optional.of(new User()));
        when(bookRepo.findById(2L)).thenReturn(Optional.of(b));

        assertThatThrownBy(() -> svc.borrow(1L, 2L))
          .hasMessage("Book is already borrowed");
    }

    @Test
    void returnHappyPath() {
        Book b = new Book();
        b.setAvailable(false);

        BorrowRecord rec = new BorrowRecord();
        rec.setBook(b);
        rec.setBorrowedAt(LocalDateTime.now());

        when(borrowRepo.findByBookIdAndReturnedAtIsNull(2L))
          .thenReturn(Optional.of(rec));
        when(borrowRepo.save(any())).thenAnswer(a -> a.getArgument(0));

        BorrowRecord out = svc.returnBook(2L);

        assertThat(out.getReturnedAt()).isNotNull();
        assertThat(b.isAvailable()).isTrue();
    }

    @Test
    void returnWhenNoActiveBorrow() {
        when(borrowRepo.findByBookIdAndReturnedAtIsNull(3L))
          .thenReturn(Optional.empty());

        assertThatThrownBy(() -> svc.returnBook(3L))
          .hasMessage("Active borrow not found");
    }
}
