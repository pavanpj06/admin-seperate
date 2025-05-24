package com.example.admin.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;



@Setter
@Getter
@Entity
@Table(name = "Plans_ies")
public class PlanEntity {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	
	private String planName;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate planStartDate;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate planEndDate;
	
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "createByUser",referencedColumnName = "id")
	@JsonIgnore
	private UserEntity user;
	
	
}
