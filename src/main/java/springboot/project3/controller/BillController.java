package springboot.project3.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Length;
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

import spring.project3.model.BillStat;
import spring.project3.model.BillStatCouponCode;
import spring.project3.model.BillStatUserName;
import springboot.project3.entity.Bill;
import springboot.project3.entity.Coupon;
import springboot.project3.entity.User;
import springboot.project3.repository.BillRepo;
import springboot.project3.repository.CouponRepo;
import springboot.project3.repository.UserRepo;
import springboot.project3.service.MailService;

@Controller
@RequestMapping("/bill")
public class BillController {
	private static Logger logger = LoggerFactory.getLogger(BillController.class);
	@Autowired
	BillRepo billRepo;
	@Autowired
	UserRepo userRepo; 
	@Autowired
	MailService mailService;
	
	@Autowired
	CouponRepo couponRepo;

	@GetMapping("/create")
	public String create(Model model) {
		List<User> listUser = userRepo.findAll();
		model.addAttribute("listUser", listUser);
		return "bill/create";
	}

	@PostMapping("/create")
	public String create( @ModelAttribute Bill bill,
			@RequestParam(name = "couponCode", required = false) String couponCode,
			@RequestParam("buy_Date") String buyDateStr, BindingResult bindingResult,
			@ModelAttribute User user) throws ParseException {
// mot model chi co 1 attribute thoi
		if (bindingResult.hasErrors()) {
			return "bill/create";
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
		bill.setBuyDate(simpleDateFormat.parse(buyDateStr));
		//lên lịch quét để gửi về email xem có đơn hàng
		//convert current Date to milisecond
		// long currentMilis = new Date().getTime();
		 //convert milis to Date
//		 Date date = new Date(currentMilis - 5 * 60 * 1000);
//		 List<Bill> billList = billRepo.searchByDate(date);
	//	 mailService.sendEmail(user.getEmail(), "don hang cua ban da duoc tao", "1 hoa don moi da duoc tao");
		 // 4. có thể xai luồng khác để tránh chờ đợi gửi email ở phía gmail. "manhn0194@gmail.com"
		 new Thread() {
				@Override
				public void run() {
					mailService.sendEmail(user.getEmail() , "Ban da co don hang moi", 
							"Don hang moi cua ban da duoc tao");
				}
			}.start();		
			
		 
	// check coupon co hay khong	 
		if(couponCode != null && !couponCode.isEmpty()) {
			Coupon c = couponRepo.findByCode(bill.getCouponCode());
			if(c != null) {
				if(c.getExpiredDate().after(bill.getBuyDate() ) ) {
					bill.setCouponCode(couponCode);
					bill.setDiscount(c.getDiscountAmount());
					}
				else {
					System.out.println("Coupon: " + c.getCouponCode() + " Expired date: " + c.getExpiredDate() + ": da qua han su dung ");
					}
			} else
				System.out.println("Coupon not found!");
		}
		
	
		billRepo.save(bill);
		
		return "redirect:/bill/search";
	}

	@GetMapping("/update")
	public String update(@ModelAttribute Bill bill,@RequestParam("id") int id, Model model) {
//		Bill bill = billRepo.getById(id);
//		model.addAttribute("listBill", bill);
//		List<User> listUsers = userRepo.findAll();
//		model.addAttribute("usersss", listUsers);
		
		model.addAttribute("listUser", userRepo.findAll());
		model.addAttribute("listBill", billRepo.getById(bill.getId()));
		return "bill/update.html";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute Bill bill, @RequestParam("user") int idUser) {
		bill.setUser(userRepo.getById(idUser));
		billRepo.save(bill);
		return "redirect:/bill/search";
	}

	@GetMapping("/delete") // ?id=12
	public String delete(@RequestParam("id") int id) {
		billRepo.deleteById(id);
		return "redirect:/bill/search";// url maping
	}


	@GetMapping("/search")
	public String search(Model model,
			@RequestParam(name = "userID", required = false) Integer userID,
			@RequestParam(name = "fromDate", required = false) String fromDate,
			@RequestParam(name = "toDate", required = false) String toDate,
			@RequestParam(name = "page", required = false) Integer page,
			@RequestParam(name = "size", required = false) Integer size) throws ParseException {
		List<User> user = userRepo.findAll();
		model.addAttribute("user",user);
		if (size == null)
			size = 3;

		if (page == null)
			page = 0;

		Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
		
		if(userID != null && fromDate != null && toDate != null && !fromDate.trim().isEmpty() && !toDate.trim().isEmpty()) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Page<Bill> pageBill = billRepo.search_User_fromDate_toDate(userID, sdf.parse(fromDate), sdf.parse(toDate), pageable);
			model.addAttribute("list",pageBill.toList()); // tra ve mang bill tu ham toList tu page bill
			model.addAttribute("totalPage",pageBill.getTotalPages()); // tra ve tong so trang de chia trang ben view
			
			
		} else if (userID == null && fromDate != null && toDate != null && !fromDate.trim().isEmpty() && !toDate.trim().isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Page<Bill> pageBill = billRepo.search_fromDate_toDate( sdf.parse(fromDate), sdf.parse(toDate), pageable);
			model.addAttribute("list",pageBill.toList()); 
			model.addAttribute("totalPage",pageBill.getTotalPages());
			
		} else if (userID == null && fromDate != null && !fromDate.trim().isEmpty() && toDate == "" ) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Page<Bill> pageBill = billRepo.searchByFrom( sdf.parse(fromDate), pageable);
			model.addAttribute("list",pageBill.toList()); 
			model.addAttribute("totalPage",pageBill.getTotalPages());
			
		} else if (userID == null && fromDate == "" && toDate != null && !toDate.trim().isEmpty()) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Page<Bill> pageBill = billRepo.searchByTo(sdf.parse(toDate), pageable);
			model.addAttribute("list", pageBill.toList());
			model.addAttribute("totalPage", pageBill.getTotalPages());
		} else if (userID != null && fromDate == "" && toDate == "") {

			Page<Bill> pageBill = billRepo.searchUser(userID, pageable);

			model.addAttribute("list", pageBill.toList());
			model.addAttribute("totalPage", pageBill.getTotalPages());
		} else {
			Page<Bill> pageBill = billRepo.findAll(pageable);

			model.addAttribute("list", pageBill.toList());
			model.addAttribute("totalPage", pageBill.getTotalPages());	
		}
		model.addAttribute("page",page);
		model.addAttribute("size",size);
		model.addAttribute("userID",userID == null ? "": userID);
		model.addAttribute("fromDate",fromDate == null ? "" : fromDate);
		model.addAttribute("toDate",toDate == null ? "" : toDate );
