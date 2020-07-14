package com.peachkite.security;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.peachkite.db.domain.User;
import com.peachkite.enums.MartialStatus;

public class PeachkiteUserDetail implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1024234747725024296L;
	
	private String id;
	private String username;
	private String firstName;
	private String middleName;
	private String lastName;
	private String companyId;
	private MartialStatus martialStatus;
	private String password;
	private boolean enabled = true;
    private Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

	public PeachkiteUserDetail(User user) {
    	this.id=user.getId();
    	this.username=user.getEmail();
    	this.firstName=user.getFirstName();
    	this.middleName=user.getMiddleName();
    	this.lastName=user.getLastName();
    	this.companyId=user.getCompanyId();
    	this.martialStatus=user.getMartialStatus();
    	this.password=user.getPassword();
    	for(String role : user.getRoles())
    		this.authorities.add(new SimpleGrantedAuthority(role));
    }
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public MartialStatus getMartialStatus() {
		return martialStatus;
	}

	public void setMartialStatus(MartialStatus martialStatus) {
		this.martialStatus = martialStatus;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void setAuthorities(Set<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isEnabled();
	}

	@Override
	public boolean isAccountNonLocked() {
		return isEnabled();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isEnabled();
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

}
