package dev.oc.loginService.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.oc.loginService.model.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>{
	Optional<User> findByEmail(String email);
}
