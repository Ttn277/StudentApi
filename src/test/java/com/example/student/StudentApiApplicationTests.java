package com.example.student;

import com.example.student.entity.Student;
import com.example.student.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentApiApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp() {
		studentRepository.deleteAll();
	}

	@Test
	public void testGetAllStudent() throws Exception {
		Student student1 = new Student();
		student1.setNo("Test Student 1");
		student1.setName("Student A");
		student1.setClassName("１年生");
		studentRepository.save(student1);

		mockMvc.perform(get("/api/student"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].no").value("Test Student 1"))
				.andExpect(jsonPath("[0].name").value("Student A"))
				.andExpect(jsonPath("$[0].className").value("１年生"));
	}

	@Test
	public void testCreateStudent() throws Exception {
		Student student = new Student();
		student.setNo("New Student");
		student.setName("This is a new student");
		student.setClassName("２年生");

		mockMvc.perform(post("/api/student")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(student)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.no").value("New Student"))
				.andExpect(jsonPath("$.name").value("This is a new student"))
				.andExpect(jsonPath("$.className").value("２年生"));

		//DBに新しSTUDENTが作成されたか確認
		Student createdStudent = studentRepository.findAll().get(0);
		assertEquals("New Student", createdStudent.getNo());
		assertEquals("This is a new student", createdStudent.getName());
		assertEquals("２年生", createdStudent.getClassName());
	}

	@Test
	public void testUpdateStudent() throws Exception {
		Student student = new Student();
		student.setNo("Update No");
		student.setName("Update Student Name");
		student.setClassName("Update Student Class");
		Student savedStudent = studentRepository.save(student);

		savedStudent.setNo("Updated No");
		savedStudent.setName("Updated Name");
		savedStudent.setClassName("Updated ClassName");

		mockMvc.perform(put("/api/student/" + savedStudent.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(savedStudent)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.no").value("Updated No"))
				.andExpect(jsonPath("$.name").value("Updated Name"))
				.andExpect(jsonPath("$.className").value("Updated ClassName"));


		//DBの値が更新されたことを確認する
		Student updatedStudent = studentRepository.findById(savedStudent.getId()).get();
		assertEquals("Updated No", updatedStudent.getNo());
		assertEquals("Updated Name", savedStudent.getName());
		assertEquals("Updated ClassName", savedStudent.getClassName());
	}

	@Test
	public void testDeleteStudent() throws Exception {
		Student student = new Student();
		student.setNo("Delete Student No");
		student.setName("Delete Student Name");
		student.setClassName("Delete Student ClassName");
		Student savedStudent = studentRepository.save(student);

		mockMvc.perform(delete("/api/student/" + savedStudent.getId()))
				.andExpect(status().isOk());

		//DBから削除されていることを確認
		assertTrue(studentRepository.findById(savedStudent.getId()).isEmpty());
	}
}
