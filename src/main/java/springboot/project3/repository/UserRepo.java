package springboot.project3.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import springboot.project3.entity.User;


public interface UserRepo extends JpaRepository<User, Integer>{
	@Query("SELECT d FROM User d WHERE d.name LIKE :x")
	Page<User> search(@Param("x") String s, Pageable pageable);
	
	User findByUsername(String s);

	User findByEmail(String email);
}
