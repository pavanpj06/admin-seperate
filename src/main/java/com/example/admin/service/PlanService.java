package com.example.admin.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.example.admin.bindings.PlanForm;
import com.example.admin.entities.PlanEntity;

public interface PlanService {

	
	
	
	public String createPlan(PlanForm  planForm);
	
	
	
	
	public String createMultiplePlans(List<PlanForm>  planForm);
	
	
	
	public Page<PlanForm> getAllThePlan(int page, int size,String sortField, String sortDir);

	
	public PlanForm editThePlan(Integer id);
	
	
	public Long getTotalPlansCount();
	
	public List<PlanEntity> getAllPlans();
	

	
	
	
	
	
	
}
