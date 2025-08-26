package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.User;
import com.example.librarymanagement.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller slice test for UserController.
 * Expects endpoints:
 *  - POST /api/users
 *  - GET  /api/users
 *  - DELETE /api/users/{id}
 */
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createUser_shouldReturnUser() throws Exception {
        User toCreate = new User("Jane Doe", "jane@example.com");
        User saved = new User("Jane Doe", "jane@example.com");
        saved.setId(10L);

        when(userService.create(any(User.class))).thenReturn(saved);

        mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toCreate)))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(10))
            .andExpect(jsonPath("$.name").value("Jane Doe"))
            .andExpect(jsonPath("$.email").value("jane@example.com"));

        verify(userService, times(1)).create(any(User.class));
    }

    @Test
    void getAllUsers_shouldReturnList() throws Exception {
        User u1 = new User("A","a@a");
        u1.setId(1L);
        User u2 = new User("B","b@b");
        u2.setId(2L);
        when(userService.findAll()).thenReturn(List.of(u1, u2));

        mvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2));

        verify(userService).findAll();
    }

    @Test
    void deleteUser_whenExists_shouldReturnNoContent() throws Exception {
        // configure service to do nothing
        doNothing().when(userService).delete(5L);

        mvc.perform(delete("/api/users/5"))
            // Adjust expectation if your controller returns 200 OK instead of 204 No Content
            .andExpect(status().isNoContent());

        verify(userService).delete(5L);
    }

    @Test
    void deleteUser_whenMissing_shouldReturnNotFound() throws Exception {
        // simulate service throwing when not found
        doThrow(new NoSuchElementException("User not found")).when(userService).delete(99L);

        mvc.perform(delete("/api/users/99"))
            .andExpect(status().isNotFound());
        
        verify(userService).delete(99L);
    }
}
