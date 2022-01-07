package entities.dto;

import entities.User;

public class UserDetails {

	private String userName;
	private String email;
	
	public UserDetails() {
	}

	public UserDetails(User user) {
		this.userName = user.getUsername();
		this.email = user.getEmail();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
