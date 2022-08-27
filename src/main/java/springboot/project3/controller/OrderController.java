package springboot.project3.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import spring.project3.model.Order;
import springboot.project3.entity.Bill;
import springboot.project3.entity.BillTerms;
import springboot.project3.entity.Category;
import springboot.project3.entity.Product;
import springboot.project3.entity.User;
import springboot.project3.repository.BillTermsRepo;
import springboot.project3.repository.ProductRepo;

@Controller
public class OrderController {
	@Autowired
	BillTermsRepo billtermsRepo;

	@Autowired
	ProductRepo productRepo;

	@GetMapping("/add-to-cart/{productId}")
	public String addToCart(@PathVariable("productId") int productId, HttpServletRequest request, HttpSession session,
			Model model) {
		Product product = productRepo.getById(productId);

		// session.getAttribute("cart")
		if (session.getAttribute("cart") == null) {
			// Bill bill = new Bill();
			Order order = new Order();
			BillTerms billTerms = new BillTerms();
			billTerms.setQuantity(1);
			billTerms.setProduct(product);
			double price = productRepo.getById(productId).getPrice() * billTerms.getQuantity();
			billTerms.setPrice(price);
			List<BillTerms> billTermsInfo = new ArrayList<BillTerms>();
			billTermsInfo.add(billTerms);

			order.setBillTerms(billTermsInfo);

			double tong = 0;
			for (BillTerms b : billTermsInfo) {
				tong += b.getPrice();
			}
			order.setTotal(tong);
			session.setAttribute("cart", order);
			model.addAttribute("order", order);
		} else {
			Order order = (Order) session.getAttribute("cart");
			List<BillTerms> billTermsInfo = order.getBillTerms();
			boolean flag = false;
			for (BillTerms billTerms : billTermsInfo) {
				if (billTerms.getProduct().getId() == product.getId()) {
					billTerms.setQuantity(billTerms.getQuantity() + 1);
					double price = productRepo.getById(productId).getPrice() * billTerms.getQuantity();
					billTerms.setPrice(price);

					// order.setTotal(productId)
					flag = true;
				}
			}

			if (!flag) {
				// neu trong gio hang chua co san pham nay
				BillTerms billTerms = new BillTerms();
				billTerms.setQuantity(1);
				billTerms.setProduct(product);
				double price = productRepo.getById(productId).getPrice() * billTerms.getQuantity();
				billTerms.setPrice(price);
				billTermsInfo.add(billTerms);
			}
			double tong = 0;
			for (BillTerms b : billTermsInfo) {
				tong += b.getPrice();
			}
			order.setTotal(tong);

			session.setAttribute("cart", order);
			model.addAttribute("order", order);
		}

		return "cart/viewcart";
	}

	@GetMapping("/xoa-gio-hang/{productId}")
	public String deleteCart(@PathVariable("productId") int productId, HttpSession session, Model model) {
		if (session.getAttribute("cart") != null) {
			Order order = (Order) session.getAttribute("cart");
			List<BillTerms> billTermsInfo = order.getBillTerms();

			Iterator<BillTerms> itr = billTermsInfo.iterator();

			while (itr.hasNext()) {
				if (itr.next().getProduct().getId() == productId) {
					itr.remove();
				}
			}

			double tong = 0;
			for (BillTerms b : billTermsInfo) {
				tong += b.getPrice();
			}
			order.setTotal(tong);

			if (billTermsInfo.isEmpty()) {
				order.setTotal(0);
				session.removeAttribute("cart");
			}

//					session.setAttribute("cart", order);
			model.addAttribute("order", order);
		}
		return "cart/viewcart";
	}

	@GetMapping("/update-gio-hang/{productId}")
	public String updateCart(@PathVariable("productId") int productId, HttpSession session, Model model) {
		if (session.getAttribute("cart") != null) {
			Order order = (Order) session.getAttribute("cart");
			List<BillTerms> billTermsInfo = order.getBillTerms();
			BillTerms bts = new BillTerms();

			for (BillTerms billTerms : billTermsInfo) {
				if (billTerms.getProduct().getId() == productId) {
					bts = billTerms;
				}
				break;
			}
		
			model.addAttribute("order", bts);

			
		
	}
		return "cart/update.html";
	}

	@PostMapping("/update-gio-hang")
	public String update(@RequestParam("quantity") int quantity, @RequestParam("id") int productId, HttpSession session,
			Model model) {
		Order order = (Order) session.getAttribute("cart");
		List<BillTerms> billTermsInfo = order.getBillTerms();
		for (BillTerms billTerms : billTermsInfo) {
			if (billTerms.getProduct().getId() == productId) {
				billTerms.setQuantity(quantity);
				double price = productRepo.getById(productId).getPrice() * billTerms.getQuantity();
				billTerms.setPrice(price);

			}
		}
		double tong = 0;
		for (BillTerms b : billTermsInfo) {
			tong += b.getPrice();
		}
		order.setTotal(tong);
		session.setAttribute("cart", order);
		model.addAttribute("order", order);
		return "cart/viewcart";
	}

	@GetMapping("/xem-gio-hang")
	public String viewCart(HttpSession session, HttpServletRequest request, Model model) {
		if (session.getAttribute("cart") != null) {
			Order order = (Order) session.getAttribute("cart");
			model.addAttribute("order", order);
		}
		return "cart/viewcart";
	}
	
	

}
