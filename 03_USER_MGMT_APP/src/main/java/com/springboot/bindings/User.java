package com.springboot.bindings;

import java.time.LocalDate;

import lombok.Data;

@Data
public class User {
	private String fullName;
	private String email;
	private Long mobile;
	private LocalDate dob;
	private String gender;
	private Long ssn;
}
