package com.example.student.service;

import com.example.student.entity.Student;
import com.example.student.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    //全て学生の表示
    public List<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    //個別の表示
    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    //新規作成
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    //更新
    public Student updateStudent(Long id, Student student) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found");
        }
        student.setId(id);
        return studentRepository.save(student);
    }

    //削除
    public boolean deleteStudent(Long id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return true;
        }
        return false;
    }
}