package com.example.librarymanagement.service.controller;

import com.example.librarymanagement.model.User;
import com.example.librarymanagement.service.UserService;
import org.springframework.http.ResponseEntity;
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
  
  @GetMapping("/{id}")
  public ResponseEntity<User> get(@PathVariable Long id) {
    return svc.findById(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }
  
  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id) {
    svc.delete(id);
  }
}
