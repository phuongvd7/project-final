package springboot.project3.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
@Data
@Entity
@Table(name = "category")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@NotEmpty(message = "{ValidationMessages.properties}")
	@Size(min=3, message  = "fail")
	private String name;
	
	@JsonIgnore // de tranh loop render json, chi chon 1 ben
	@OneToMany(mappedBy = "category", fetch = FetchType.LAZY) // eager lay ca bang, con lazy chi lay cot ra thoi
	private List<Product> products;

	private String imageURL;

	
}