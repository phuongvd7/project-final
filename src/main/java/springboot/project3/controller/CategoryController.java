package springboot.project3.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import springboot.project3.entity.Category;
import springboot.project3.entity.User;
import springboot.project3.repository.CategoryRepo;



@Controller
@RequestMapping("/category")
public class CategoryController {	
	@Autowired // DI
	CategoryRepo categoryRepo;
	
	@GetMapping("/create")
//	@Secured(value = {"ROLE_ADMIN"})
	public String create(Model model, HttpServletRequest request) {
		
//		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//ham co vai tro bat nguoi dung dang nhap 1 lan. sau do chuyen nguoi dung qua muc tao don ,... thi no se giup minh luu thong tin nguoi mua luon	
//			System.out.println(currentUser.getUsername());
			if(request.isUserInRole("ADMIN")) { // ham de check role thang dang nhap hien tai 
				System.out.println("ADMIN");
			}
		model.addAttribute("category", new Category());
		return "category/create";
	}
	@PostMapping("/create")
	public String create(@ModelAttribute("category") @Valid Category category,@RequestParam(name="file",required=false) MultipartFile file, BindingResult bindingresult) {
		try {
		if (bindingresult.hasErrors()) {
			return "category/create";
		}
		//luu lai file vao 1 folder, sau do lay url save vao database
		if(file != null && file.getSize() > 0) {
			final String folder = "D:\\userfiles";
			String originFilename = file.getOriginalFilename();
			
			File newFile = new File(folder + "/" + originFilename);
			//copy noi dung file upload vao file new
			file.transferTo(newFile);
			//luu lai url file vao db
			category.setImageURL(originFilename);
		} 
		}catch (IOException e) {
			e.printStackTrace();
		}
		categoryRepo.save(category);
		return "redirect:/category/search";
	}
	
	
	@GetMapping("/download") // ?imageURL=12
	public void download(@RequestParam("imageURL") String imageURL, HttpServletResponse response) {
		final String folder = "D:\\userfiles";
		File file = new File(folder + "/" + imageURL);
		
		if(file.exists()) {
			try {
				Files.copy(file.toPath(), response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	@GetMapping("/update")
	public String update(@RequestParam("id") int id, Model model){ 
		Category category = categoryRepo.getById(id);
		model.addAttribute("category", category);
		return "category/update.html";
	}
	
	@PostMapping("/update")
	public String update(@ModelAttribute Category category) {
		categoryRepo.save(category);
		return "redirect:/category/search";
	}
	
	@GetMapping("/delete") // ?id=12
	public String delete(HttpServletRequest req, @RequestParam("id") int id) {
		categoryRepo.deleteById(id);
//		customerRepo.save(customerRepo.getById(id));
		return "redirect:/category/search";// url maping
	}
	
//	@GetMapping("/search")
//	public String search(Model model, @RequestParam(name = "keywordname", required = false) String s) {
//		if(s == null) {
//			List<Product> list = productRepo.findAll();
//			model.addAttribute("productList", list);
//		} else {
//			List<Product> list = productRepo.search("%" + s + "%");
//			model.addAttribute("productList", list);
//		}
//			
//	
//		return "product/search";


//@GetMapping("/search")
//public String search(Model model, @RequestParam(name = "name", required = false) String name,
//		@RequestParam(name = "id", required = false) Integer id,
//		@RequestParam(name = "page", required = false) Integer page,
//		@RequestParam(name = "size", required = false) Integer size) {
//	if (size == null)
//		size = 3;// max records per page
//	if (page == null)
//		page = 0;// trang hien tai
//
//	Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
//
//	if (name != null && !name.isEmpty()) {
//		Page<Category> pageProduct = categoryRepo.search("%" + name + "%", pageable);
//
//		model.addAttribute("list", pageProduct.toList());
//
//		model.addAttribute("totalPage", pageProduct.getTotalPages());
//		model.addAttribute("page", page);
//		model.addAttribute("size", size);
//		model.addAttribute("name", name);
//		model.addAttribute("id", id);
//	} else if (id != null) {
//		Category category = categoryRepo.findById(id).orElse(null);
//		if (category != null) {
//			model.addAttribute("list", Arrays.asList(category));
//
//		} else
//			// log
////			logger.info("Id not found");
//
//		model.addAttribute("totalPage", 1);
//		model.addAttribute("page", page);
//		model.addAttribute("size", size);
//		model.addAttribute("name", name);
//		model.addAttribute("id", id);
//	} else {
//		Page<Category> pageProduct = categoryRepo.findAll(pageable);
//		model.addAttribute("list", pageProduct.toList());
//		model.addAttribute("totalPage", pageProduct.getTotalPages());
//		model.addAttribute("page", page);
//		model.addAttribute("name", name == null ? "" : name);
//		model.addAttribute("size", size);
//		model.addAttribute("id", id == null ? "" : id);
//	}
//	return "category/search";
	
	@GetMapping("/search")
	public String search(Model model, @RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "id", required = false) Integer id,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) {
		if (size == null)
			size = 3; // max records per page
		if (page == null)
			page = 0; // trang hien tai

		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending()); // sap xep theo thu tu cot => theo cot id

		if (name != null && !name.isEmpty()) {
			Page<Category> pageCategory = categoryRepo.search("%" + name + "%", pageable);
			model.addAttribute("list", pageCategory.toList());
			model.addAttribute("totalPage", pageCategory.getTotalPages());
			
//ở đây có 3 page 0,1,2
		} else if (id != null) {
			Category category = categoryRepo.findById(id).orElse(null);
			if (category != null) {
				model.addAttribute("list", Arrays.asList(category));
				model.addAttribute("totalPage", 0);
				 } else//
					 
					 model.addAttribute("totalPage", 0);

			} else {

				// if(id==null) {
				Page<Category> pageCategory = categoryRepo.findAll(pageable);
				model.addAttribute("list", pageCategory.toList()); // tolist convert sang 1 list from page 
				model.addAttribute("totalPage", pageCategory.getTotalPages());//totalPage de xem co bao nhieu sang => bien int
//ở đây có 4 page 0,1,2,3,4
			}
			model.addAttribute("page", page);
			model.addAttribute("name", name == null ? "" : name);
			model.addAttribute("size", size);
			model.addAttribute("id", id == null ? "" : id);
			return "category/search";

		}
}	
