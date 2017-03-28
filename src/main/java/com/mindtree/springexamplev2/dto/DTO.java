package com.mindtree.springexamplev2.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class DTO {
	private int id;
	// @NotNull(message = "Name can't be empty")
	private String name;
	// @NotNull(message = "LastName can't be empty")
	private String lastname;

	/*private String filePath;*/

	/*public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
*/
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Email(message = "Enter valid Email id")
	private String email;
	// @NotNull(message = "Password can't be empty")
	private String password;
	// @NotNull(message = "Password can't be empty")
	private String confirmPassword;

	public DTO(int id, String name, String lastname, String email, String password, String confirmPassword) {
		super();
		this.id = id;
		this.name = name;
		this.lastname = lastname;
		this.email = email;
		this.password = password;
		this.confirmPassword = confirmPassword;
	}

	public DTO() {
		super();
		// TODO Auto-generated constructor stub
	}
}
