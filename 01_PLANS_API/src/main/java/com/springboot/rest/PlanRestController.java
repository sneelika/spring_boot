package com.springboot.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.constants.AppConstants;
import com.springboot.entity.Plan;
import com.springboot.props.AppProperties;
import com.springboot.service.PlanService;

@RestController
public class PlanRestController {

	private PlanService planService;

	Map<String, String> messages;

	public PlanRestController(PlanService planService, AppProperties appProps) {
		this.planService = planService;
		this.messages = appProps.getMessages();
		System.out.println(this.messages);
	}

	@GetMapping("/categories")
	public ResponseEntity<Map<Integer, String>> planCategories() {
		Map<Integer, String> categories = planService.getPlanCategories();
		return new ResponseEntity<>(categories, HttpStatus.OK);
	}

	@PostMapping("/plan")
	public ResponseEntity<String> savePlan(@RequestBody Plan plan) {
		String msgString = AppConstants.EMPTY_STRING;
		boolean isSaved = planService.savePlan(plan);
		if (isSaved) {
			msgString = messages.get(AppConstants.PLAN_SAVE_SUCCESS);
		} else {
			msgString = messages.get(AppConstants.PLAN_SAVE_FAIL);
		}
		return new ResponseEntity<>(msgString, HttpStatus.CREATED);
	}

	@GetMapping("/plans")
	public ResponseEntity<List<Plan>> plans() {
		List<Plan> allPlans = planService.getAllPlans();
		return new ResponseEntity<>(allPlans, HttpStatus.OK);
	}

	@GetMapping("/plan/{planId}")
	public ResponseEntity<Plan> editPlan(@PathVariable Integer planId) {
		Plan plan = planService.getPlanById(planId);
		return new ResponseEntity<>(plan, HttpStatus.OK);
	}

	@PutMapping("/plan")
	public ResponseEntity<String> updatePlan(@RequestBody Plan plan) {
		boolean isUpdated = planService.updatePlan(plan);

		String msgString = AppConstants.EMPTY_STRING;
		if (isUpdated) {
			msgString = AppConstants.PLAN_UPDATE_SUCCESS;
		} else {
			msgString = AppConstants.PLAN_UPDATE_FAIL;
		}

		return new ResponseEntity<>(msgString, HttpStatus.OK);

	}

	@DeleteMapping("/plan/{planId}")
	public ResponseEntity<String> deletePlan(@PathVariable Integer planId) {
		boolean isDeleted = planService.deletePlanById(planId);

		String msgString = AppConstants.EMPTY_STRING;
		if (isDeleted) {
			msgString = AppConstants.PLAN_DELETE_SUCCESS;
		} else {
			msgString = AppConstants.PLAN_DELETE_FAIL;
		}
		return new ResponseEntity<>(msgString, HttpStatus.OK);
	}

	@PutMapping("status-change/{planId}/{status}")
	public ResponseEntity<String> statusChange(@PathVariable Integer planId, @PathVariable String status) {
		String msgString = AppConstants.EMPTY_STRING;
		boolean isStatusChanged = planService.planStatusChange(planId, status);
		if (isStatusChanged) {
			msgString = AppConstants.PLAN_STATUS_CHANGE;
		} else {
			msgString = AppConstants.PLAN_STATUS_CHANGE_FAIL;
		}

		return new ResponseEntity<>(msgString, HttpStatus.OK);
	}

}