package com.springboot.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.springboot.entity.EligibilityDetails;
import com.springboot.repo.EligibilityDetailsRepo;

@Component
public class AppRunner implements ApplicationRunner {

	@Autowired
	private EligibilityDetailsRepo repo;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		EligibilityDetails entity1 = new EligibilityDetails();
		entity1.setEligIdId(1);
		entity1.setName("John");
		entity1.setMobile(1234567869l);
		entity1.setGender('M');
		entity1.setSsn(686868686l);
		entity1.setPlanName("SNAP1");
		entity1.setPlanStatus("Approved");
		repo.save(entity1);

		EligibilityDetails entity2 = new EligibilityDetails();
		entity2.setEligIdId(2);
		entity2.setName("Sayuru");
		entity2.setMobile(123452267869l);
		entity2.setGender('F');
		entity2.setSsn(6868685686l);
		entity2.setPlanName("SNAP2");
		entity2.setPlanStatus("Not Approved");
		repo.save(entity2);
	}

}
