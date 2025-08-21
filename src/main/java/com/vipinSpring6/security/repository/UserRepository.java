package com.vipinSpring6.security.repository;

import com.vipinSpring6.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {// entity wala user

    User findByUserName(String userName);




}
