package springboot.project3.controller;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
//import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import springboot.project3.entity.Product;
import springboot.project3.repository.CategoryRepo;
import springboot.project3.repository.ProductRepo;



@Controller
@RequestMapping("/product")
public class ProductController {	
	@Autowired // DI
	ProductRepo productRepo;
	
	@Autowired
	CategoryRepo categoryRepo;
	
	@GetMapping("/create")
//	@Secured(value = {"ROLE_ADMIN"})
	public String create(Model model) {
		model.addAttribute("listCategory",categoryRepo.findAll());
		return "product/create";
}
	@PostMapping("/create")
//	@Secured(value = {"ROLE_ADMIN"})
	public String create(@ModelAttribute Product product,@RequestParam("category") int idCategory) {
		
		product.setCategory(categoryRepo.getById(idCategory));
		productRepo.save(product);
		return "redirect:/product/search";
	}
	
	
	
	@GetMapping("/update")
	public String update(@ModelAttribute Product product, Model model){ 
		model.addAttribute("listCategory", categoryRepo.findAll());
		model.addAttribute("listProduct", productRepo.getById(product.getId()));
		return "product/update.html";
	}
	
	@PostMapping("/update")
	public String update(@ModelAttribute Product product,@RequestParam("category") int idCategory) {
		product.setCategory(categoryRepo.getById(idCategory));
		productRepo.save(product);
		
		return "redirect:/product/search";
	}
	
	@GetMapping("/delete") // ?id=12
	public String delete(HttpServletRequest req, @RequestParam("id") int id) {
		productRepo.deleteById(id);
//		customerRepo.save(customerRepo.getById(id));
		return "redirect:/product/search";// url maping
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


//	@GetMapping("/search")
//	public String search(Model model, @RequestParam(name = "id", required = false) Integer id,
//			@RequestParam(name = "name", required = false) String name,
//			@RequestParam(name = "page", required = false) Integer page,
//			@RequestParam(name = "size", required = false) Integer size,
//			@RequestParam(name = "sort", required = false) Integer sort,
//			@RequestParam(name = "categoryId", required = false) Integer categoryId) {
//	if (size == null)
//		size = 3;// max records per page
//	if (page == null)
//		page = 0;// trang hien tai
//
//	Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
//
//	if (name != null && !name.isEmpty()) {
//		Page<Product> pageProduct = productRepo.search("%" + name + "%", pageable);
//
//		model.addAttribute("list", pageProduct.toList());
//
//		model.addAttribute("totalPage", pageProduct.getTotalPages());
//		model.addAttribute("page", page);
//		model.addAttribute("size", size);
//		model.addAttribute("name", name);
//		model.addAttribute("id", id);
//	} else if (id != null) {
//		Product product = productRepo.findById(id).orElse(null);
//		if (product != null) {
//			model.addAttribute("list", Arrays.asList(product));
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
//		Page<Product> pageProduct = productRepo.findAll(pageable);
//
//		model.addAttribute("list", pageProduct.toList());
//
//		model.addAttribute("totalPage", pageProduct.getTotalPages());
//		model.addAttribute("page", page);
//		model.addAttribute("size", size);
//		model.addAttribute("name", name);
//		model.addAttribute("id", id);
//	}
//	return "product/search";
//}	
	@GetMapping("/search")
//	@Secured(value = {"ROLE_ADMIN"})
	public String search(Model model, @RequestParam(name = "id", required = false) Integer id,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size,
			@RequestParam(name = "sort", required = false) Integer sort,
			@RequestParam(name = "categoryId", required = false) Integer categoryId) {

		if (size == null)
			size = 3;

		if (page == null)
			page = 0;
		
		if (sort == null) {
			Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
			if (name != null && !name.isEmpty()) {
				Page<Product> pageProduct = productRepo.searchByName("%" + name + "%", pageable);
				model.addAttribute("list", pageProduct.toList());
				model.addAttribute("totalPage", pageProduct.getTotalPages());
			} else if (id != null) {
				Product product = productRepo.findById(id).orElse(null);
				if (product != null) {
					model.addAttribute("list", Arrays.asList(product));
				}
				model.addAttribute("totalPage", 0);

			} else if (categoryId != null) {
				Page<Product> pageProduct = productRepo.searchById(categoryId, pageable);
				model.addAttribute("list", pageProduct.toList());
				model.addAttribute("totalPage", pageProduct.getTotalPages());
			} else {
				Page<Product> pageProduct = productRepo.findAll(pageable);
				model.addAttribute("list", pageProduct.toList());
				model.addAttribute("totalPage", pageProduct.getTotalPages());
			}

		} else if (sort == 1) {
			Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
			if (name != null && !name.isEmpty()) {
				Page<Product> pageProduct = productRepo.searchByName("%" + name + "%", pageable);
				model.addAttribute("list", pageProduct.toList());
				model.addAttribute("totalPage", pageProduct.getTotalPages());
			} else if (id != null) {
				Product product = productRepo.findById(id).orElse(null);
				if (product != null) {
					model.addAttribute("list", Arrays.asList(product));
				}
				model.addAttribute("totalPage", 0);

			} else if (categoryId != null) {
				Page<Product> pageProduct = productRepo.searchById(categoryId, pageable);
				model.addAttribute("list", pageProduct.toList());
				model.addAttribute("totalPage", pageProduct.getTotalPages());
			} else {
				Page<Product> pageProduct = productRepo.findAll(pageable);
				model.addAttribute("list", pageProduct.toList());
				model.addAttribute("totalPage", pageProduct.getTotalPages());
			}
		} 
		model.addAttribute("category", categoryRepo.findAll());
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("name", name == null ? "" : name);
		model.addAttribute("id", id == null ? "" : id);
		model.addAttribute("sort", sort == null ? "" : sort);
		model.addAttribute("categoryId", categoryId == null ? "" : categoryId);

		return "product/search";
	}
	
}