package com.bridgelabz.todoapplication.noteservice.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.todoapplication.Utility.Messages;
import com.bridgelabz.todoapplication.noteservice.model.Label;
import com.bridgelabz.todoapplication.noteservice.model.NoteDto;
import com.bridgelabz.todoapplication.noteservice.model.Notes;
import com.bridgelabz.todoapplication.noteservice.repository.LabelElasticRepository;
import com.bridgelabz.todoapplication.noteservice.repository.LabelRepository;
import com.bridgelabz.todoapplication.noteservice.repository.NoteElasticRepository;
import com.bridgelabz.todoapplication.noteservice.repository.NoteRepository;
import com.bridgelabz.todoapplication.sequence.dao.SequenceDao;
import com.bridgelabz.todoapplication.userservice.service.UserServiceImpl;
import com.google.common.base.Preconditions;

/**
 * Purpose : To provide the implementation for the NoteService.
 * 
 * @author Sameer Saurabh
 * @version 1.0
 * @Since 24/07/2018
 */
@Service
public class NoteServiceImpl implements NoteService {
	@Autowired
	NoteRepository noteRepository;

	@Autowired
	LabelRepository labelRepository;

	/*@Autowired
	Notes note;*/

	@Autowired
	ModelMapper mapper;

	private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private SequenceDao sequenceDao;
	
	@Autowired
	Messages messages;
	
	@Autowired
	NoteElasticRepository noteElasricRepository;
	
	@Autowired
	LabelElasticRepository labelElasricRepository;
	
	private static final String HOSTING_SEQ_KEY = "hosting";
	
