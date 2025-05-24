package com.example.admin.bindings;

import java.time.LocalDate;

import com.example.admin.exceptions.NotPastDate;
import com.example.admin.exceptions.ValidPlanDates;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@ValidPlanDates
public class PlanForm {

	private Integer id;
	@NotNull
	@Pattern(regexp = "^[A-Z]+$", message = "Plan name must contain only uppercase letters.")
    @Size(max = 8, min =4,message = "Plan name must be capital and minimum should enter 4 words")
	private String planName;
	
	@NotNull(message = "Plan start date is required")
	@NotPastDate
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate startDate;
	
	
	
	@NotNull(message = "Plan end date should be greater than 6 months")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate endDate;

    private String createByUser;
	
}
