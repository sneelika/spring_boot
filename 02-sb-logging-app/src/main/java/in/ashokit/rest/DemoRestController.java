package in.ashokit.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoRestController {
	Logger logger = LoggerFactory.getLogger(DemoRestController.class);

	@GetMapping("/welcome")
	public String getWelcomeMsg() {
		logger.debug("getWelcomeMsg() Execution Started");
		String msg = "Welcome to Ashoit IT";
		logger.debug("getWelcomeMsg() Execution Finished");
		return msg;
	}

}