	/**
	 * Method is written to create note for the logged in User.
	 * 
	 * @param newNote
	 * @param token
	 * @return List<Notes>
	 */
	@Override
	public String createNote(NoteDto newNote, String userId, String labelName) throws Exception {
		Preconditions.checkNotNull(newNote.getDescription(),messages.get("161"));
		Preconditions.checkNotNull(newNote.getTitle(),messages.get("162"));
		Preconditions.checkNotNull(labelName,messages.get("163"));
			if ((!newNote.getDescription().equals("") || !newNote.getTitle().equals("")))
		{
			Notes note = mapper.map(newNote, Notes.class);
			note.setId(sequenceDao.getNextSequenceId(HOSTING_SEQ_KEY));
			note.setCreationDate(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
			note.setUserId(userId);
			note.setLastModified(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
			note.setTrash(false);
			
			// If note is created using label.
			if (!labelName.equals("")) {
				Optional<Label> label = Preconditions.checkNotNull(labelRepository.findLabelByLabelName(labelName),
						messages.get("164")+ labelName);
				newNote.getLabel().add(label.get());
			}
			
			List<Label> labels = note.getLabel();
			for (Label label : labels) {
				logger.info(label.toString());
				label.setId(sequenceDao.getNextSequenceId(HOSTING_SEQ_KEY));
				label.setUserId(userId);
				Optional<Label> foundLabel = labelRepository.findLabelByLabelName(label.getLabelName());
				if (foundLabel.isPresent() == false && !label.getLabelName().equals("")) {
					labelRepository.save(label);
					labelElasricRepository.save(label);
				}
			}
			// If archive is true then pinned must be false.  
			if (note.isArchive()==true) {
				note.setPinned(false);
			}
			logger.info(messages.get("165"));
			noteRepository.save(note);
			noteElasricRepository.save(note);
			logger.info(messages.get("166")+note.getId());
			return note.getId();
		}
		logger.error(messages.get("121"));
		throw new Exception(messages.get("121"));
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
	 * This method is written to return all notes (from the list which is not
	 * archived) of a particular User.
	 * 
	 * @param token
	 * @return List<Notes>
	 */
	@Override
	public List<Notes> getNotes(String userId) {
		Preconditions.checkNotNull(userId,messages.get("168"));
		
		List<Notes> notes = Preconditions.checkNotNull(
				noteRepository.findByUserId(userId),messages.get("167")
				);

		List<Notes> listNotes = new ArrayList<>();

		// Notes which is not archived but pinned.
		for (Notes n : notes) {
			if (n.isArchive()==false) {
				if (n.isPinned()==true) {
					logger.info(n.toString());
					listNotes.add(n);
				}
			}
		}

		// Notes which is not archived and not pinned.
		for (Notes n : notes) {
			if (n.isArchive()==false) {
				if (n.isPinned()==false) {
					logger.info(n.toString());
					listNotes.add(n);
				}
			}
		}

		return listNotes;
	}

	/**
	 * Method is written to return archived notes of a particular user.
	 * 
	 * @param token
	 * @return List<Notes>
	 */
	@Override
	public List<Notes> getArchiveNotes(String userId) {
		Preconditions.checkNotNull(userId,messages.get("168"));
		List<Notes> notes = noteRepository.findByUserId(userId);
		List<Notes> listNotes = new ArrayList<>();
		for (Notes n : notes) {
			if (n.isArchive()==true) {
				listNotes.add(n);
			}
		}
		return listNotes;
	}

	/**
	 * Method is written to get all the label created by the User.
	 * 
	 * @param token
	 * @return
	 */
	public List<String> getLabel(String userId) {
		Preconditions.checkNotNull(userId,messages.get("168"));
		List<Label> labels = labelRepository.findByUserId(userId);
		List<String> labelName = new ArrayList<>();
		for (Label label : labels) {
			labelName.add(label.getLabelName());
		}
		return labelName;
	}

	/**
	 * Method is written to create Label for the particular user.
	 * 
	 * @param token
	 * @param label
	 * @throws Exception
	 */
	public void createLabel(String userId, Label label) throws Exception {
		Preconditions.checkNotNull(userId,messages.get("168"));
		Preconditions.checkNotNull(label.getLabelName(),messages.get("163"));
		
		List<Label> foundLabels = labelRepository.findByUserId(userId);
		for (Label foundLabel : foundLabels) {
			if (foundLabel.getLabelName().equals(label.getLabelName())) {
				throw new Exception(messages.get("170") + label.getLabelName());
			}
		}
		label.setId(sequenceDao.getNextSequenceId(HOSTING_SEQ_KEY));
		label.setUserId(userId);
		labelRepository.save(label);
	}

	/**
	 * Method is written to add Label on an existing Note.
	 * 
	 * @param token
	 * @param newLabel
	 * @param noteId
	 * @throws Exception
	 */
	public void addLabelInNote(String userId, Label newLabel, String noteId) throws Exception {
		Preconditions.checkNotNull(userId,messages.get("168"));
		Preconditions.checkNotNull(newLabel.getLabelName(),messages.get("163"));
		Preconditions.checkNotNull(noteId,messages.get("169"));
		
		List<Notes> notes = Preconditions.checkNotNull(noteRepository.findByUserId(userId),
				messages.get("167") + userId);
		Notes foundNote = Preconditions.checkNotNull(
				notes.stream().filter(t -> t.getId().equals(noteId)).findFirst().orElse(null),
				"No Note Found With id " + noteId);
		
		// Checking label is already present on note .
		List<Label> foundLabels = foundNote.getLabel();
		for (Label foundLabel : foundLabels) {
			if (foundLabel.getLabelName().equals(newLabel.getLabelName())) {
				throw new Exception(
						messages.get("170") + newLabel.getLabelName());
			}
		}
		
		newLabel.setId(sequenceDao.getNextSequenceId(HOSTING_SEQ_KEY));
		newLabel.setUserId(userId);
		foundNote.getLabel().add(newLabel);
		noteRepository.save(foundNote);

		List<Label> labels = labelRepository.findByUserId(userId);
		Boolean labelfoundStatus = true;
		for (Label label : labels) {
			if (label.getLabelName().equals(newLabel.getLabelName())) {
				labelfoundStatus = true;
			}
		}
		if (labelfoundStatus == false) {
			labelRepository.save(newLabel);
		}
	}

	/**
	 * Method Is Written To Get All the Notes According To Its Label.
	 * 
	 * @param token
	 * @param labelName
	 * @return List<Notes>
	 */
	public List<Notes> getNoteWithLabel(String userId, String labelName) {
		Preconditions.checkNotNull(userId,messages.get("168"));
		Preconditions.checkNotNull(labelName,messages.get("163"));
		List<Notes> notes = noteRepository.findByUserId(userId);
		Preconditions.checkNotNull(notes,messages.get("167"));
		List<Notes> foundNotes = new ArrayList<>();

		for (Notes note : notes) {
			List<Label> labels = note.getLabel();
			for (Label label : labels) {
				if (label.getLabelName().equals(labelName)) {
					foundNotes.add(note);
				}
			}
		}
		return foundNotes;
	}

	/**
	 * Method is written to remove the label and remove the label from every notes
	 * of the user.
	 * 
	 * @param token
	 * @param labelName
	 */
	public void removeLabel(String userId, String labelName) {
		Preconditions.checkNotNull(userId,messages.get("168"));
		Preconditions.checkNotNull(labelName,messages.get("163"));
		
		List<Notes> notes = Preconditions.checkNotNull(
				noteRepository.findByUserId(userId), messages.get("167"));
		for (Notes note : notes) {
			List<Label> labels = note.getLabel();

			for (Label label : labels) {
				if (label.getLabelName().equals(labelName)) {
					labelRepository.deleteById(label.getId());
					labels.remove(label);
				}
				break;
			}
			note.setLabel(labels);
			noteRepository.save(note);
		}
	}

	/**
	 * To remove the label from the particular Note.
	 * 
	 * @param token
	 * @param labelName
	 * @param noteId
	 */
	public void removeNoteLabel(String userId, String labelName, String noteId) {
		Preconditions.checkNotNull(userId,messages.get("168"));
		Preconditions.checkNotNull(labelName,messages.get("163"));
		Preconditions.checkNotNull(noteId,messages.get("169"));
		
		List<Notes> notes = Preconditions.checkNotNull(
				noteRepository.findByUserId(userId), messages.get("167"));

		Notes foundNote = Preconditions.checkNotNull(
				notes.stream().filter(t -> t.getId().equals(noteId)).findFirst().orElse(null),
				messages.get("167") + noteId + " In The Logged In User");

		List<Label> labels = foundNote.getLabel();
		for (Label label : labels) {
			if (label.getLabelName().equals(labelName)) {
				labels.remove(label);
			}
			break;
		}
		foundNote.setLabel(labels);
		noteRepository.save(foundNote);
	}

	/**
	 * This method is written to return particular note of the particular User.
	 * 
	 * @param token
	 * @param id
	 * @return Notes
	 * @throws Exception
	 */
	@Override
	public Notes getParticularNote(String userId, String id) throws Exception {
		Preconditions.checkNotNull(userId,messages.get("168"));
		Preconditions.checkNotNull(id,messages.get("169"));
		List<Notes> notes = Preconditions
				.checkNotNull(noteRepository.findByUserId(userId), messages.get("167") + userId);

		Notes foundNote = Preconditions.checkNotNull(
				notes.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null),
				messages.get("167") + id );
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
	public Notes updateNotes(String userId, NoteDto updatedNote,String noteId) throws Exception {
		Preconditions.checkNotNull(userId,messages.get("168"));
		Preconditions.checkNotNull(noteId,messages.get("169"));
		
		Preconditions.checkNotNull(updatedNote.getDescription(),messages.get("161"));
		Preconditions.checkNotNull(updatedNote.getTitle(),messages.get("162"));
		
		List<Notes> notes = Preconditions.checkNotNull(noteRepository.findByUserId(userId),
				messages.get("167")  + userId);

		Notes foundNote = Preconditions.checkNotNull(
				notes.stream().filter(t -> t.getId().equals(noteId)).findFirst().orElse(null),
				messages.get("167") + noteId );
		
		Notes note=mapper.map(updatedNote, Notes.class);
		note.setId(foundNote.getId());
		note.setCreationDate(foundNote.getCreationDate());
		note.setUserId(foundNote.getUserId());
		note.setLastModified(new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date()));
		note.setTrash(foundNote.isTrash());
		if (note.isArchive()==true) {
			note.setPinned(false);
		}
		
		List<Label> foundNoteLabels=foundNote.getLabel();
		
		List<Label> labels = note.getLabel();
		
		// Only new labels from the updated note which is already present on the existing note will be added.
		for(Label label:labels) {
		Label foundNoteLabel=	foundNoteLabels.stream().filter(t -> t.getLabelName().equals(label.getLabelName())).findFirst().orElse(null);
		if(foundNoteLabel==null) {	
			foundNoteLabels.add(label);
		}
		}
		
		// New label which is already not present on the label will be saved.
		for (Label label : labels) {
			logger.info(label.toString());
			label.setId(sequenceDao.getNextSequenceId(HOSTING_SEQ_KEY));
			label.setUserId(userId);
			Optional<Label> foundLabel = labelRepository.findLabelByLabelName(label.getLabelName());
			if (foundLabel.isPresent() == false && !label.getLabelName().equals("")) {
				labelRepository.save(label);
			}
		}
		note.setLabel(foundNoteLabels);
		noteRepository.save(note);
		return foundNote;
	}

	/**
	 * Method is written to remove particular Note of the User.
	 * 
	 * @param token
	 * @param title
	 * @throws Exception
	 */
	@Override
	public void removeNote(String userId, String id) throws Exception {
		Preconditions.checkNotNull(userId,messages.get("168"));
		Preconditions.checkNotNull(id,messages.get("169"));
		
		List<Notes> notes = Preconditions
				.checkNotNull(noteRepository.findByUserId(userId), messages.get("167"));

		Notes foundNote = Preconditions.checkNotNull(
				notes.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null),
				messages.get("167") + id);
		
		if(foundNote.isTrash()==false) {
			foundNote.setTrash(true);
			logger.info(" Trashed Note : " + foundNote.toString());
			noteRepository.save(foundNote);
			return;
		}
		logger.info(" Deleted Note : " + foundNote.toString());
		noteRepository.delete(foundNote);
	}

	/**
	 * Method is written to set the reminder to the Notes.
	 * 
	 * @param token
	 * @param noteId
	 * @param reminderTime
	 * @throws ParseException
	 */
	public void doSetReminder(String userId, String noteId, String reminderTime) throws ParseException {
		Preconditions.checkNotNull(userId,messages.get("168"));
		Preconditions.checkNotNull(noteId,messages.get("169"));
		Preconditions.checkNotNull(reminderTime,messages.get("171"));
		
		List<Notes> notes = Preconditions
				.checkNotNull(noteRepository.findByUserId(userId), messages.get("167"));
		for (Notes note : notes) {
			if (note.getId().equals(noteId)) {
				Date reminder = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(reminderTime);
				long timeDifference = reminder.getTime() - new Date().getTime();
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						logger.info( messages.get("172")+ note.toString());
					}
				}, timeDifference);

			}
		}
	}
	
	/**
	 * To get all notes which is trashed. 
	 * 
	 * @param token
	 * @return
	 */
	public List<Notes> getTrashedNotes(String userId){
		Preconditions.checkNotNull(userId,messages.get("168"));
		
		List<Notes> notes = Preconditions
				.checkNotNull(noteRepository.findByUserId(userId), 
						messages.get("167"));
		List<Notes> foundNotes=new ArrayList<>();
		for(Notes note : notes) {
			if(note.isTrash()==true) {
				foundNotes.add(note);
			}
		}
		
		return foundNotes;
	}
	
	/**
	 * To set the pinned status to true or false for the particular noteId if note archived status is false.
	 * 
	 * @param token
	 * @param noteId
	 * @return
	 */
	public String pinnedUnpinned(String userId, String noteId) {
		Preconditions.checkNotNull(userId,messages.get("168"));
		Preconditions.checkNotNull(noteId,messages.get("169"));
		
		List<Notes> notes = Preconditions
				.checkNotNull(noteRepository.findByUserId(userId),messages.get("167"));
		Notes note=	Preconditions.checkNotNull(notes.stream().filter(t -> t.getId().equals(noteId)).findFirst().orElse(null), 
				messages.get("167"));
		if(note.isArchive()!=true) {
			if(note.isPinned()==false) {
			note.setPinned(true);
			}else {
			note.setPinned(false);
			}
			noteRepository.save(note);
			return note.toString() +" is Pinned";
		}
		
		return "Archived Note Cannot Be Pinned";
	}
	
	/**
	 * To set the pinned status to true or false for the particular noteId if note archived status is false.
	 * 
	 * @param token
	 * @param noteId
	 * @return
	 */
	public String archivedOrRemoveArchived(String userId, String noteId) {
		Preconditions.checkNotNull(userId,messages.get("168"));
		Preconditions.checkNotNull(noteId,messages.get("169"));
		
		List<Notes> notes = Preconditions
				.checkNotNull(noteRepository.findByUserId(userId), messages.get("167"));
		Notes note=	Preconditions.checkNotNull(notes.stream().filter(t -> t.getId().equals(noteId)).findFirst().orElse(null), 
				messages.get("167") + noteId );
		if(note.isArchive()!=true) {
			note.setArchive(true);
			note.setPinned(false);
		}else {
			note.setArchive(false);
		}
		return note.toString()+"Note Is Archived";
	}
	
	/**
	 * To view all the trashed Notes.
	 * 
	 * @param token can not be null.
	 * @param noteId can not be null.
	 * 
	 * @return List<Notes>
	 */
	public List<Notes> viewTrash(String userId, String noteId){
		Preconditions.checkNotNull(userId,messages.get("168"));
		Preconditions.checkNotNull(noteId,messages.get("169"));
		
		List<Notes> notes = Preconditions
				.checkNotNull(noteRepository.findByUserId(userId), messages.get("167"));
		List<Notes> trashedNotes=new ArrayList<>();
		
		for(Notes note:notes) {
			if(note.isTrash()==true) {
				trashedNotes.add(note);
			}
		}
		return trashedNotes;
	}
}
