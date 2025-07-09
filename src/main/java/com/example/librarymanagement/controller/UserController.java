package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.User;
import com.example.librarymanagement.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService svc;
  public UserController(UserService svc) { this.svc = svc; }

  @PostMapping
  public User create(@RequestBody User u) { return svc.create(u); }

  @GetMapping
  public List<User> all() { return svc.findAll(); }
  
  
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    svc.delete(id);
  }
}
