package edu.studyup.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

class EventServiceImplTest {

	EventServiceImpl eventServiceImpl;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		eventServiceImpl = new EventServiceImpl();
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//Create Event1
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date(2020, 12, 13));
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		DataStorage.eventData.put(event.getEventID(), event);
	}

	@AfterEach
	void tearDown() throws Exception {
		DataStorage.eventData.clear();
	}

	@Test //should pass 
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test //should pass 
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		  });
	}
	
	@Test //should pass 
	void testUpdateEvent_falseLength() {
		int eventID = 1;
		String name = "NewEventName                                     ";
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, name);
		});
	}

	
	@Test //should pass 
	void testUpdateEvent_twentyLength() throws StudyUpException{
		int eventID = 1;
		String name = "12345678901234567890";
		eventServiceImpl.updateEventName(eventID, name);
		assertEquals("12345678901234567890", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test //should pass 
	void testUpdateEvent_LongEventID_badCase() {
		int eventID = 1;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed this file. It is over 20 characters ijiaifgdjiodfgjiodfg");
		  });
	}
	
	@Test //should pass all
	void testgetActiveEvent_pastEvent_badCase() throws StudyUpException{
		Event event = new Event();
		event.setEventID(2);
		event.setDate(new Date(97, 12, 13));
		event.setName("Event 2");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		DataStorage.eventData.put(event.getEventID(), event);
		
		Event event3 = new Event();
		event3.setEventID(3);
		event3.setDate(new Date(98, 12, 13));
		event3.setName("Event 3");
		Location location3 = new Location(-2, 37);
		event.setLocation(location3);
		DataStorage.eventData.put(event3.getEventID(), event3);
		
		Map<Integer, Event> eventData = DataStorage.eventData;
		List<Event> activeEvents = eventServiceImpl.getActiveEvents();
		assertTrue(eventData.get(1).getDate().after(new Date()));
		assertTrue(eventData.get(3).getDate().before(new Date()));
		assertTrue(new Date(97, 12, 13).before(new Date()));
		assertEquals(1,activeEvents.size());
	}
	
	@Test //should pass
	void testAddStudentToEvent_NegativeEventID_badCase() {
		int eventID = -1;
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Sung");
		student.setEmail("josung@email.com");
		student.setId(2);
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student,eventID);
		  });
	}
	
	@Test //should pass
	void testAddStudentToEvent_EventID_goodCase() throws StudyUpException{
		int eventID = 1;
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Sung");
		student.setEmail("josung@email.com");
		student.setId(2);
		eventServiceImpl.addStudentToEvent(student,eventID);
		Map<Integer, Event> eventData = DataStorage.eventData;
		assertEquals(eventData.get(eventID).getStudents().size(), 2);
		  
	}
	@Test //should pass
	void testAddStudentToEvent_EmptyEvent_badCase() throws StudyUpException{
		int eventID = 2;
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Sung");
		student.setEmail("josung@email.com");
		student.setId(2);
		//Create Event2
		Event event = new Event();
		event.setEventID(2);
		event.setDate(new Date());
		event.setName("Event 2");
		Location location = new Location(-12, 37);
		event.setLocation(location);
				
		DataStorage.eventData.put(event.getEventID(), event);
		eventServiceImpl.addStudentToEvent(student,eventID);
		Map<Integer, Event> eventData = DataStorage.eventData;
		assertEquals(eventData.get(eventID).getStudents().size(), 1);
	}
	
	@Test //should pass
	void testAddStudentToEvent_morethantwo_badcase() throws StudyUpException {
		int eventID = 1;
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Sung");
		student.setEmail("josung@email.com");
		student.setId(1);
		Student student2 = new Student();
		student2.setFirstName("Kai");
		student2.setLastName("Shen");
		student2.setEmail("kzshen@email.com");
		student2.setId(1);
	    eventServiceImpl.addStudentToEvent(student,eventID);
	    Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student2,eventID);
		  });
	}
	
	@Test //all should pass
	void test_getPastEvents() {
		Event event = new Event();
		event.setEventID(2);
		event.setDate(new Date(97, 12, 13));
		event.setName("Event 2");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		DataStorage.eventData.put(event.getEventID(), event);
		
		Event event3 = new Event();
		event3.setEventID(3);
		event3.setDate(new Date(98, 12, 13));
		event3.setName("Event 3");
		Location location3 = new Location(-2, 37);
		event.setLocation(location3);
		DataStorage.eventData.put(event3.getEventID(), event3);
		
		Map<Integer, Event> eventData = DataStorage.eventData;
		List<Event> pastEvents = eventServiceImpl.getPastEvents();
		assertTrue(eventData.get(1).getDate().after(new Date()));
		assertTrue(eventData.get(3).getDate().before(new Date()));
		assertTrue(new Date(97, 12, 13).before(new Date()));
		assertEquals(2,pastEvents.size());
	}
	
	@Test //should pass
	void test_deleteEvent() {
		int eventID = 1;
		eventServiceImpl.deleteEvent(eventID);
		Map<Integer, Event> eventData = DataStorage.eventData;
		Map<Integer, Event> expectedData = new HashMap<Integer, Event>();
		assertEquals(expectedData.size(), eventData.size());
	}
	
}
