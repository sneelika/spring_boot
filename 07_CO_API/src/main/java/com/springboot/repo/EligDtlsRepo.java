package com.springboot.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.entity.EligDtlsEntity;

public interface EligDtlsRepo extends JpaRepository<EligDtlsEntity, Serializable> {
	public EligDtlsEntity findByCaseNum(Long caseNum);
}
