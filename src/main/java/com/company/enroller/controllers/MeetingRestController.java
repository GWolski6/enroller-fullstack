package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/meetings")
public class MeetingRestController
{
    @Autowired
    MeetingService meetingService;

    @Autowired
    ParticipantService participantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings()
    {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
    }
    
    @RequestMapping(value = "", method = RequestMethod.POST) 
	public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting)
    {
		Meeting foundMeeting = meetingService.findById(meeting.getId());
		
		if (foundMeeting != null)
		{
			return new ResponseEntity<String>("Unable to register. Meeting with ID " + meeting.getId() + " already exists", HttpStatus.CONFLICT);
		}
		
		meetingService.add(meeting);
		return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/{login}", method = RequestMethod.POST) 
	public ResponseEntity<?> addParticipantToAMeeting(@PathVariable("id") long id, @PathVariable("login") String login)
	{
		Meeting desiredMeeting = meetingService.findById(id);
		Participant participantToAdd = participantService.findByLogin(login);
		
		if (desiredMeeting == null)
		{
			return new ResponseEntity<String>("Unable to add participant. Meeting with ID " + id + " does not exists", HttpStatus.NOT_FOUND);
		}
		
		if (participantToAdd == null)
		{
			return new ResponseEntity<String>("Unable to add participant. Participant with login " + login + " does not exists", HttpStatus.NOT_FOUND);
		}
		
		if (desiredMeeting.getParticipants().contains(participantToAdd))
		{
			return new ResponseEntity<String>("Unable to add participant. Participant with login " + participantToAdd.getLogin() + " is already assigned to this meeting", HttpStatus.CONFLICT);
		}
		
		desiredMeeting.addParticipant(participantToAdd);
		desiredMeeting = meetingService.update(desiredMeeting);
		return new ResponseEntity<Collection<Participant>>(desiredMeeting.getParticipants(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id)
	{
	    Meeting meeting = meetingService.findById(id);
	if (meeting == null)
	{ 
	return new ResponseEntity(HttpStatus.NOT_FOUND);
	}
	
	meetingService.delete(meeting);
	return new ResponseEntity<Meeting>(meeting, HttpStatus.OK); 
	}
	
	@RequestMapping(value = "/{id}/{login}", method = RequestMethod.DELETE) 
	public ResponseEntity<?> deleteParticipantFromMeeting(@PathVariable("id") long id, @PathVariable("login") String login)
	{
		Meeting desiredMeeting = meetingService.findById(id);
		Participant participantToAdd = participantService.findByLogin(login);
		
		if (desiredMeeting == null)
		{
			return new ResponseEntity<String>("Unable to remove participant. Meeting with ID " + id + " does not exists", HttpStatus.NOT_FOUND);
		}
		
		if (participantToAdd == null)
		{
			return new ResponseEntity<String>(
					"Unable to remove participant. Participant with login " + login + " does not exists", HttpStatus.NOT_FOUND);
		}
		
		if (!desiredMeeting.getParticipants().contains(participantToAdd))
		{
			return new ResponseEntity<String>(
					"Unable to remove participant. Participant with login " + participantToAdd.getLogin() + " is not assigned to this meeting", HttpStatus.CONFLICT);
		}
		
		desiredMeeting.removeParticipant(participantToAdd);
		desiredMeeting = meetingService.update(desiredMeeting);
		return new ResponseEntity<Collection<Participant>>(desiredMeeting.getParticipants(), HttpStatus.OK);
	}
}
