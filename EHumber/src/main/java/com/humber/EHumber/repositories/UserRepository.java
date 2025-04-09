package com.humber.EHumber.repositories;

import com.humber.EHumber.models.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<MyUser, Long> {
    public Optional<MyUser> findByUsername(String username);
}
