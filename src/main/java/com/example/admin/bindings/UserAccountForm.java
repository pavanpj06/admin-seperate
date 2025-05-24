package com.example.admin.bindings;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.admin.exceptions.ValidRole;

import enums.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserAccountForm {

	private Long id;
	@NotNull
	private String fullName;

    @Email
    @NotNull
    private String email;

    @Size(max = 10,min = 10,message = "Phone number should contains only 10 digits")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number should contain only digits")
    private String mobileNumber;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;


//    @Past
    @NotNull
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private String dateOfBirth;
    @NotNull(message = "SSN Number cannot be null")
    @Size( max = 8, message = "SSN Number not more than 8 numerics")
    @Pattern(regexp = "\\d+", message = "SS Number must contain only digits")
    private String ssNumber;


    
    
    @NotNull
    @ValidRole
    private Integer roleId;

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
