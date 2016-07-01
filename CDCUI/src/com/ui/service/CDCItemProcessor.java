package com.ui.service;
import org.springframework.batch.item.ItemProcessor;

import com.ui.model.Marksheet;
import com.ui.model.Student;
public class CDCItemProcessor implements ItemProcessor<Student, Marksheet> {
    @Override
    public Marksheet process(final Student student) throws Exception {

    	int totalMark = student.getSubMarkOne()+student.getSubMarkTwo();
    	System.out.println("student id:"+student.getStdId() +" and Total mark:"+ totalMark);
    	Marksheet marksheet = new Marksheet(student.getStdId(), totalMark);
        return marksheet;
    }
} 