package com.example.admin.entities;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;

import enums.Gender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "User_Creation_Entity_ies")
public class UserEntity {

//	Note: If you use @Data anotation for entity classes, toStirng method will also come and override then stack overflow exception will come
	//You can write @Data for Binding classes.
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "user_id")
    private Long id;

    private String fullName;

//    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
//    private PlanEntity planentity;
    
    @Email
    @Column(unique = true)
    private String email;

    @Size(max = 10,min = 10,message = "Phone number should contains only 10 digits")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number should contain only digits")
    private String mobileNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;


    private String dateOfBirth;
    
    private String accountStatus;

    private String ssNumber;

    private String accountSw;
    
    
    private String password;
    
    private Integer roleId;
    
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime creationDateAndTime;


    @UpdateTimestamp
    @Column(insertable = false)
    private LocalDateTime updationDateAndTime;

	@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
	private List<PlanEntity> ent;
	
	@CreatedBy
	private String createdBy;
	//jsonignore, backreference
	
	
	
}
