package com.springboot.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.springboot.binding.Education;
import com.springboot.service.DcService;

public class EducationRestController {

	@Autowired
	private DcService service;

	@PostMapping
	public ResponseEntity<Long> saveEducation(@RequestBody Education education) {
		Long caseNum = service.saveEducation(education);
		return new ResponseEntity<>(caseNum, HttpStatus.CREATED);
	}

}
