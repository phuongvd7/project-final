package springboot.project3.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import spring.project3.model.MessageDTO;
import spring.project3.model.UserDTO;
import springboot.project3.RandomPassword;
import springboot.project3.entity.User;
import springboot.project3.repository.UserRepo;
import springboot.project3.service.MailService;
import springboot.project3.service.UserService;


@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserRepo userRepo;
	@Autowired
	MailService mailService;
	@Autowired
	RandomPassword random;
	
	@Autowired
	UserService userService;
	@GetMapping("/registration")
	public String showRegistrationForm(WebRequest request, Model model) {
	    UserDTO userDto = new UserDTO();
	    model.addAttribute("user", userDto);
	    return "user/registration";
	}
	@PostMapping("/registration")
	public String registerUserAccount(
	  @ModelAttribute("user") @Valid UserDTO userDto, 
	  HttpServletRequest request, Errors errors) { 
	    
	    try {
	        User registered = userService.registerNewUserAccount(userDto);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }

	    return  "user/successRegister";
	}
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("user", new User());
		return "user/create";
	}

	@PostMapping("/create")
	public String create(@ModelAttribute("user") @Valid User user,BindingResult bindingresult) {
		if(bindingresult.hasErrors()) {
			return "user/create";
		} 
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		userRepo.save(user);
		return "redirect:/user/search";
	}

	@GetMapping("/update")
	public String update(@RequestParam("id") int id, Model model,@ModelAttribute User user1) {
		User user = userRepo.getById(id);
		model.addAttribute("user", user);
		return "user/update";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute User user) {
//		User oldOne = userRepo.getById(user.getId());
//		
//
//		oldOne.setName(user.getName());
//		oldOne.setPassword(user.getPassword());
//		oldOne.setRole(user.getRole());
//		oldOne.setUsername(user.getUsername());
//		userRepo.save(oldOne); // khi test bang cach nay lai ko change dc role @@
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		userRepo.save(user); // cach nay thi ok
		return "redirect:/user/search";
	}

	@GetMapping("/delete")
	public String delete( @RequestParam("id") int id) {
		
		userRepo.deleteById(id); 

		return "redirect:/user/search";
	}

	
	@GetMapping("/forget-password")
	public String forgetPassword() {
		return "user/forget-password";
	}
	
//	@PostMapping("/forget-password")
////	public String forgetPassword(@RequestParam(name = "email" String email, Model model)) throws Exception {
//		User user = userRepo.findByEmail(email);
//		
//		return "user/forget-password";
//	}
	@PostMapping("/forget-password")
	public String forgetPass(@RequestParam(name = "email") String email, Model model) throws Exception {
		
		User user = userRepo.findByEmail(email);	
		MessageDTO messageDTO = new MessageDTO();
		
		if (StringUtils.hasText(email)) {
			
			if (user != null) {
				String newPassword = random.randomPassword(6);
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
				user.setPassword(passwordEncoder.encode(newPassword));
				messageDTO.setContent(newPassword);
				userRepo.save(user);
				
				messageDTO.setToName(user.getName());
				messageDTO.setTo(user.getEmail());
				
				mailService.sendEmailPassword(messageDTO);
				
			} else {
				throw new Exception("CANT FIND USER WITH EMAIL: " + email) ;
			}
			

		} else new Exception("Enter Your Email");
		
		model.addAttribute("u", user);
		
		return "user/change-password-success.html";

	}
	
	@GetMapping("/search")
	public String search(Model model, @RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "id", required = false) Integer id,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) {

		if (size == null)
			size = 3;// max records per page
		if (page == null)
			page = 0;// trang hien tai

		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

		if (name != null && !name.isEmpty()) {
			Page<User> pageUser = userRepo.search("%" + name + "%", pageable);
			model.addAttribute("list", pageUser.toList());
			model.addAttribute("totalPage", pageUser.getTotalPages());
			model.addAttribute("page", page);
			model.addAttribute("size", size);
			model.addAttribute("name", name == null ? "" : name);
			model.addAttribute("id", id == null ? "" : id);
		} else if (id != null) {
			User user = userRepo.findById(id).orElse(null);
//			if (user != null) {
			model.addAttribute("list", Arrays.asList(user));
			model.addAttribute("page", page);
			model.addAttribute("size", size);
			model.addAttribute("name", name == null ? "" : name);
			model.addAttribute("id", id == null ? "" : id);
//			} else
			// log
			// logger.info("Id not found");
			model.addAttribute("totalPage", 0);
		} else {
			Page<User> pageUser = userRepo.findAll(pageable);
			model.addAttribute("list", pageUser.toList());
			model.addAttribute("totalPage", pageUser.getTotalPages());
			model.addAttribute("page", page);
			model.addAttribute("size", size);
			model.addAttribute("name", name == null ? "" : name);
			model.addAttribute("id", id == null ? "" : id);
		}

		return "user/search";
	}
}
