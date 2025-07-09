package com.example.librarymanagement.service;

import com.example.librarymanagement.model.User;                     // for User
import com.example.librarymanagement.repository.UserRepository;     // for userRepo
import com.example.librarymanagement.repository.BorrowRecordRepository; // for borrowRepo
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;                     // for List
import java.util.NoSuchElementException;   // for throwing when missing

@Service
public class UserService {
    private final UserRepository userRepo;
    private final BorrowRecordRepository borrowRepo;

    public UserService(UserRepository userRepo,
                       BorrowRecordRepository borrowRepo) {
        this.userRepo = userRepo;
        this.borrowRepo = borrowRepo;
    }

    public User create(User u) {
        return userRepo.save(u);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepo.existsById(id)) {
            throw new NoSuchElementException("User with id " + id + " not found");
        }
        // 1) remove dependent borrow records
        borrowRepo.deleteByUserId(id);
        // 2) delete the user
        userRepo.deleteById(id);
    }
}
