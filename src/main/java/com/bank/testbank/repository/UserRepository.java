package com.bank.testbank.repository;

import com.bank.testbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByContactNo(String contactNo);
}
