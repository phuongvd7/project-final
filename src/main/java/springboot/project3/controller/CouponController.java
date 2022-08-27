package springboot.project3.controller;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import springboot.project3.entity.Coupon;
import springboot.project3.repository.CouponRepo;

@Controller
@RequestMapping("/coupon")
public class CouponController {
	
	private static Logger logger = LoggerFactory.getLogger(CouponController.class);
	
	@Autowired
	CouponRepo couponRepo;
	
	@GetMapping("/create")
	public String create(Model model) {
		model.addAttribute("coupon", new Coupon());
		return "coupon/create";
	}

	@PostMapping("/create") 
	public String create(@ModelAttribute("coupon") @Valid Coupon c,@RequestParam("expired_Date") String expiredDateStr , BindingResult bindingResult) 
	// cho requestparam de khac ten di de set lai date khong thi thang Modelattribute cu giong ten la se map het
			throws ParseException {

		if (bindingResult.hasErrors()) {
			return "coupon/create";
		}
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		c.setExpiredDate(simpleDateFormat.parse(expiredDateStr));
	//	c.setExpiredDate(new java.util.Date());
		couponRepo.save(c);
		return "redirect:/coupon/create";
	}
	@GetMapping("/delete")
	public String delete(@RequestParam("id") int id) {
		couponRepo.deleteById(id);
		return "redirect:/coupon/search";
	}
	
	@GetMapping("/search")
	public String search(Model model, @RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size,
			@RequestParam(name = "id", required = false) Integer id,
			@RequestParam(name = "sort", required = false) Integer sort,
			@RequestParam(name = "couponCode", required = false) String couponCode) {

		if (size == null)
			size = 3;// toi da ban ghi tren trang
		if (page == null)
			page = 0; // trang hien tai

		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		
		if(couponCode != null && !couponCode.isEmpty()) {
			Page<Coupon> pageCoupon = couponRepo.searchByCode("%" + couponCode + "%", pageable);
			model.addAttribute("list", pageCoupon.toList());
			model.addAttribute("totalPage", pageCoupon.getTotalPages());
		} else if (id != null) {
			Coupon coupon = couponRepo.findById(id).orElse(null);
			if (coupon != null) {
				model.addAttribute("list", Arrays.asList(coupon));
				model.addAttribute("totalPage", 0);
				 } else//
					 
					 model.addAttribute("totalPage", 0);

			} else {

				// if(id==null) {
				Page<Coupon> pageCoupon = couponRepo.findAll(pageable);
				model.addAttribute("list", pageCoupon.toList()); // tolist convert sang 1 list from page 
				model.addAttribute("totalPage", pageCoupon.getTotalPages());//totalPage de xem co bao nhieu sang => bien int
			}

		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("id", id == null ? "" : id);
		model.addAttribute("sort", sort == null ? "" : sort );
		model.addAttribute("couponCode", couponCode == null ? "" : couponCode);

		return "coupon/search";
	}

}