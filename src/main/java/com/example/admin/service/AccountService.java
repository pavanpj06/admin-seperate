package com.example.admin.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.example.admin.bindings.EditUserAccount;
import com.example.admin.bindings.PlanStatsResponse;
import com.example.admin.bindings.UnLockAccountForm;
import com.example.admin.bindings.UserAccountForm;

import jakarta.mail.MessagingException;

public interface AccountService {

	
	
	public boolean createUserAccount(UserAccountForm accForm) throws MessagingException, IOException;// create account 
	
	
	
	public String createMultipleAccounts(List<UserAccountForm> accForm);
	
//	Note: here String(String) is written time 
	
	
	
	
	
	
	

	public Page<UserAccountForm> getAllUserAccountDetails(int page, int size,String sortingField,String sortOrder);// Retrieve all account details 
	
	public List<UserAccountForm>  getAllAccounts();
	
	
//	Note: here List<UserAccountForm>(collection of object) is written time 
	
	public String unlockTheAccount(UnLockAccountForm unlckForm);
	
	
	public String deleteById(Long id);
	
	
	public PlanStatsResponse getAllPlansCountWithDates();
public String editUserAccountById(Long id,EditUserAccount  ed);// get the account based on id for editing 
//	editing for based on record return type is total that page will come naa 
//	Note: here UserAccountForm(object) is written time 
	

	public String accountStatusChange(Integer id, String status);// activating and de-activating accout
}
