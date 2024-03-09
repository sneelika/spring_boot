package com.springboot.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.springboot.binding.CitizenApp;
import com.springboot.entity.CitizenAppEntity;
import com.springboot.repo.CitizenAppRepository;

@Service
public class ArServiceImpl implements ArService {

	@Autowired
	private CitizenAppRepository appRepo;

	@Override
	public Integer createApplication(CitizenApp app) {
		String endpointUrl = "https://ssa-web-api.herokuapp.com/ssn/{ssn}";
//		RestTemplate rt = new RestTemplate();
//
//		ResponseEntity<String> resEntity = rt.getForEntity(endpointUrl, String.class, app.getSsn());
//		String stateName = resEntity.getBody();

		WebClient webClient = WebClient.create();

		String stateName = webClient.get().uri(endpointUrl).retrieve().bodyToMono(String.class).block();

		if ("New Jersey".equals(stateName)) {
			CitizenAppEntity entity = new CitizenAppEntity();
			BeanUtils.copyProperties(app, entity);
			entity.setStateName(stateName);
			CitizenAppEntity save = appRepo.save(entity);

			return save.getAppId();
		}

		return 0;
	}

}