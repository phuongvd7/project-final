package springboot.project3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
//	@Value("${my.key}")
//	private String hello;
//	
	@Autowired
	private Environment env;

	@GetMapping({ "/2", "/hi" })
	public String hello() {
//		System.out.println(hello);
//		System.out.println(env.getProperty("my.key")); // show ra bang console
		return "hi"; // ten view file
	}
	@GetMapping({"/welcome"})
	public String welcome(){
		return "wel";
	}
	
}
