package com.example.admin.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.admin.bindings.PlanForm;
import com.example.admin.entities.PlanEntity;
import com.example.admin.service.PlanService;

import jakarta.validation.Valid;

@RestController
public class PlanController {

	@Autowired
	private PlanService planService;

	Map<String, String> messageResponse = new HashMap();

	@PostMapping("/create-the-plan")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Map<String, String>> createPlan(@Valid @RequestBody PlanForm planForm) {

		ResponseEntity<Map<String, String>> response = null;

		String plan = planService.createPlan(planForm);

		if (plan.contains("New Plan Created")) {
			messageResponse.put("message", plan);
			response = new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
		} else {
			messageResponse.put("message", plan);
			response = new ResponseEntity<>(messageResponse, HttpStatus.CONFLICT);
		}
		return response;
	}

	@GetMapping("/view-all-plans")
	@CrossOrigin(origins = "http://localhost:3000")
	public ResponseEntity<Page<PlanForm>> viewPlan(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "planName") String sortField,
	        @RequestParam(defaultValue = "asc") String sortDir) {

		return new ResponseEntity<>(planService.getAllThePlan(page, size,sortField, sortDir), HttpStatus.OK);
	}
	
	
	@GetMapping("/get-all-plancount")
	public ResponseEntity<Long> getTotalPlans(){
		
		Long totalPlansCount = planService.getTotalPlansCount();
	    return ResponseEntity.ok(totalPlansCount);
		
	}
	
	
	
	@GetMapping("/get-allJson")
	public ResponseEntity<List<PlanEntity>> getAllPlansJsonIgnore(){
		
		return new ResponseEntity<>(planService.getAllPlans(),HttpStatus.OK);
	}

}
