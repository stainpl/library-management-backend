package com.example.librarymanagement.service;

import com.example.librarymanagement.model.User;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.repository.BorrowRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import java.util.List;
import java.util.NoSuchElementException;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepo;
    @Mock BorrowRecordRepository borrowRepo;
    @InjectMocks UserService userService;

    @Test
    void createShouldSaveUser() {
        User u = new User("Name","e@mail");
        when(userRepo.save(u)).thenReturn(u);

        User out = userService.create(u);
        assertThat(out).isSameAs(u);
        verify(userRepo).save(u);
    }

    @Test
    void deleteNonExistentShouldThrow() {
        when(userRepo.existsById(5L)).thenReturn(false);
        assertThatThrownBy(() -> userService.delete(5L))
            .isInstanceOf(NoSuchElementException.class);
    }

    // add more tests...
}
