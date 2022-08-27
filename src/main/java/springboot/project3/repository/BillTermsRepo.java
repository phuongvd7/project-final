package springboot.project3.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import springboot.project3.entity.BillTerms;


public interface BillTermsRepo extends JpaRepository<BillTerms, Integer> {

	@Query("SELECT l FROM BillTerms l INNER JOIN Product c ON l.product.id = c.id WHERE c.name LIKE :name")
	
//	@Query("SELECT l FROM BillTerms l  JOIN l.product p  WHERE p.name LIKE :name")
	Page<BillTerms> searchNameProduct(@Param("name") String name, Pageable pageable);
	
	
//	@Query("SELECT l FROM BillTerms l INNER JOIN Product c ON l.bill.id = c.id WHERE c.id = :id")
//	Page<BillTerms> searchByIdProduct(@Param("id") int id, Pageable pageable);
	
	
	
	
	
	@Query("SELECT l FROM BillTerms l  JOIN  l.bill c WHERE c.id = :id")
//	@Query("SELECT l FROM BillTerms l INNER JOIN Bill c ON l.id = c.id WHERE c.id = :id")
	Page<BillTerms> searchByIdBill(@Param("id") int id, Pageable pageable);
	@Query("SELECT l FROM BillTerms l  JOIN Bill c ON l.id = c.id WHERE c.id = :id")
	List<BillTerms> searchByIdBill02(@Param("id") int id);
}
