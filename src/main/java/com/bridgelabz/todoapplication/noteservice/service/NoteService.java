package com.bridgelabz.todoapplication.noteservice.service;

import java.util.List;

import com.bridgelabz.todoapplication.noteservice.model.Notes;

public interface NoteService {
	public void createNote(Notes note, String token) throws Exception;
	public List<Notes> getAllNotes();
	public List<Notes> getNotes(String token);
	public Notes getParticularNote(String token, Integer id) throws Exception;
	public Notes updateNotes(String token,Notes note) throws Exception;
	public void removeNote(String token, Integer id) throws Exception;
}
