package com.example.admin.reposities;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.admin.entities.PlanEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface PlanRepo extends JpaRepository<PlanEntity, Long>{

	
	
	Optional<PlanEntity> findByPlanName(String planName);
	
	@Query(value = "SELECT COUNT(*) FROM plans_ies", nativeQuery = true)
	public Long getTotalPlansCount();
	
	
	@Query("SELECT p FROM PlanEntity p WHERE p.planStartDate = CURRENT_DATE")
	List<PlanEntity> findPlansCreatedToday();


	
	@Query("SELECT MIN(p.planStartDate) FROM PlanEntity p")
	LocalDate findEarliestPlanStartDate();

	@Query("SELECT MAX(p.planEndDate) FROM PlanEntity p")
	LocalDate findLatestPlanEndDate();
	
	@Query(value = "select count(*) from plans_ies",nativeQuery = true)
	public Long getAllPlansCount();

}
