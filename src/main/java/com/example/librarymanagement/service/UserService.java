package com.example.librarymanagement.service;

import com.example.librarymanagement.model.User;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.repository.BorrowRecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepo;
    private final BorrowRecordRepository borrowRepo;

    public UserService(UserRepository userRepo,
                       BorrowRecordRepository borrowRepo) {
        this.userRepo = userRepo;
        this.borrowRepo = borrowRepo;
    }

    public User create(User user) {
        return userRepo.save(user);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepo.existsById(id)) {
            throw new NoSuchElementException("User with id " + id + " not found");
        }
        borrowRepo.deleteByUserId(id);
        userRepo.deleteById(id);
    }
}
