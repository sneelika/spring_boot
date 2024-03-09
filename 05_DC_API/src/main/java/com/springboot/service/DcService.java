package com.springboot.service;

import java.util.Map;

import com.springboot.binding.ChildRequest;
import com.springboot.binding.DcSummary;
import com.springboot.binding.Education;
import com.springboot.binding.Income;
import com.springboot.binding.PlanSelection;

public interface DcService {

	public Long loadCaseNum(Integer appId);

	public Map<Integer, String> getPlanNames();

	public Long savePlanSelection(PlanSelection planSelection);

	public Long saveIncomeData(Income income);

	public Long saveEducation(Education education);

	public Long saveChildren(ChildRequest request);

	public DcSummary getSummary(Long caseNumber);

}