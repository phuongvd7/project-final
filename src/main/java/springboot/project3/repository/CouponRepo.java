package springboot.project3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import springboot.project3.entity.Coupon;


public interface CouponRepo extends JpaRepository<Coupon, Integer>{

//	@Query("SELECT l FROM Bill l INNER JOIN User u ON l.user.id = u.id WHERE u.name LIKE :name")
//	Page<Coupon> searchByName(@Param("name") String name, Pageable pageable);
//	@Query("SELECT l FROM Bill l INNER JOIN User u ON l.user.id = u.id WHERE u.id = :id")
//	Page<Coupon> searchById(@Param("id") int id, Pageable pageable);
	@Query("SELECT c FROM Coupon c where c.couponCode like :code")
	Page<Coupon> searchByCode(@Param("code") String code, Pageable pageable);
	
	@Query("SELECT c FROM Coupon c where upper(c.couponCode) = upper(:code)")
	Coupon findByCode(@Param("code") String code);
}
