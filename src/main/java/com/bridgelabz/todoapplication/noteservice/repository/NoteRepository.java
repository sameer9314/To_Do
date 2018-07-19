package com.bridgelabz.todoapplication.noteservice.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.todoapplication.noteservice.model.Notes;

@Repository
public interface NoteRepository extends MongoRepository<Notes, String>{
	public List<Notes> findByUserId(String userId);
}
