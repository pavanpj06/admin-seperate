package com.example.admin.service;

import com.example.admin.bindings.DashboardData;
import com.example.admin.bindings.LogInForm;
import com.example.admin.bindings.UnLockAccountForm;
import com.example.admin.bindings.UserAccountForm;

public interface UserService {

	
	
	public String loginAccount(LogInForm logInForm);
	
	public boolean revoverPwd(String email);
	
	
	public  DashboardData showUserDashboardInfo();
	
	
	public UserAccountForm getByEmail(String email);
	
	public String unLockTheAccount(UnLockAccountForm uncLockAcct);
}
