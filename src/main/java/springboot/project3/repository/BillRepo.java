package springboot.project3.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import spring.project3.model.BillStat;
import springboot.project3.entity.Bill;


public interface BillRepo extends JpaRepository<Bill, Integer>{

	@Query("SELECT l FROM Bill l INNER JOIN User u ON l.user.id = u.id WHERE u.name LIKE :name")
	Page<Bill> searchByName(@Param("name") String name, Pageable pageable);
	@Query("SELECT l FROM Bill l INNER JOIN User u ON l.user.id = u.id WHERE u.id = :id")
	Page<Bill> searchById(@Param("id") int id, Pageable pageable);
	
//Page<Ticket> findByCustomerPhone(String phone, Pageable pageable);
//	
//	// JQL
//	@Query("SELECT u FROM Ticket u WHERE u.createdAt >= :from")
//	Page<Ticket> searchByFrom(@Param("from") Date from, Pageable pageable);
//
//	@Query("SELECT u FROM Ticket u WHERE u.createdAt <= :to")
//	Page<Ticket> searchByTo(@Param("to") Date to, Pageable pageable);
//	
//	@Query("SELECT u FROM Ticket u WHERE u.createdAt >= :from "
//			+ "AND u.createdAt <= :to")
//	Page<Ticket> searchByFromTo(@Param("from") Date from, 
//			@Param("to") Date to, Pageable pageable);
//	
//	@Query("SELECT u FROM Ticket u JOIN u.department d "
//			+ "WHERE d.id = :dId")
//	Page<Ticket> searchByDepartment(@Param("dId") int departmentId, Pageable pageable);
//	
//	@Query("SELECT u FROM Ticket u JOIN u.department d "
//			+ "WHERE d.id = :dId AND u.createdAt >= :from "
//			+ " AND u.createdAt <= :to AND u.customerPhone = :phone")
//	Page<Ticket> searchByAll(@Param("dId") int departmentId,
//			@Param("from") Date from, 
//			@Param("to") Date to,
//			@Param("phone") String phone,
//			Pageable pageable);
	
	@Query("SELECT b FROM Bill b JOIN b.user u WHERE u.id = :uID AND b.buyDate >= :from AND b.buyDate <= :to")
	Page<Bill> search_User_fromDate_toDate(@Param("uID") int s, @Param("from") Date from, @Param("to") Date to,
			Pageable pageable);

	@Query("SELECT b FROM Bill b WHERE b.buyDate >= :from AND b.buyDate <= :to")
	Page<Bill> search_fromDate_toDate(@Param("from") Date from, @Param("to") Date to, Pageable pageable);

	@Query("SELECT b FROM Bill b WHERE b.buyDate >= :from")
	Page<Bill> searchByFrom(@Param("from") Date from, Pageable pageable);

	@Query("SELECT b FROM Bill b WHERE b.buyDate <= :to")
	Page<Bill> searchByTo(@Param("to") Date to, Pageable pageable);
	
	@Query("SELECT b FROM Bill b JOIN b.user u WHERE u.id = :uID")
	Page<Bill> searchUser(@Param("uID") int userID, Pageable pageable);

//	@Query("SELECT b FROM Bill b WHERE b.buyDate >= :date")
//	List<Bill> searchBill(@Param("date") Date date);
	@Query("SELECT u FROM Bill u WHERE u.buyDate >= :date")
	List<Bill> searchByDate(@Param("date") Date date);

//	Bill findByCouponCode(String couponCode);

	// thong ke theo thang
	@Query("SELECT new  spring.project3.model.BillStat(count(b.id) as sl , MONTH(b.buyDate) as thang) FROM Bill b GROUP BY MONTH(b.buyDate)")
	List<BillStat> thongKeTheoThang();
	
//	@Query("SELECT count(b.id) as sl , MONTH(b.buyDate) as thang FROM Bill b GROUP BY MONTH(b.buyDate)")
//	List<BillStat> thongKeTheoThang();

	// thong ke theo ten nguoi mua
	@Query("SELECT count(b.id) as SoLuongDonMua , u.name AS UserName FROM Bill b JOIN b.user u WHERE u.id = b.user.id GROUP BY b.user.id")
	List<Object[]> thongKeTheoUserName();
//	@Query("SELECT count(b.id) as SoLuongDonMuaTheoCode , u.name AS UserName FROM Bill b JOIN b.user u WHERE u.id = b.user.id GROUP BY b.user.id")
//	List<Object[]> thongKeTheoCouponCode();
	
	@Query("SELECT count(b.id) as SL, b.couponCode FROM Bill b group by b.couponCode ")
	List<Object[]> thongKeTheoCouponCode();
}
