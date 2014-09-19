//package org.magnum.mobilecloud.video.repository;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//import javax.persistence.*;
//
//import org.springframework.security.core.userdetails.UserDetails;
//
//import com.google.common.base.Objects;
//
//@Entity
//public class User implements UserDetails {
//
//	private static final long serialVersionUID = 1L;
//
//	@Id
//	@GeneratedValue(strategy=GenerationType.AUTO)
//	private long id;
//	
//	private String username;
//	private String password;
//	private String grantedAuthority;
//	
//	public User() {
//	}
//	
//	public User(String username, String password) {
//		super();
//		this.username = username;
//		this.password = password;
//	}
//	
//	public User(String username, String password, String grantedAuthority) {
//		super();
//		this.username = username;
//		this.password = password;
//		this.grantedAuthority = grantedAuthority;
//	}
//	
//	@Override
//	public String getUsername() {
//		return username;
//	}
//
//	public void setUserName(String username) {
//		this.username = username;
//	}
//	
//	@Override
//	public String getPassword() {
//		return password;
//	}
//	
//	public void setPassword(String password) {
//		this.password = password; 
//	}
//	
//	@Override
//	public Collection<UserAuthority> getAuthorities() {
//		String[] roles = grantedAuthority.split(",");
//		Collection<UserAuthority> userRoles = new ArrayList<>();
//		for (int i = 0; i < roles.length; i++) {
//			userRoles.add(new UserAuthority(roles[i]));
//		}
//		return userRoles;
//	}
//
//	@Override
//	public boolean isAccountNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		return true;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		return true;
//	}
//
//	@Override
//	public boolean isEnabled() {
//		return true;
//	}
//
//	@Override
//	public int hashCode() {
//		// Google Guava provides great utilities for hashing
//		return Objects.hashCode(username, password);
//	}
//	
//	@Override
//	public boolean equals(Object obj) {
//		if (obj instanceof User) {
//			User other = (User) obj;
//			// Google Guava provides great utilities for equals too!
//			return Objects.equal(username, other.username)
//					&& Objects.equal(password, other.password);
//		} else {
//			return false;
//		}
//	}
//
//}