//		model.addAttribute("userID",userID );
//		model.addAttribute("fromDate",fromDate );
//		model.addAttribute("toDate",toDate);
		return "bill/search";
	}
//	@GetMapping("/thongKe")
//	public String thongKe(Model model) throws ParseException {
//		List<Object[]> list = billRepo.thongKeTheoThang();
//
//		List<BillStat> billStats = new ArrayList<BillStat>();
//		if (list != null && !list.isEmpty()) {
//			for (Object[] objects : list) {
//				BillStat billStat = new BillStat();
//				billStat.setSoLuong(Integer.parseInt((objects[0]).toString()));
//				billStat.setThang(Integer.parseInt((objects[1]).toString()));
//				billStats.add(billStat);
//			}
//		}
//		model.addAttribute("billStats", billStats);
//		return "bill/thongKe";
//	}
	@GetMapping("/thongKe")
	public void thongKe(Model model) throws ParseException {
	List<BillStat> list = billRepo.thongKeTheoThang();
           System.out.println(list.size());
	//		model.addAttribute("billStats", list);
	//		return "bill/thongKe";
	}
	
	@GetMapping("/thongKe2")
	public String thongKe2(Model model) throws ParseException {
		List<Object[]> list = billRepo.thongKeTheoUserName();

		List<BillStatUserName> billStats = new ArrayList<BillStatUserName>();
		if (list != null && !list.isEmpty()) {
			for (Object[] objects : list) {
				BillStatUserName billStat = new BillStatUserName();
				billStat.setSoLuong(Integer.parseInt((objects[0]).toString()));
				billStat.setUserName((objects[1]).toString());
				billStats.add(billStat);
			}
		}
		model.addAttribute("billStats", billStats);
		return "bill/thongKe2";
	}
	@GetMapping("/thongKe3")
	public String thongKe3(Model model) throws ParseException {
		List<Object[]> list = billRepo.thongKeTheoCouponCode();

		List<BillStatCouponCode> billStats = new ArrayList<BillStatCouponCode>();
		if (list != null && !list.isEmpty()) {
			for (Object[] objects : list) {
				BillStatCouponCode billStat = new BillStatCouponCode();
				billStat.setSoLuong(Integer.parseInt((objects[0]).toString()));
				
				billStat.setCouponCode((objects[1]).toString());
				billStats.add(billStat);
			}
		}

		model.addAttribute("billStats", billStats);
		return "bill/thongKe3";
	}
}
