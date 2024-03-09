package com.springboot.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.binding.CitizenApp;
import com.springboot.service.ArService;

@RestController
public class ArRestController {
	@Autowired
	private ArService service;

	@PostMapping("/app")
	public ResponseEntity<String> createCitizenApp(@RequestBody CitizenApp app) {
		Integer status = service.createApplication(app);
		if (status > 0) {
			return new ResponseEntity<>("App created with App Id  :" + status, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Invalid SSN", HttpStatus.BAD_REQUEST);
		}
	}
}