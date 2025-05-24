package com.example.admin.bindings;

import enums.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EditUserAccount {

	
	private String fullName;
    private String mobileNumber;
    @NotNull(message = "Gender must be specified")
    private Gender gender;
  
	
	
}
