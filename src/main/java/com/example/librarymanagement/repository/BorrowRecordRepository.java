package com.example.librarymanagement.repository;

import com.example.librarymanagement.model.BorrowRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    
    Optional<BorrowRecord> findByBookIdAndReturnedAtIsNull(Long bookId);

    List<BorrowRecord> findByReturnedAtIsNull();
    
    void deleteByBookId(Long bookId);
    
    void deleteByUserId(Long userId);
}
