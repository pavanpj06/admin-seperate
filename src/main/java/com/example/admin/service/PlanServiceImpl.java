package com.example.admin.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.admin.bindings.PlanForm;
import com.example.admin.constants.AppConstants;
import com.example.admin.entities.PlanEntity;
import com.example.admin.entities.UserEntity;
import com.example.admin.reposities.PlanRepo;
import com.example.admin.reposities.UserRepo;

@Service
public class PlanServiceImpl implements PlanService {

	@Autowired
	private PlanRepo planRepo;

	
	
	
	@Value("${shceduling.cofig.email}")
	private String emailSchedulingConfig;
	
	
    @Autowired
    private JavaMailSender javaMailSender;
	@Autowired
	private UserRepo userRepo;
	PlanEntity ent = null;

	@Override
	public String createPlan(PlanForm planForm) {
		String msg = null;

//		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//		    String email = authentication.getName();

//		    UserEntity user = userRepo.findByEmail(email).orElseThrow();

		Optional<UserEntity> user = userRepo.findByEmail(planForm.getCreateByUser());

		UserEntity userEntity = user.get();
//		            .orElseThrow(() -> new RuntimeException("User not found with email: " + planForm.getCreateByUser()));
		Optional<PlanEntity> byPlanName = planRepo.findByPlanName(planForm.getPlanName());
		if (!byPlanName.isPresent()) {
			ent = new PlanEntity();
			ent.setPlanName(planForm.getPlanName());
			ent.setPlanStartDate(planForm.getStartDate());
			ent.setPlanEndDate(planForm.getEndDate());
			ent.setUser(userEntity);

			planRepo.save(ent);
			msg = AppConstants.PLAN_CREATION_MSG;
		} else
			msg = AppConstants.PLAN_EXISTS;

		return msg;
	}

	@Override
	public PlanForm editThePlan(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createMultiplePlans(List<PlanForm> planForm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<PlanForm> getAllThePlan(int page, int size,String sortField, String sortDir) {
		 Sort sort = sortDir.equalsIgnoreCase("desc") ?
	                Sort.by(sortField).descending() :
	                Sort.by(sortField).ascending();
		PageRequest of = PageRequest.of(page, size,sort);
		Page<PlanEntity> all = planRepo.findAll(of);
		List<PlanForm> collect = all.getContent().stream().map(user -> {
			PlanForm planForm = new PlanForm();
			planForm.setId(user.getId());
			planForm.setPlanName(user.getPlanName());
			planForm.setStartDate(user.getPlanStartDate());
			planForm.setEndDate(user.getPlanEndDate());
			planForm.setCreateByUser(user.getUser().getEmail());

			return planForm;

		}).collect(Collectors.toList());

		 return new PageImpl<>(collect, of, all.getTotalElements());
	}

	@Override
	public Long getTotalPlansCount() {
		Long totalPlansCount = planRepo.getTotalPlansCount();
		return totalPlansCount;
	}

	@Override
	public List<PlanEntity> getAllPlans() {
	    return planRepo.findAll(); 
	}
	
	
	@Scheduled(cron = "0 45 15 * * ?", zone = "Asia/Kolkata")
    public void sendTodayCreatedPlansEmail() {
        List<PlanEntity> todayPlans = planRepo.findPlansCreatedToday();

        if (!todayPlans.isEmpty()) {
            StringBuilder content = new StringBuilder("Plans created today:\n\n");
            for (PlanEntity plan : todayPlans) {
                content.append(plan.getPlanName()).append("\n");
            }

            sendEmail(emailSchedulingConfig, "Today's Plans Summary", content.toString());
        }
    }
	/*
	 ┌───────────── second (0 - 59)
	 │ ┌───────────── minute (0 - 59)
	 │ │ ┌───────────── hour (0 - 23)
	 │ │ │ ┌───────────── day of month (1 - 31)
	 │ │ │ │ ┌───────────── month (1 - 12 or JAN-DEC)
	 │ │ │ │ │ ┌───────────── day of week (1 - 7 or SUN-SAT, ? means "no specific value")
	 │ │ │ │ │ │
	 │ │ │ │ │ │
	 0  0  18  *  *  ?
	 */

	
	private void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }


}
