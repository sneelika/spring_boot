package com.springboot.binding;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CitizenApp {
	private String fullName;
	private String email;
	private Long phno;
	private String gender;
	private Long ssn;
	private LocalDate dob;

}
