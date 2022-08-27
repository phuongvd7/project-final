package springboot.project3.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "training.io.demospringboot.controller")
public class ExceptionController {
	private static Logger logger = LoggerFactory.getLogger(ExceptionController.class);

	@ExceptionHandler({ Exception.class })
	public String error(Exception e) {
		logger.error(e.getMessage());
		return "error";
	}

}
