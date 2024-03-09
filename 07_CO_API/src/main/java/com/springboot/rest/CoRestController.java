package com.springboot.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import com.springboot.binding.CoResponse;
import com.springboot.service.CoService;

public class CoRestController {
	@Autowired
	private CoService service;

	@GetMapping("/process")
	public CoResponse processTriggers() {
		return service.processPendingTriggers();
	}
}
