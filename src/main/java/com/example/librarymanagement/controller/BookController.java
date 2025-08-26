package com.example.librarymanagement.controller;

import com.example.librarymanagement.model.Book;
import com.example.librarymanagement.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
  private final BookService svc;
  public BookController(BookService svc) { this.svc = svc; }

  @PostMapping
  public Book add(@RequestBody Book b) {
    return svc.add(b);
  }

  @GetMapping
  public List<Book> all() {
    return svc.findAll();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Book> get(@PathVariable Long id) {
    return svc.findById(id)
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    svc.delete(id);
  }
}
