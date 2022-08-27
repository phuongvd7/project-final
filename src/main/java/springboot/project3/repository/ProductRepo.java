package springboot.project3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import springboot.project3.entity.Product;

public interface ProductRepo extends JpaRepository<Product,Integer>{

		// JQL
//	@Query("SELECT u FROM Product u WHERE u.name LIKE :x")
//	Page<Product> search(@Param("x") String s, Pageable pageable);
//	@Query("SELECT c FROM Product c WHERE c.name LIKE :x")
//	Page<Product> search(@Param("x") String s, Pageable pageable);
	
	
	@Query("SELECT l FROM Product l INNER JOIN Category c ON l.category.id = c.id WHERE c.name LIKE :name")
	Page<Product> searchByName(@Param("name") String name, Pageable pageable);
	@Query("SELECT l FROM Product l INNER JOIN Category c ON l.category.id = c.id WHERE c.id = :id")
	Page<Product> searchById(@Param("id") Integer id, Pageable pageable);
}