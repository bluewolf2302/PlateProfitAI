package com.plateprofit.ai.repository;

import com.plateprofit.ai.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> { }
