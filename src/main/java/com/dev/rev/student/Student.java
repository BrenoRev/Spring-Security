package com.dev.rev.student;

import java.io.Serializable;

import lombok.Data;

@Data
public class Student implements Serializable{
	private static final long serialVersionUID = 1L;

	public final Integer studentId;
	public final String studentName;
	
}
