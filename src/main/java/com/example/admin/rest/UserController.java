package com.example.admin.rest;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.admin.bindings.DashboardData;
import com.example.admin.bindings.LogInForm;
import com.example.admin.bindings.UserAccountForm;
import com.example.admin.service.UserService;

@RestController
public class UserController {

	

	@Autowired
	private UserService  userService;
	
	
	
	
	
//	@PostMapping("user-login")
//	public ResponseEntity<Map<String,String>> login(@RequestBody  LogInForm loginForm){
//
//		Map<String,String> response=new HashMap();
//		
//		
//		
//		
//	}
	
	
	
	@GetMapping("/dashboard")
	public ResponseEntity<DashboardData> getDashboardData(@RequestParam("email")  String email){
		
		UserAccountForm byEmail = userService.getByEmail(email);
		
		DashboardData showUserDashboardInfo = userService.showUserDashboardInfo();
		showUserDashboardInfo .setUserAccountForm(byEmail);
		return new ResponseEntity<>(showUserDashboardInfo,HttpStatus.OK);
		
	}
	
	
	
	
	
	
}
