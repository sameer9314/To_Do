package com.bridgelabz.todoapplication.noteservice.controller;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.todoapplication.noteservice.model.Label;
import com.bridgelabz.todoapplication.noteservice.model.NoteDto;
import com.bridgelabz.todoapplication.noteservice.model.Notes;
import com.bridgelabz.todoapplication.noteservice.service.NoteServiceImpl;
import com.bridgelabz.todoapplication.userservice.model.Response;
import com.bridgelabz.todoapplication.userservice.service.UserServiceImpl;
import com.google.common.base.Preconditions;

import io.swagger.annotations.ApiOperation;

/**
 * Purpose : Controller class to handle Note Service.
 * 	
 * @author   Sameer Saurabh
 * @version  1.0
 * @Since    24/07/2018
 */
@RestController
@RequestMapping(value="/note")
public class NoteController {

	@Autowired
	NoteServiceImpl noteService;

	@Autowired
	Response response;
	
	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	final String REQ_IN = "REQ_IN";
	final String RES_OUT = "RES_OUT";

	/**
	 * This method is written to create Notes for the logged in User.
	 * 
	 * @param note
	 * @param token
	 * @throws Exception 
	 */
	@ApiOperation(value = "Create New Note")
	@PostMapping(value = "/createnote")
	public ResponseEntity<Response> createNote(@RequestBody NoteDto note,HttpServletRequest req ) throws Exception {
		logger.info(REQ_IN + " Note Creation Starts");
		
		String userId=req.getAttribute("userId").toString();
		
		Preconditions.checkNotNull(userId, "Token Is Null");
		Preconditions.checkNotNull(note, "Note Cannot Be Null");
		
		String noteId=noteService.createNote(note, userId,"");
		
		response.setMessage("Note Created with id : "+noteId);
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
	@GetMapping(value = "/getallnotes")
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
	@PostMapping(value = "/getnotes")
	public ResponseEntity<Response> getNotes(HttpServletRequest req) {
		
		response.setMessage(noteService.getNotes(req.getAttribute("userId").toString()).toString());
		response.setStatus(200);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * Method is written to get the Archived Notes.
	 * @param token
	 * @return
	 */
	@ApiOperation(value = "Get Archive Notes")
	@PostMapping(value = "/archivenotes")
	public List<Notes> getArchiveNotes(HttpServletRequest req){
		return noteService.getArchiveNotes(req.getAttribute("userId").toString());
	}
	
	/**
	 * This method is written to return particular note of the particular User And
	 * return back to client side.
	 * 
	 * @param token
	 * @param title
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "Get Particular Note")
	@PostMapping(value = "/getparticularnote")
	public ResponseEntity<Response> getParticularNote(HttpServletRequest req, @RequestParam String id) throws Exception {
		logger.info(REQ_IN + " Updating Notes Starts");
		
		Notes note=noteService.getParticularNote(req.getAttribute("userId").toString(), id);
		
		response.setMessage(note.toString());
		response.setStatus(200);
		
		logger.info(RES_OUT + " Updating Notes Ends");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Method is written to get all the label created by the User.
	 * 
	 * @param token
	 * @return
	 */
	@ApiOperation(value = "Prints All Label")
	@PostMapping(value = "/getallabel")
	public ResponseEntity<Response> getLabel(HttpServletRequest req) {
		logger.info(REQ_IN + " Get Label Starts");
		
		List<String> labelName=noteService.getLabel(req.getAttribute("userId").toString());
		
		response.setMessage(labelName.toString());
		response.setStatus(200);
		
		logger.info(RES_OUT + " Get Label Ends");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Method is written to add Label on existing Note.
	 * @param token
	 * @param newLabel
	 * @param noteId
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "Add Label To Note")
	@PostMapping(value = "/addlebel")
	public ResponseEntity<Response> addLabelInNote(HttpServletRequest req,@RequestBody Label newLabel,@RequestParam String noteId) throws Exception {
		logger.info(REQ_IN + " Add Label Starts");
		
		noteService.addLabelInNote(req.getAttribute("userId").toString(),newLabel,noteId);
	
		response.setMessage("Label Added");
		response.setStatus(1);
		
		logger.info(RES_OUT + " Add Label Ends");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * To create Label for the particular User.
	 * 
	 * @param token
	 * @param newLabel
	 * @return
	 * @throws Exception 
	 */
	@ApiOperation(value = "Create Label")
	@PostMapping(value = "/createlabel")
	public ResponseEntity<Response> createLabel(HttpServletRequest req,@RequestBody Label newLabel) throws Exception {
		logger.info(REQ_IN + " Create Label Starts ");
	
		noteService.createLabel(req.getAttribute("userId").toString(),newLabel);
		
		response.setMessage("Label Created");
		response.setStatus(1);
		
		logger.info(RES_OUT + " Create Label Ends");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Method is written to create Note with existing Label. 
	 * 
	 * @param note
	 * @param token
	 * @param labelName
	 * @return ResponseEntity<Response>
	 * @throws Exception
	 */
	@ApiOperation(value = "Create Note With Existing Label")
	@PostMapping(value = "/createnotewithlabel")
	public ResponseEntity<Response> createNoteWithLabel(@RequestBody NoteDto note,HttpServletRequest req,@RequestParam String labelName) throws Exception {
		logger.info(REQ_IN + " Create Note With Existing Label Starts ");
		
		noteService.createNote(note,req.getAttribute("userId").toString(),labelName);
		
		response.setMessage("Note Created With Label");
		response.setStatus(1);
		
		logger.info(RES_OUT + " Create Note With Existing Label Ends");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	
	/**
	 * Method Is Written To Get All the Notes According To Its Label.
	 * 
	 * @param token
	 * @param labelName
	 * @return ResponseEntity<Response>
	 */
	@ApiOperation(value = "Prints Notes With Label")
	@PostMapping(value = "/getnoteswithlabel")
	public ResponseEntity<Response> getNoteWithLabel(HttpServletRequest req,@RequestParam String labelName) {
		logger.info(REQ_IN + " Get Notes With Label Starts");
		List<Notes> notes=null;
		
		notes=noteService.getNoteWithLabel(req.getAttribute("userId").toString(),labelName);
		
		response.setMessage(notes.toString());
		response.setStatus(1);
		
		logger.info(RES_OUT + " Get Notes With Label Ends");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * To remove the Label from the Label List.
	 * 
	 * @param token
	 * @param labelName
	 * @return
	 */
	@ApiOperation(value = "Remove Label ")
	@PostMapping(value = "/removelabel")
	public ResponseEntity<Response> removeLabel(HttpServletRequest req,@RequestParam String labelName) {
		logger.info(REQ_IN + " Remove Label  Starts");
		
		noteService.removeLabel(req.getAttribute("userId").toString(),labelName);
		
		response.setMessage(labelName+" removed");
		response.setStatus(1);
		
		logger.info(RES_OUT + " Remove Label  Ends");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * To remove the label from Note.
	 * 
	 * @param token
	 * @param labelName
	 * @param noteId
	 * @return
	 */
	@ApiOperation(value = "Remove Note Label ")
	@PostMapping(value = "/removeNoteLabel")
	public ResponseEntity<Response> removeNoteLabel(HttpServletRequest req,
			@RequestParam String labelName,@RequestParam String noteId) {
		logger.info(REQ_IN + " Remove Note Label  Starts");
		
		noteService.removeNoteLabel(req.getAttribute("userId").toString(),labelName,noteId);
		
		response.setMessage(labelName+" removed from note");
		response.setStatus(1);
		
		logger.info(RES_OUT + " Remove Note Label  Ends");
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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
	@ApiOperation(value = "Update Note")
	@PostMapping(value = "/updatenote")
	public ResponseEntity<Response> updateNotes(HttpServletRequest req,@RequestBody NoteDto note,
			@RequestParam String noteId) throws Exception {
		
		logger.info(REQ_IN + " Updating Notes Starts");
		
		Notes updatedNote=noteService.updateNotes(req.getAttribute("userId").toString(),note,noteId);
		
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
	 * @throws Exception 
	 */
	@ApiOperation(value = "Remove Note")
	@PostMapping(value = "/removenote/{id}")
	public ResponseEntity<Response> removeNote(HttpServletRequest req, @PathVariable("id") String id) throws Exception {
		logger.info(REQ_IN + " Removing Notes Starts");
		
		noteService.removeNote(req.getAttribute("userId").toString(), id);
		
		response.setMessage("Note removed.");
		response.setStatus(200);
		
		logger.info(RES_OUT + " Removing Notes Ends");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	
	/**
	 * Method is written to set the reminder to the Notes. 
	 * 
	 * @param token
	 * @param noteId
	 * @param reminderTime
	 * @return
	 * @throws ParseException 
	 */
	@ApiOperation(value = "Set Reminder")
	@PostMapping(value = "/setreminder")
	public ResponseEntity<Response> doSetReminder(HttpServletRequest req, @RequestParam String noteId,@RequestParam String reminderTime) throws ParseException {
		logger.info(REQ_IN + " Set Reminder Starts");
		
		noteService.doSetReminder(req.getAttribute("userId").toString(),noteId,reminderTime);
		
		response.setMessage("Reminder Set");
		response.setStatus(200);
		
		logger.info(RES_OUT + " Set Reminder Ends");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * To get all notes which is trashed. 
	 * 
	 * @param token
	 * @return
	 */
	@ApiOperation(value="Get Trashed Notes")
	@PostMapping(value="/gettrashednotes")
	public ResponseEntity<Response> getTrashedNotes(HttpServletRequest req){
		logger.info(REQ_IN + "Get Trashed Notes Starts");
		
		response.setMessage(noteService.getTrashedNotes(req.getAttribute("userId").toString()).toString());
		response.setStatus(200);
		
		logger.info(RES_OUT + "Get Trashed Notes Ends");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * To set the pinned status to true or false for the particular noteId if note archived status is false.
	 * 
	 * @param token
	 * @param noteId
	 * @return
	 */
	@ApiOperation(value="Pinned Or Unpinned Notes")
	@PostMapping(value="/pinnedorunpinnednotes")
	public ResponseEntity<Response>  pinnedUnpinned(HttpServletRequest req,@RequestParam String noteId) {
		logger.info(REQ_IN + "Pinned Or Unpinned Notes Starts");
		
		response.setMessage(noteService.pinnedUnpinned(req.getAttribute("userId").toString(),noteId));
		response.setStatus(200);
		
		logger.info(RES_OUT + "Pinned Or Unpinned Notes Ends");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * To set the pinned status to true or false for the particular noteId if note archived status is false.
	 * 
	 * @param token
	 * @param noteId
	 * @return
	 */
	@ApiOperation(value="Pinned Notes")
	@PostMapping(value="/dopinnednotes")
	public ResponseEntity<Response>  archivedOrRemoveArchived(HttpServletRequest req,@RequestParam String noteId) {
		logger.info(REQ_IN + "Archived Or Removed Archived Notes Starts");
		
		response.setMessage(noteService.archivedOrRemoveArchived(req.getAttribute("userId").toString(),noteId));
		response.setStatus(200);
		
		logger.info(RES_OUT + "Archived Or Removed Archived Notes Ends");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	/**
	 * To set the pinned status to true or false for the particular noteId if note archived status is false.
	 * 
	 * @param token
	 * @param noteId
	 * @return
	 */
	@ApiOperation(value="View Trashed Notes")
	@PostMapping(value="/viewtrashednotes")
	public ResponseEntity<Response>  viewTrash(HttpServletRequest req,@RequestParam String noteId) {
		logger.info(REQ_IN + "View Trash Notes Starts");
		
		Preconditions.checkNotNull(req.getAttribute("userId").toString(), "Token Is Null");
		Preconditions.checkNotNull(noteId, "Note Id Is Null");
		
		response.setMessage(noteService.viewTrash(req.getAttribute("userId").toString(),noteId).toString());
		response.setStatus(200);
		
		logger.info(RES_OUT + "View Trash Notes Ends");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
