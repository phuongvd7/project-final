package springboot.project3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import springboot.project3.entity.Category;

public interface CategoryRepo extends JpaRepository<Category,Integer>{

		// JQL
//	@Query("SELECT u FROM Product u WHERE u.name LIKE :x")
//	Page<Product> search(@Param("x") String s, Pageable pageable);
	@Query("SELECT c FROM Category c WHERE c.name LIKE :x")
	Page<Category> search(@Param("x") String s, Pageable pageable);
//	List<Category> search(@Param("x") String s);
}