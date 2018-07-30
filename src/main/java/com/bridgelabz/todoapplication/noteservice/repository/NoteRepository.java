package com.bridgelabz.todoapplication.noteservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.todoapplication.noteservice.model.Notes;

/**
 * Purpose : NoteRepository interface which extends MongoRepository to store 
 *           the Label details into MONGO Database.
 * @author   Sameer Saurabh
 * @version  1.0
 * @Since    24/07/2018
 */
@Repository
public interface NoteRepository extends MongoRepository<Notes, String>{
	public List<Notes> findByUserId(String userId);
}
