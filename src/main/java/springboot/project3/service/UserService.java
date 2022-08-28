package springboot.project3.service;

import java.util.Arrays;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import spring.project3.model.UserDTO;
import springboot.project3.entity.User;
import springboot.project3.repository.UserRepo;

@Service
public class UserService {
	    @Autowired
	    private UserRepo userRepo; 
	    
	    @Transactional
	 //   @Override
	    public User registerNewUserAccount(UserDTO userDto) throws Exception  {
	    	        
	    	        if (emailExists(userDto.getEmail())) {   
	    	            throw new Exception(
	    	              "There is an account with that email address:  "
	    	              + userDto.getEmail());
	    	        }
	    	        User user = new User();    
	    	        user.setName(userDto.getName());
	    	        user.setUsername(userDto.getUsername());
	    	        user.setPassword(userDto.getPassword());
	    	        user.setEmail(userDto.getEmail());
	    	        user.setRoles(Arrays.asList("ROLE_MEMBER"));
	    	        return userRepo.save(user);       
	    	    }

	    	    private boolean emailExists(String email) {
	    	        return userRepo.findByEmail(email) != null;
	    	    }
	    	}
