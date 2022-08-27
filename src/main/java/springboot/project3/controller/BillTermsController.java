package springboot.project3.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import springboot.project3.entity.BillTerms;
import springboot.project3.repository.BillRepo;
import springboot.project3.repository.BillTermsRepo;
import springboot.project3.repository.ProductRepo;


@Controller
@RequestMapping("/billterms")
public class BillTermsController {
	@Autowired
	BillRepo billRepo;
	@Autowired
	BillTermsRepo billTermsRepo;
	
	@Autowired
	ProductRepo productRepo;
	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("listBill", billRepo.findAll());
		model.addAttribute("listProduct", productRepo.findAll());
		return "billterms/create";
}
	@PostMapping("/create")
	public String create(@ModelAttribute BillTerms billTerms, @RequestParam("bill") int idBill,
			@RequestParam("product") int idProduct) {
		double price = productRepo.getById(idProduct).getPrice() * billTerms.getQuantity();
				
		billTerms.setPrice(price);
		billTerms.setBill(billRepo.getById(idBill));
		billTerms.setProduct(productRepo.getById(idProduct));
		billTermsRepo.save(billTerms);
		return "redirect:/billterms/search";
	}
	@GetMapping("/update")
	public String update(Model model, @ModelAttribute BillTerms billTerms ) {
		model.addAttribute("listBill", billRepo.findAll());
		model.addAttribute("listProduct", productRepo.findAll());
		model.addAttribute("billTerms", billTermsRepo.getById(billTerms.getId()));
		return "billterms/update";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute BillTerms billTerms, @RequestParam("bill") int billId,
			@RequestParam("product") int productId) {
		double price = productRepo.getById(productId).getPrice() * billTerms.getQuantity();
		
		billTerms.setPrice(price);
		billTerms.setBill(billRepo.getById(billId));
		billTerms.setProduct(productRepo.getById(productId));
		billTermsRepo.save(billTerms);
		return "redirect:/billterms/search";
	}
	@GetMapping("/search")
	public String search(Model model, @RequestParam(name = "id", required = false) Integer id,
			@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size,
			@RequestParam(name = "sort", required = false) Integer sort,
			@RequestParam(name = "billId", required = false) Integer billId) {

		if (size == null)
			size = 3;

		if (page == null)
			page = 0;
		
		if (sort == null) {
			Pageable pageable = PageRequest.of(page, size, Sort.by("bill").ascending());
			if (name != null && !name.isEmpty()) {
				Page<BillTerms> pageBillTerms = billTermsRepo.searchNameProduct("%" + name + "%", pageable);
				model.addAttribute("list", pageBillTerms.toList());
				model.addAttribute("totalPage", pageBillTerms.getTotalPages());
			} else if (id != null) {
				
//				BillTerms billTerms = billTermsRepo.findById(id).orElse(null);
//				if (billTerms != null) {
//					model.addAttribute("list", Arrays.asList(billTerms));
//				}
//				model.addAttribute("totalPage", 0);
				Page<BillTerms> pageBillTerms = billTermsRepo.searchByIdBill(id, pageable);
				model.addAttribute("list", pageBillTerms.toList());
				model.addAttribute("totalPage", pageBillTerms.getTotalPages());
				
			} else if (billId != null) {
				Page<BillTerms> pageBillTerms = billTermsRepo.searchByIdBill(billId, pageable);
				model.addAttribute("list", pageBillTerms.toList());
				model.addAttribute("totalPage", pageBillTerms.getTotalPages());
			} else {
				Page<BillTerms> pageBillTerms = billTermsRepo.findAll(pageable);
				model.addAttribute("list", pageBillTerms.toList());
				model.addAttribute("totalPage", pageBillTerms.getTotalPages());
			}

		} else if (sort == 1) {
			Pageable pageable = PageRequest.of(page, size, Sort.by("bill").ascending());
			if (name != null && !name.isEmpty()) {
				Page<BillTerms> pageBillTerms = billTermsRepo.searchNameProduct("%" + name + "%", pageable);
				model.addAttribute("list", pageBillTerms.toList());
				model.addAttribute("totalPage", pageBillTerms.getTotalPages());
			} else if (id != null) {
//				BillTerms billTerms = billTermsRepo.findById(id).orElse(null);
//				if (billTerms != null) {
//					model.addAttribute("list", Arrays.asList(billTerms));
//				}
//				model.addAttribute("totalPage", 0);
//				
				Page<BillTerms> pageBillTerms = billTermsRepo.searchByIdBill(id, pageable);
				model.addAttribute("list", pageBillTerms.toList());
				model.addAttribute("totalPage", pageBillTerms.getTotalPages());

			} else if (billId != null) {
				Page<BillTerms> pageBillTerms = billTermsRepo.searchByIdBill(billId, pageable);
			//	Page<BillTerms> pageBillTerms = billTermsRepo.findAll(pageable);
				model.addAttribute("list", pageBillTerms.toList());
				model.addAttribute("totalPage", pageBillTerms.getTotalPages());
			} else {
				Page<BillTerms> pageBillTerms = billTermsRepo.findAll(pageable);
				model.addAttribute("list", pageBillTerms.toList());
				model.addAttribute("totalPage", pageBillTerms.getTotalPages());
			}
		} else {
			Pageable pageable = PageRequest.of(page, size, Sort.by("product").ascending());
			Page<BillTerms> pageBillTerms = billTermsRepo.findAll(pageable);
			model.addAttribute("list", pageBillTerms.toList());
			model.addAttribute("totalPage", pageBillTerms.getTotalPages());
		}
		model.addAttribute("bill", billRepo.findAll());
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("name", name == null ? "" : name);
		model.addAttribute("id", id == null ? "" : id);
		model.addAttribute("sort", sort == null ? "" : sort);
		model.addAttribute("billId", billId == null ? "" : billId);
		return "billterms/search";
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("id") int id) {
		billTermsRepo.delete(billTermsRepo.getById(id));
		return "redirect:/billterms/search";
	}

	
}