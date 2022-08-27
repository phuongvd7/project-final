package springboot.project3.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;

@Entity
@Table(name = "coupon")
@Data
public class Coupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "user_id")
	private float discountAmount;
	@Column(unique=true)
	private String  couponCode ;
	
	private Date expiredDate;
}
