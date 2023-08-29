package com.prodapt.learningspring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.model.Student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    private List<Student> studentList = new ArrayList<>();
   
    @GetMapping
    public String showStudentForm(Student student, Model model) {
    	model.addAttribute("student", new Student());
        model.addAttribute("studentList", studentList);
        return "student";
    }

    @PostMapping("/add")
    public String processStudentForm(Student student, BindingResult bindingResult,
                                     Model model) {

    	System.out.println(student.getName());
    	System.out.println(student.getScore());
        student.setId(studentList.size() + 1);
        System.out.println(student.getId());
        studentList.add(student);
        
        return "redirect:/student";
    }

    @PostMapping("/delete")
    public String deleteStudent(int idx) {
    	System.out.println(idx);
        studentList.remove(idx-1);
        System.out.println(studentList);
        return "redirect:/student";
    }

    @GetMapping("/edit")
    public String editStudent( int idx, Model model) {
        if (idx >= 0 && idx < studentList.size()) {
            Student student = studentList.get(idx);
            model.addAttribute("studentList", student);
        }
        return "student";
    }

//    @PostMapping("/update")
//    public String updateStudent(Student studentToEdit, BindingResult bindingResult,
//                                Model model) {
//        if (studentToEdit.getId() > 0 && studentToEdit.getId() <= studentList.size()) {
//            studentList.set(studentToEdit.getId() - 1, studentToEdit);
//        }
//        return "redirect:/student";
//    }

    @PostMapping("/sort")
    public String sortStudentsByRank() {
        Collections.sort(studentList, Comparator.comparingInt(Student::getRank));
        return "redirect:/student";
    }
       
}
