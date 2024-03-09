package com.springboot.service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springboot.binding.EligResponse;
import com.springboot.entity.CitizenAppEntity;
import com.springboot.entity.CoTriggerEntity;
import com.springboot.entity.DcCaseEntity;
import com.springboot.entity.DcChildrenEntity;
import com.springboot.entity.DcEducationEntity;
import com.springboot.entity.DcIncomeEntity;
import com.springboot.entity.EligDtlsEntity;
import com.springboot.entity.PlanEntity;
import com.springboot.repo.CitizenAppRepository;
import com.springboot.repo.CoTriggerRepo;
import com.springboot.repo.DcCaseRepo;
import com.springboot.repo.DcChildrenRepo;
import com.springboot.repo.DcEducationRepo;
import com.springboot.repo.DcIncomeRepo;
import com.springboot.repo.EligDtlsRepo;
import com.springboot.repo.PlanRepo;

@Service
public class EligServiceImpl implements EligService {

	@Autowired
	private DcCaseRepo dcCaseRepo;

	@Autowired
	private PlanRepo planRepo;

	@Autowired
	private DcIncomeRepo incomeRepo;

	@Autowired
	private DcChildrenRepo childRepo;

	@Autowired
	private CitizenAppRepository appRepo;

	@Autowired
	private DcEducationRepo eduRepo;

	@Autowired
	private EligDtlsRepo eligRepo;

	@Autowired
	private CoTriggerRepo coTrgRepo;

	@Override
	public EligResponse determineEligibility(Long caseNum) {
		Optional<DcCaseEntity> caseEntity = dcCaseRepo.findById(caseNum);
		Integer planId = null;
		String planName = null;
		Integer appId = null;

		if (caseEntity.isPresent()) {
			DcCaseEntity dcCaseEntity = caseEntity.get();
			planId = dcCaseEntity.getPlanId();
			appId = dcCaseEntity.getAppId();
		}

		Optional<PlanEntity> planEntity = planRepo.findById(planId);
		if (planEntity.isPresent()) {
			PlanEntity plan = planEntity.get();
			planName = plan.getPlanName();
		}

		Optional<CitizenAppEntity> app = appRepo.findById(appId);
		Integer age = 0;
		CitizenAppEntity citizenAppEntity = null;
		if (app.isPresent()) {
			citizenAppEntity = app.get();
			LocalDate dob = citizenAppEntity.getDob();
			LocalDate now = LocalDate.now();
			age = Period.between(dob, now).getYears();

		}

		EligResponse eligResponse = executePlanConditions(caseNum, planName, age);

		EligDtlsEntity eligEntity = new EligDtlsEntity();
		BeanUtils.copyProperties(eligResponse, eligEntity);
		eligEntity.setCaseNum(caseNum);
		eligEntity.setHolderName(citizenAppEntity.getFullName());
		eligEntity.setHolderSsn(citizenAppEntity.getSsn());

		eligRepo.save(eligEntity);

		CoTriggerEntity coEntity = new CoTriggerEntity();
		coEntity.setCaseNum(caseNum);
		coEntity.setTrgStatus("Pending");

		coTrgRepo.save(coEntity);

		return eligResponse;
	}

	private EligResponse executePlanConditions(long caseNum, String planNam, Integer age) {
		EligResponse response = new EligResponse();
		response.setPlanName(planNam);
		DcIncomeEntity income = incomeRepo.findByCaseNum(caseNum);

		if ("SNAP".equals(planNam)) {
			Double empIncome = income.getEmpIncome();
			if (empIncome <= 300) {
				response.setPlanStatus("AP");
			} else {
				response.setPlanStatus("DN");
				response.setDenialReason("High Income");
			}

		} else if ("CCAP".equals(planNam)) {
			Boolean ageCondition = true;
			Boolean kidsCountCondition = false;
			List<DcChildrenEntity> children = childRepo.findByCaseNum(caseNum);
			if (!children.isEmpty()) {
				kidsCountCondition = true;
				for (DcChildrenEntity entity : children) {
					Integer childAge = entity.getChildAge();
					if (childAge > 16) {
						ageCondition = false;
						break;
					}
				}
			}

			if (income.getEmpIncome() <= 300 && kidsCountCondition && ageCondition) {
				response.setPlanStatus("AP");

			} else {
				response.setPlanStatus("DN");
				response.setDenialReason("Not satisfied business rules");
			}

		} else if ("Medicaid".equals(planNam)) {

			Double empIncome = income.getEmpIncome();
			Double propertyIncome = income.getPropertyIncome();

			if (empIncome <= 300 && propertyIncome == 0) {
				response.setPlanStatus("AP");
			} else {
				response.setPlanStatus("DN");
				response.setDenialReason("High Income");
			}

		} else if ("Medicare".equals(planNam)) {

			if (age >= 65) {
				response.setPlanStatus("AP");
			} else {
				response.setPlanStatus("DN");
				response.setDenialReason("Age not matched");
			}

		} else if ("NJW".equals(planNam))

		{
			DcEducationEntity educationEntity = eduRepo.findByCaseNum(caseNum);
			Integer graduationYear = educationEntity.getGraduationYear();
			int currYear = LocalDate.now().getYear();

			if (income.getEmpIncome() <= 0 && graduationYear < currYear) {
				response.setPlanStatus("AP");
			}
		} else {
			response.setPlanStatus("DN");
			response.setDenialReason("Rules not satisfied");
		}

		if (response.getPlanStatus().equals("AP")) {
			response.setPlanStartDate(LocalDate.now());
			response.setPlanEnDate(LocalDate.now().plusMonths(6));
			response.setBenefitAmt(350.00);
		}

		return response;
	}

}
