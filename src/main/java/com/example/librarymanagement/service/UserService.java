package com.example.librarymanagement.service;

import com.example.librarymanagement.model.User;
import com.example.librarymanagement.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepo;
    @Mock BorrowRecordRepository borrowRepo;
    @InjectMocks UserService svc;

    @Test
    void createShouldSaveAndReturnUser() {
        User u = new User("Alice","a@x.com");
        when(userRepo.save(u)).thenReturn(u);

        User result = svc.create(u);

        assertThat(result).isSameAs(u);
        verify(userRepo).save(u);
    }

    @Test
    void findAllShouldDelegateToRepo() {
        List<User> list = List.of(new User("A","a@a"), new User("B","b@b"));
        when(userRepo.findAll()).thenReturn(list);

        List<User> out = svc.findAll();

        assertThat(out).hasSize(2).containsExactlyElementsOf(list);
    }

    @Test
    void deleteShouldFirstDeleteBorrowRecordsThenUser() {
        long id = 123L;
        when(userRepo.existsById(id)).thenReturn(true);

        svc.delete(id);

        InOrder ord = inOrder(borrowRepo, userRepo);
        ord.verify(borrowRepo).deleteByUserId(id);
        ord.verify(userRepo).deleteById(id);
    }

    @Test
    void deleteNonexistentShouldThrow() {
        when(userRepo.existsById(5L)).thenReturn(false);

        assertThatThrownBy(() -> svc.delete(5L))
          .isInstanceOf(NoSuchElementException.class)
          .hasMessageContaining("not found");
    }
}
