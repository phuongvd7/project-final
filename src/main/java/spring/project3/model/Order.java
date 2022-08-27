package spring.project3.model;

import java.util.List;

import springboot.project3.entity.BillTerms;

public class Order {
	private List<BillTerms> billTerms;
	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	private double total;
	public List<BillTerms> getBillTerms() {
		return billTerms;
	}

	public void setBillTerms(List<BillTerms> billTerms) {
		this.billTerms = billTerms;
	}
}
