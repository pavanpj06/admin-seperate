package com.example.admin.bindings;

import lombok.Data;

@Data
public class DashboardData {

	
	
	private Long plansCnt;
	
	private Long citizenApproveCnt;
	
	private Long citizenDeniedCnt;
	
	private Double benefitsGivenAmt;
	
	
	private UserAccountForm userAccountForm;
	
}
