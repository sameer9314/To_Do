package com.bridgelabz.todoapplication.noteservice.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.todoapplication.noteservice.model.Notes;
import com.bridgelabz.todoapplication.noteservice.repository.NoteRepository;
import com.bridgelabz.todoapplication.tokenutility.TokenUtility;
import com.bridgelabz.todoapplication.userservice.service.UserServiceImpl;

@Service
public class NoteServiceImpl implements NoteService {

	@Autowired
	NoteRepository noteRepository;

	@Autowired
	TokenUtility tokenUtility;

	@Autowired
	Notes note;

	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	final String REQ_IN = "REQ_IN";
	final String RES_OUT = "RES_OUT";

	/**
	 * This method is written to create Notes for the logged in User.
	 * 
	 * @param note
	 * @throws Exception
	 * 
	 */
	
	@Override
	public void createNote(Notes note, String token) throws Exception {
		logger.info("note creation method starts");
		if (!note.getId().equals("") && !note.getDescription().equals("")
				&& !token.equals("")) {
			String userId = tokenUtility.parseJWT(token).getId();
			if (!userId.equals(null)) {
				List<Notes> notes = noteRepository.findByUserId(userId);
					Notes foundNote = notes.stream().filter(t -> t.getId()==note.getId())
							.findFirst().orElse(null);

					if(foundNote!=null) {
						logger.error(
								"Note Is Already Present With The Title " + note.getTitle() + " In The Logged In User");
						throw new Exception(
								"Note Is Already Present With The Title " + note.getTitle() + " In The Logged In User");
					}
						note.setCreationDate(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
						note.setUserId(tokenUtility.parseJWT(token).getId());
						note.setLastModified(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
						if(!note.getArchive().equals("")&&!note.getPinned().equals("")) {
							note.setPinned("");
						}
						noteRepository.save(note);
						logger.info("Note Created");
						return;
			}
			logger.error("Not valid User");
			throw new Exception("Not valid User");
		}

		logger.error("Field Is Null");
		throw new Exception("Field Cannot Be Null");
	}

	/**
	 * This method is written to return all the notes present in the database.
	 * 
	 * @return List<Notes>
	 */
	@Override
	public List<Notes> getAllNotes() {
		return noteRepository.findAll();
	}

	/**
	 * This method is written to return all notes (from the list
	 * which is not archived) of a particular User.
	 * 
	 * @param token
	 * @return List<Notes>
	 */
	@Override
	public List<Notes> getNotes(String token) {
		logger.info(REQ_IN+" Get Notes ");
		List<Notes> notes=noteRepository.findByUserId(tokenUtility.parseJWT(token).getId());
		List<Notes> listNotes=new ArrayList<>();
		
		// Notes which is not archived but pinned.
		for(Notes n:notes) {
			if(n.getArchive().equals("")) {
				if(!n.getPinned().equals("")) {
					logger.info(n.toString());
				listNotes.add(n);
				}
			}
		}
		
		// Notes which is not archived and not pinned.
		for(Notes n:notes) {
			if(n.getArchive().equals("")) {
				if(n.getPinned().equals("")) {
					logger.info(n.toString());
				listNotes.add(n);
				}
			}
		}
		
		logger.info(RES_OUT+" Get Notes Ends");
		return listNotes;
	}
	
	public List<Notes> getArchiveNotes(String token){
		logger.info(REQ_IN+" Get Archive Notes ");
		List<Notes> notes=noteRepository.findByUserId(tokenUtility.parseJWT(token).getId());
		List<Notes> listNotes=new ArrayList<>();
		for(Notes n:notes) {
			if(!n.getArchive().equals("")) {
				listNotes.add(n);
			}
		}
		logger.info(RES_OUT+" Get Archive Notes Ends");
		return listNotes;
	}
	
	/*public List<Notes> getlabelledNotes(String token,String label){
		List<Notes> notes=noteRepository.findByUserId(tokenUtility.parseJWT(token).getId());
		List<Notes> labelledNotes=null;
		for(Notes n:notes) {
			if(!n.getLabel().equals(label)) {
				listNotes.add(n);
			}
		}
	}*/
	
	/**
	 * This method is written to return particular note of the particular User.
	 * 
	 * @param token
	 * @param title
	 * @return Notes
	 * @throws Exception 
	 */
	@Override
	public Notes getParticularNote(String token, Integer id) throws Exception {
		List<Notes> notes = noteRepository.findByUserId(tokenUtility.parseJWT(token).getId());

		Notes foundNote = notes.stream().filter(t -> t.getId()==id).findFirst().orElse(null);

		if(foundNote==null) {
			logger.error(
					"Note Is Not Present With The Id " + note.getId() + " In The Logged In User");
			throw new Exception(
					"Note Is Not Present With The Id " + note.getId() + " In The Logged In User");
		}
		return foundNote;
	}

	/**
	 * Method is written to update particular note of the logged in User.
	 * 
	 * @param token
	 * @param title
	 * @param description
	 * @return
	 * @throws Exception 
	 */
	@Override
	public Notes updateNotes(String token,Notes updatedNote) throws Exception {
		String userId = tokenUtility.parseJWT(token).getId();
		List<Notes> notes = noteRepository.findByUserId(userId);

		Notes foundNote = notes.stream().filter(t -> t.getId()==updatedNote.getId()).findFirst().orElse(null);

		if(foundNote==null) {
			logger.error(
					"Note Is Not Present With The Id " + note.getId() + " In The Logged In User");
			throw new Exception(
					"Note Is Not Present With The Id " + note.getId() + " In The Logged In User");
		}
		
		note.setId(foundNote.getId());
		note.setTitle(updatedNote.getTitle());
		note.setDescription(updatedNote.getDescription());
		note.setCreationDate(foundNote.getCreationDate());
		note.setUserId(userId);
		note.setLastModified(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
		note.setArchive(updatedNote.getArchive());
		note.setPinned(updatedNote.getPinned());
		if(!updatedNote.getArchive().equals("")) {
			note.setPinned("");
		}
		noteRepository.save(note);
		return note;
	}

	/**
	 * Method is written to remove particular Note of the User.
	 * 
	 * @param token
	 * @param title
	 * @throws Exception 
	 */
	@Override
	public void removeNote(String token, Integer id) throws Exception {
		List<Notes> notes = noteRepository.findByUserId(tokenUtility.parseJWT(token).getId());
		Notes foundNote=notes.stream().filter(t -> t.getId()==id).findFirst().orElse(null);
		if(foundNote==null) {
			logger.error(
					"Note Is Not Present With The Id " + note.getId() + " In The Logged In User");
			throw new Exception(
					"Note Is Not Present With The Id " + note.getId() + " In The Logged In User");
		}
		logger.info("note deleted");
		noteRepository.delete(foundNote);
		
	}

}
