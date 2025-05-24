package com.example.admin.bindings;

import lombok.Data;

@Data
public class UnLockAccountForm {

	private String tempPassword;
	private String email;
	private String confirmPassword;
}
