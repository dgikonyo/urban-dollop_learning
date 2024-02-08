package com.kcbgroup.learning.jugtours.repository;

import com.kcbgroup.learning.jugtours.models.Group;
import com.kcbgroup.learning.jugtours.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
