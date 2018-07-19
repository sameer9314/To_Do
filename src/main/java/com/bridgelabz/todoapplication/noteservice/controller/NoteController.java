package com.bridgelabz.todoapplication.noteservice.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.todoapplication.noteservice.model.Notes;
import com.bridgelabz.todoapplication.noteservice.service.NoteServiceImpl;
import com.bridgelabz.todoapplication.userservice.model.Response;
import com.bridgelabz.todoapplication.userservice.service.UserServiceImpl;
import com.google.common.base.Optional;

import io.swagger.annotations.ApiOperation;

@RestController
public class NoteController {

	@Autowired
	NoteServiceImpl noteService;

	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	final String REQ_IN = "REQ_IN";
	final String RES_OUT = "RES_OUT";

	/**
	 * This method is written to create Notes for the logged in User.
	 * 
	 * @param note
	 * @param token
	 */
	@ApiOperation(value = "Create New Note")
	@RequestMapping(value = "/createnote", method = RequestMethod.POST)
	public ResponseEntity<Response> createNote(@RequestBody Notes note, @RequestParam String token) {
		logger.info(REQ_IN + " Note Creation Starts");
		Response response = new Response();
		try{
			noteService.createNote(note, token);
			
		}catch(Exception e) {
			response.setMessage(e.getMessage()+"");
			response.setStatus(-1);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		
		response.setMessage("Note Created");
		response.setStatus(200);
		logger.info(RES_OUT + " Notes Creation Ends");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * This method is written to get all the notes present in the database And
	 * return back to the client side.
	 * 
	 * @return List<Notes>
	 */
	@ApiOperation(value = "Get All Notes")
	@RequestMapping(value = "/getallnotes", method = RequestMethod.GET)
	public List<Notes> getAllNotes() {
		return noteService.getAllNotes();
	}

	/**
	 * This method is written to get all notes of a particular User 
	 * from the notes list which is not Archived  And return back
	 * to client side.
	 * 
	 * @param token
	 * @return
	 */
	@ApiOperation(value = "Get Notes")
	@RequestMapping(value = "/getnotes", method = RequestMethod.POST)
	public List<Notes> getNotes(@RequestParam String token) {
		return noteService.getNotes(token);
	}
	
	/**
	 * Method is written to get the Archived Notes.
	 * @param token
	 * @return
	 */
	@ApiOperation(value = "Get Archive Notes")
	@RequestMapping(value = "/archivenotes", method = RequestMethod.POST)
	public List<Notes> getArchiveNotes(@RequestParam String token){
		return noteService.getArchiveNotes(token);
	}
	
//	@ApiOperation(value = "Get Labelled Notes")
//	@RequestMapping(value = "/labellednoted", method = RequestMethod.POST)
//	public List<Notes> getlabelledNotes(@RequestParam String token){
//		return noteService.getlabelledNotes(token);
//	}

	/**
	 * This method is written to return particular note of the particular User And
	 * return back to client side.
	 * 
	 * @param token
	 * @param title
	 * @return
	 */
	@ApiOperation(value = "Get Particular Note")
	@RequestMapping(value = "/getparticularnote", method = RequestMethod.POST)
	public ResponseEntity<Response> getParticularNote(@RequestParam String token, @RequestParam Integer id) {
		logger.info(REQ_IN + " Updating Notes Starts");
		Response response = new Response();
		Notes note=null;
		try{note=noteService.getParticularNote(token, id);
		}catch(Exception e) {
			response.setMessage(e+"");
			response.setStatus(-1);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		response.setMessage(note.toString());
		response.setStatus(-1);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Method is written to update particular note of the logged in User.
	 * 
	 * @param token
	 * @param title
	 * @param description
	 * @return
	 */
	@ApiOperation(value = "Update Note")
	@RequestMapping(value = "/updatenote", method = RequestMethod.POST)
	public ResponseEntity<Response> updateNotes(@RequestParam String token,@RequestBody Notes note) {
		
		logger.info(REQ_IN + " Updating Notes Starts");
		Response response = new Response();
		Notes updatedNote;
		try {
			updatedNote=noteService.updateNotes(token,note);
		}catch(Exception e) {
			response.setMessage(e+"");
			response.setStatus(-1);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		response.setMessage("Updated Note Is :- "+ updatedNote);
		response.setStatus(200);
		logger.info(RES_OUT + " Updating Notes Ends");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * This method is written to delete the as per the User request according to the
	 * title of the Note.
	 * 
	 * @param token
	 * @param title
	 * @return
	 */
	@ApiOperation(value = "Remove Note")
	@RequestMapping(value = "/removenote", method = RequestMethod.POST)
	public ResponseEntity<Response> removeNote(@RequestParam String token, @RequestParam Integer id) {
		logger.info(REQ_IN + " Removing Notes Starts");
		Response response = new Response();
		try{
			noteService.removeNote(token, id);
		}catch(Exception e) {
			response.setMessage(e+"");
			response.setStatus(-1);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		response.setMessage("Note removed.");
		response.setStatus(200);
		logger.info(RES_OUT + " Removing Notes Ends");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
