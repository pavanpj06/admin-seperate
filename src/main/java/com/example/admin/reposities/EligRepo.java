package com.example.admin.reposities;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.admin.entities.EligEntity;

public interface EligRepo extends JpaRepository<EligEntity, Serializable> {

}
