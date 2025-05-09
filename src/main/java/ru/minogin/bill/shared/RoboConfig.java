package ru.minogin.bill.shared;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.google.gwt.user.client.rpc.IsSerializable;

@Embeddable
public class RoboConfig implements IsSerializable {
	private String username;
	private String password1;
	private String password2;

	@Column(name = "robo_username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "robo_password1")
	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	@Column(name = "robo_password2")
	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}
}
