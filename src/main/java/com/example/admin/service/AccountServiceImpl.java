package com.example.admin.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.admin.bindings.EditUserAccount;
import com.example.admin.bindings.PlanStatsResponse;
import com.example.admin.bindings.UnLockAccountForm;
import com.example.admin.bindings.UserAccountForm;
import com.example.admin.entities.UserEntity;
import com.example.admin.exceptions.ResourceNotFoundException;
import com.example.admin.reposities.PlanRepo;
import com.example.admin.reposities.UserRepo;
import com.example.admin.utils.EmailUtils;

import jakarta.mail.MessagingException;

@Service
public class AccountServiceImpl implements AccountService{

	
	
	private UserEntity userEntity;
	@Autowired
	private  UserRepo userRepository;

	
	@Autowired
	private  PlanRepo planRepository;
	
	 @Autowired
	    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailUtils emailUtils;
	
	
	
	
	@Override
	public boolean createUserAccount(UserAccountForm accForm)   {
		
		
		try {
		 userEntity=new UserEntity();
		 
		 
//		 BeanUtils.copyProperties(accForm, userEntity);
		 // here copying data form dto to entity 
		 //Note:  if you use this BeanUtils.copy fileds will directly copyed form binding to entity
		 
		 Optional<UserEntity> byEmail = userRepository.findByEmail(accForm.getEmail());
		 
		 if(!(byEmail.isPresent())) {
		 userEntity.setFullName(accForm.getFullName());
		 
		 userEntity.setEmail(accForm.getEmail());
		 userEntity.setGender(accForm.getGender());
		 userEntity.setMobileNumber(accForm.getMobileNumber());
		 userEntity.setDateOfBirth(accForm.getDateOfBirth());
		 String password=
				 generateRandomPwd();
		  String encodedPassword = passwordEncoder.encode(password);
		  System.out.println("Temp password: "+password);
		  
		 userEntity.setPassword(encodedPassword);
		 
		 userEntity.setAccountStatus("Locked");
		 userEntity.setAccountSw("Y");
		 userEntity.setSsNumber(accForm.getSsNumber());
		 userEntity.setRoleId(accForm.getRoleId());
		userRepository.save(userEntity);
		

	
		
			return 	emailUtils.emailSending(accForm.getEmail(),password);
		
	
		 
		 
		 
		 }
		 else
			 return false;
		 
		 
	} catch (Exception e) {
		 e.printStackTrace();// add this step in log
			return false; 
			 
	}
		 
		
	}

	@Override
	public Page<UserAccountForm> getAllUserAccountDetails(int page, int size, String sortField, String sortOrder) {
	    Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
	    PageRequest pageRequest = PageRequest.of(page, size, sort);

	    Page<UserEntity> userEntityPage = userRepository.findAll(pageRequest);

	    List<UserAccountForm> dtoList = userEntityPage.getContent().stream().map(user -> {
	        UserAccountForm dto = new UserAccountForm();
	        dto.setId(user.getId());
	        dto.setEmail(user.getEmail());
	        dto.setFullName(user.getFullName());
	        dto.setGender(user.getGender());
	        dto.setMobileNumber(user.getMobileNumber());
	        dto.setSsNumber(user.getSsNumber());
	        dto.setDateOfBirth(user.getDateOfBirth());
	        dto.setRoleId(user.getRoleId());
	        return dto;
	    }).collect(Collectors.toList());

	    return new PageImpl<>(dtoList, pageRequest, userEntityPage.getTotalElements());
	}

	@Override
	public String unlockTheAccount(UnLockAccountForm unlckForm) {
		Optional<UserEntity> byEmail = userRepository.findByEmail(unlckForm.getEmail());
		
		if(byEmail.isPresent()) {
			UserEntity userEntity2 = byEmail.get();
		

			if (passwordEncoder.matches(unlckForm.getTempPassword(), userEntity2.getPassword())) {
				
				String newEncodedPassword = passwordEncoder.encode(unlckForm.getConfirmPassword().trim());
	            userEntity2.setPassword(newEncodedPassword);
				
				userEntity2.setAccountStatus("UN-Locked");
				
				userRepository.save(userEntity2);
				
				return "Account unlocking is done";
			}else
				return "your temp password is worng, please enter the correct";
		
		}else 
			
		return "with this mail id account doesn't exist";
		
	}
	

	@Override
	public String editUserAccountById(Long id,EditUserAccount  ed) {
		String status="Record not able to edit";
		Optional<UserEntity> byId = userRepository.findById(id);
		if(byId.isPresent()) {
			UserEntity userEntity = byId.get();

			userEntity.setFullName(ed.getFullName());
			userEntity.setMobileNumber(ed.getMobileNumber());
			userEntity.setGender(ed.getGender());
			
		
			userRepository.save(userEntity);
			
			status="Record updated Succesfully";	
		}else 
			 status="Record not present";
		
		return status;
	}
	
	
	
	@Override
	@Transactional
	public String accountStatusChange(Integer id, String status) {
		
		Integer updateTheAccount = userRepository.updateTheAccount(id, status);
		if(updateTheAccount>0)
			return"Account status is updated";
		
		
		return "Failed to update";
	}
	
	
	private String generateRandomPwd() {
		
		
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    StringBuilder sb = new StringBuilder();
	    Random random = new Random();

	    // specify length of random string
	    int length = 7;

	    for(int i = 0; i < length; i++) {

	      // generate random index number
	      int index = random.nextInt(alphabet.length());

	      // get character specified by index
	      // from the string
	      char randomChar = alphabet.charAt(index);

	      // append the character to string builder
	      sb.append(randomChar);
	    }

	    String randomString = sb.toString();
	    return randomString;
	}

	

	

	@Override
	public String createMultipleAccounts(List<UserAccountForm> accForm) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String deleteById(Long id) {
	    try {
	        // Check if the account exists before deleting
	      Optional<UserEntity> byId = userRepository.findById(id);
	        if (!byId.isPresent()) {
	            throw new ResourceNotFoundException("User not found with ID: " + id);
	        }

	        // Proceed to delete the account
	        userRepository.deleteById(id);
	        return "Your account deleted successfully.";

	    } catch (Exception e) {
	        return "Account not deleted: " + e.getMessage();
	    }
	}

	@Override
	public PlanStatsResponse getAllPlansCountWithDates() {
	    Long planCount = planRepository.getAllPlansCount();

	
	    LocalDate earliestDate = planRepository.findEarliestPlanStartDate();
	    LocalDate latestDate = planRepository.findLatestPlanEndDate();

	    
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
	    String formattedStartDate = earliestDate != null ? earliestDate.format(formatter) : "Unknown";
	    String formattedEndDate = latestDate != null ? latestDate.format(formatter) : "Unknown";

	    PlanStatsResponse response=new PlanStatsResponse();
	    response.setStartDate(formattedStartDate);
	    
	    response.setEndDate(formattedEndDate);
	    response.setPlanCount(planCount);


	    return response;
	}

	@Override
	public List<UserAccountForm> getAllAccounts() {
		List<UserEntity> all = userRepository.findAll();
		return all.stream().map(entity -> {
            UserAccountForm dto = new UserAccountForm();
            dto.setId(entity.getId());
            dto.setFullName(entity.getFullName());
            dto.setEmail(entity.getEmail());
            dto.setMobileNumber(entity.getMobileNumber());
            dto.setGender(entity.getGender());
            dto.setSsNumber(entity.getSsNumber());
            return dto;
        }).collect(Collectors.toList());
	}

	




	
	
	
}
