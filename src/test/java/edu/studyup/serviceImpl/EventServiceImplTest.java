package edu.studyup.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
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
		event.setDate(new Date());
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

	@Test
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		  });
	}
	
	@Test
	void testUpdateEvent_falseLength() {
		int eventID = 1;
		String name = "NewEventName                                     ";
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, name);
		});
	}

	
	@Test
	void testUpdateEvent_twentyLength() throws StudyUpException{
		int eventID = 1;
		String name = "12345678901234567890";
		eventServiceImpl.updateEventName(eventID, name);
		assertEquals("12345678901234567890", DataStorage.eventData.get(eventID).getName());
	}
	
	@Test 
	void test_getActiveEvents() {
		Map<Integer, Event> eventData = DataStorage.eventData;
		List<Event> activeEvents = eventServiceImpl.getActiveEvents();
		assertEquals(eventData.size(),activeEvents.size());
	}
	
	@Test 
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
	@Test
	void testUpdateEvent_LongEventID_badCase() {
		int eventID = 1;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed this file. It is over 20 characters ijiaifgdjiodfgjiodfg");
		  });
	}
	@Test 
	void testAddStudentToEvent_EventID_goodCase() {
		int eventID = 1;
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Sung");
		student.setEmail("josung@email.com");
		student.setId(2);
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student,eventID);
		  });
	}
	@Test 
	void testAddStudentToEvent_EmptyEventID_badCase() {
		int eventID = 2;
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Sung");
		student.setEmail("josung@email.com");
		student.setId(2);
		//Create Event1
		Event event = new Event();
		event.setEventID(2);
		event.setDate(new Date());
		event.setName("Event 2");
		Location location = new Location(-12, 37);
		event.setLocation(location);
				
		DataStorage.eventData.put(event.getEventID(), event);
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.addStudentToEvent(student,eventID);
		  });
	}
	
	@Test 
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
	@Test

	void testgetActiveEvent() throws StudyUpException{
		Event event = new Event();
		event.setEventID(2);
		event.setDate(new Date(2020, 12, 13));
		event.setName("Event 2");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		DataStorage.eventData.put(event.getEventID(), event);
		List<Event> expected = new ArrayList<>();
		expected.add(event);
		List<Event> actual = eventServiceImpl.getActiveEvents();
		assertTrue(DataStorage.eventData.get(1).getDate().before(new Date()));
		assertEquals(expected, actual);
	}
}
