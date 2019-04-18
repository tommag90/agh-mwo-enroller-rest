package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") long meetingId) {
		Meeting meeting = meetingService.findById(meetingId);
		if(meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
		Meeting foundMeeting = meetingService.findById(meeting.getId());
		if(foundMeeting != null) {
			return new ResponseEntity("Unable to create. A meeting with id " + meeting.getId() + " already exist.", HttpStatus.CONFLICT);
		}
		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}/{userLogin}", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") long meetingId,
			@PathVariable("userLogin") String login, @RequestBody Participant participant) {
		Meeting meeting = meetingService.findById(meetingId);
		if(meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		Participant foundParticipant = new ParticipantService().findByLogin(login);
		if(foundParticipant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		meeting.addParticipant(participant);
		meetingService.updateMeeting(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	
	}
	
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long meetingId) {
		Meeting meeting = meetingService.findById(meetingId);
		if(meeting == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		Collection<Participant> participants = meeting.getParticipants();
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

//	@RequestMapping(value = "", method = RequestMethod.POST)
//	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
//		Participant foundParticipant = participantService.findByLogin(participant.getLogin());
//		if (foundParticipant != null) {
//			return new ResponseEntity("Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
//	     }
//		
//		participantService.add(participant);
//		return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);
//	}
//	
//	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
//	public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
//		Participant participant = participantService.findByLogin(login);
//		if (participant == null) {
//	         return new ResponseEntity(HttpStatus.NOT_FOUND);
//	     }
//		participantService.delete(participant);
//		return new ResponseEntity("Participant " + participant.getLogin() + " deleted", HttpStatus.OK);
//	}
//	
//	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
//	public ResponseEntity<?> updateParticipantPassword(@PathVariable("id") String login,
//			@RequestBody Participant incomingParticipant) {
//		Participant participant = participantService.findByLogin(login);
//		if (participant == null) {
//	         return new ResponseEntity(HttpStatus.NOT_FOUND);
//	     }
//		participant.setPassword(incomingParticipant.getPassword());
//		participantService.update(participant);
//		return new ResponseEntity("Participant " + participant.getLogin() + " updated", HttpStatus.OK);
//	}

}
