package spring.project3.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserDTO {
    @NotNull
    @NotEmpty
    private String name;
    
    @NotNull
    @NotEmpty
    private String username;
    
    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;
    
    @NotNull
    @NotEmpty
    private String email;
    
}