package com.springboot.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Data;

@Entity
@Table(name = "CITIZEN_APPS")
@Data
public class CitizenAppEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer appId;

	private String fullName;
	private String email;
	private Long phno;
	private String gender;
	private Long ssn;
	private String stateName;
	private LocalDate dob;
	@CreationTimestamp
	@Column(name = "create_date", updatable = false)
	private LocalDate createDate;
	@UpdateTimestamp
	private LocalDate updateDate;
	private String createdBy;
	private String updatedBy;

}
