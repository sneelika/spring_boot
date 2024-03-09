package com.springboot.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "ELIGIBILITY_DTLS")
@Data
public class EligDtlsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer edTraceId;
	private Long caseNum;
	private String holderName;
	private String planName;
	private Long holderSsn;
	private String planStatus;
	private LocalDate planStartDate;
	private LocalDate planEnDate;
	private Double benefitAmt;
	private String denialReason;
}
