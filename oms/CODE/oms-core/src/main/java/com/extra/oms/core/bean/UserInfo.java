/**
 * 
 */
package com.extra.oms.core.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author aibrahim
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserInfo {

	private Long id;

	private String userName;

	private String password;

	private List<String> privileges;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getPrivileges() {
		return privileges;
	}

	public void setPrivileges(List<String> privileges) {
		this.privileges = privileges;
	}
}
