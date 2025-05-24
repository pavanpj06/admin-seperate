package com.example.admin.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.admin.bindings.DashboardData;
import com.example.admin.bindings.LogInForm;
import com.example.admin.bindings.UnLockAccountForm;
import com.example.admin.bindings.UserAccountForm;
import com.example.admin.entities.EligEntity;
import com.example.admin.entities.UserEntity;
import com.example.admin.reposities.EligRepo;
import com.example.admin.reposities.PlanRepo;
import com.example.admin.reposities.UserRepo;
import com.example.admin.utils.EmailUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private EmailUtils emailUtils;

	@Autowired
	private PlanRepo planRepository;

	@Autowired
	private UserRepo userRepository;

	@Autowired
	private EligRepo eligRepository;

	@Autowired
	private EmailUtils utils;

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Override
	public String loginAccount(LogInForm logInForm) {
		
	
		Optional<UserEntity> byEmail = userRepository.findByEmail(logInForm.getEmail());
		
		if(byEmail.isPresent()) {
			 UserEntity user = byEmail.get();
			if (!passwordEncoder.matches(logInForm.getPassword(), user.getPassword())) {
		        return "Invalid password";
		    }
			if ("Y".equals(user.getAccountSw()) && "UN-Locked".equals(user.getAccountStatus()))

				return "Success";
			else
				return "Account Locked/In-Active";  
		    
		}else {
			return "Invalid email";
		}


		

		
	}

	@Override
	public boolean revoverPwd(String email) {
		Optional<UserEntity> byEmail = userRepository.findByEmail(email);

		if (byEmail == null)

			return false;
		else {

			String subject = "";

			String body = "";
//			return emailUtils.emailSending(byEmail.getEmail(), subject, body);
			return true;
		}

	}

	@Override
	public DashboardData showUserDashboardInfo() {
		long count = planRepository.count();

		List<EligEntity> all = eligRepository.findAll();

		long approvedCount = all.stream().filter(ed -> ed.getCitizedStatus().equals("Approved")).count();
		long deniedCount = all.stream().filter(pe -> pe.getCitizedStatus().equals("Denied")).count();

		
		double totalBenefitAmount = all.stream()
			    .mapToDouble(EligEntity::getBenefitAmount)
			    .sum();


		
		
		
		DashboardData dbd = new DashboardData();
		dbd.setPlansCnt(count);

		dbd.setBenefitsGivenAmt(totalBenefitAmount);
		dbd.setCitizenApproveCnt(approvedCount);
		dbd.setCitizenDeniedCnt(deniedCount);
		return dbd;
	}

	@Override
	public String unLockTheAccount(UnLockAccountForm uncLockAcct) {

		return null;
	}

	@Override
	public UserAccountForm getByEmail(String email) {
		Optional<UserEntity> byEmail = userRepository.findByEmail(email);
		
		UserAccountForm user=new UserAccountForm();
//		user.setEmail(byEmail.getEmail().get());
		return user;
	}

}
