package com.springboot.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.entity.PlanEntity;

public interface PlanRepo extends JpaRepository<PlanEntity, Serializable> {

}