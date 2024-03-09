package com.springboot.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.binding.Child;
import com.springboot.binding.ChildRequest;
import com.springboot.binding.DcSummary;
import com.springboot.binding.Education;
import com.springboot.binding.Income;
import com.springboot.binding.PlanSelection;
import com.springboot.entity.CitizenAppEntity;
import com.springboot.entity.DcCaseEntity;
import com.springboot.entity.DcChildrenEntity;
import com.springboot.entity.DcEducationEntity;
import com.springboot.entity.DcIncomeEntity;
import com.springboot.entity.PlanEntity;
import com.springboot.repo.CitizenAppRepository;
import com.springboot.repo.DcCaseRepo;
import com.springboot.repo.DcChildrenRepo;
import com.springboot.repo.DcEducationRepo;
import com.springboot.repo.DcIncomeRepo;
import com.springboot.repo.PlanRepo;

@Service
public class DcServiceImpl implements DcService {

	@Autowired
	private DcCaseRepo dcCaseRepo;

	@Autowired
	private PlanRepo planRepo;

	@Autowired
	private DcIncomeRepo incomeRepo;

	@Autowired
	private DcEducationRepo educationRepo;

	@Autowired
	private DcChildrenRepo childrenRepo;

	@Autowired
	private CitizenAppRepository appRepo;

	@Override
	public Long loadCaseNum(Integer appId) {

		Optional<CitizenAppEntity> app = appRepo.findById(appId);

		if (app.isPresent()) {
			DcCaseEntity entity = new DcCaseEntity();
			entity.setAppId(appId);

			entity = dcCaseRepo.save(entity);
			return entity.getCaseNum();
		}
		return 0l;
	}

	@Override
	public Map<Integer, String> getPlanNames() {
		List<PlanEntity> findAll = planRepo.findAll();

		Map<Integer, String> plansMap = new HashMap<>();

		for (PlanEntity entity : findAll) {
			plansMap.put(entity.getPlanId(), entity.getPlanName());
		}

		return plansMap;
	}

	@Override
	public Long savePlanSelection(PlanSelection planSelection) {

		Optional<DcCaseEntity> findById = dcCaseRepo.findById(planSelection.getCaseNum());

		if (findById.isPresent()) {
			DcCaseEntity dcCaseEntity = findById.get();
			dcCaseEntity.setPlanId(planSelection.getPlanId());
			dcCaseRepo.save(dcCaseEntity);
			return planSelection.getCaseNum();
		}

		return null;
	}

	@Override
	public Long saveIncomeData(Income income) {
		DcIncomeEntity entity = new DcIncomeEntity();
		BeanUtils.copyProperties(income, entity);
		incomeRepo.save(entity);

		return income.getCaseNum();
	}

	@Override
	public Long saveEducation(Education education) {
		DcEducationEntity entity = new DcEducationEntity();

		BeanUtils.copyProperties(education, entity);

		educationRepo.save(entity);

		return education.getCaseNum();
	}

	@Override
	public Long saveChildren(ChildRequest request) {
		List<Child> childs = request.getChilds();
		Long caseNum = request.getCaseNum();

		for (Child c : childs) {
			DcChildrenEntity entity = new DcChildrenEntity();
			BeanUtils.copyProperties(c, entity);
			entity.setCaseNum(caseNum);
			childrenRepo.save(entity);
		}

		return request.getCaseNum();
	}

	@Override
	public DcSummary getSummary(Long caseNumber) {
		String planName = "";

		DcIncomeEntity incomeEntity = incomeRepo.findByCaseNum(caseNumber);
		DcEducationEntity educationEntity = educationRepo.findByCaseNum(caseNumber);
		List<DcChildrenEntity> childrenEntity = childrenRepo.findByCaseNum(caseNumber);
		Optional<DcCaseEntity> dcCase = dcCaseRepo.findById(caseNumber);

		if (dcCase.isPresent()) {
			Integer planId = dcCase.get().getPlanId();
			Optional<PlanEntity> plan = planRepo.findById(planId);
			if (plan.isPresent()) {
				planName = plan.get().getPlanName();
			}
		}

		// Set the data to summary obj

		DcSummary summary = new DcSummary();
		summary.setPlanName(planName);

		Income income = new Income();
		BeanUtils.copyProperties(incomeEntity, income);
		summary.setIncome(income);

		Education education = new Education();
		BeanUtils.copyProperties(educationEntity, education);
		summary.setEducation(education);

		List<Child> children = new ArrayList();
		for (DcChildrenEntity entity : childrenEntity) {
			Child ch = new Child();
			BeanUtils.copyProperties(entity, ch);
			children.add(ch);
		}
		summary.setChildren(children);
		return null;
	}

}
