package springboot.project3.entity;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "\"user\"")
@Data
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String name;
	private String username;
	private String password;
	private String role;
	private String email;

// cach khong tao moi entity role ma chi can tao mang
	@ElementCollection(fetch = FetchType.EAGER)
	@Column(name = "role")
	@CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
	private List<String> roles;
}
