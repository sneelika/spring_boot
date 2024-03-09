package com.springboot.repo;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot.entity.CitizenAppEntity;

public interface CitizenAppRepository extends JpaRepository<CitizenAppEntity, Serializable> {

}
