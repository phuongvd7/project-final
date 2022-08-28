package springboot.project3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import springboot.project3.service.MailService;

@Controller

public class HomeController {
	@Autowired
	MailService mailService;
	@GetMapping("/home")
	public String home() {
	//	mailService.sendEmail("ducphuong170498@gmail.com", "test test", "test test");
		System.out.println("testmail");
		return "home.html";
	}
	@GetMapping("/dang-nhap") // ko dung duoc post mapping vi spring quan ly
	public String login() {
		return "login.html";
	}
	@GetMapping("/deny") 
	public String deny(Model model) {
		return "deny.html";
	}
}
