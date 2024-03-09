package com.springboot.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.binding.EligResponse;
import com.springboot.service.EligService;

@RestController
public class EduRestController {

	@Autowired
	private EligService service;

	@GetMapping("/eligibility/{caseNum}")
	public EligResponse determineEligibility(@PathVariable Long caseNum) {
		EligResponse eligResponse = service.determineEligibility(caseNum);
		return eligResponse;

	}
}
