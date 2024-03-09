package com.springboot.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.entity.CoTriggerEntity;

public interface CoTriggerRepo extends JpaRepository<CoTriggerEntity, Serializable> {
	public java.util.List<CoTriggerEntity> findByTrgStatus(String status);

	public CoTriggerEntity findByCaseNum(Long caseNum);
}