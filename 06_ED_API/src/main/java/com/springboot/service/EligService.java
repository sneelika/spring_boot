package com.springboot.service;

import com.springboot.binding.EligResponse;

public interface EligService {
	public EligResponse determineEligibility(Long caseNum);
}
