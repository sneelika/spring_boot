package com.springboot.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.entity.Plan;

public interface PlanRepo extends JpaRepository<Plan, Integer> {

}
