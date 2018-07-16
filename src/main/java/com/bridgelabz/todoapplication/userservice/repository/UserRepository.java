package com.bridgelabz.todoapplication.userservice.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.todoapplication.userservice.model.User;

/**
 * Purpose : Interface to extend MongoRepository.	
 * @author   Sameer Saurabh
 * @version  1.0
 * @Since    11/07/2018
 */
@Repository
public interface UserRepository extends MongoRepository<User, String>{
	public Optional<User> findByEmail(String email);
}
