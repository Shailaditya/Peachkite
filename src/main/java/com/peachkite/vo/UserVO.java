package com.peachkite.vo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.peachkite.db.domain.User;
import com.peachkite.enums.MartialStatus;
import com.peachkite.security.PeachkiteUserDetail;

import lombok.Data;

@Data
public class UserVO {
	private String userId;
	private String email;
	private String firstName;
	private String middleName;
	private String lastName;
	private String companyId;
	private MartialStatus martialStatus; 
	private List<String> roles=new ArrayList<>();
	
	public UserVO(PeachkiteUserDetail userDetail) {
		this.userId=userDetail.getId();
		this.email=userDetail.getUsername();
		this.firstName=userDetail.getFirstName();
		this.middleName=userDetail.getMiddleName();
		this.lastName=userDetail.getLastName();
		this.companyId=userDetail.getCompanyId();
		this.martialStatus=userDetail.getMartialStatus();
		if(userDetail.getAuthorities() != null) {
			Iterator<? extends GrantedAuthority> iterator=userDetail.getAuthorities().iterator();
			while (iterator.hasNext()) {
				GrantedAuthority authority = iterator.next();
				this.roles.add(authority.getAuthority());
			}
		}
	}
	
	public UserVO(User user) {
		this.userId=user.getId();
		this.email=user.getEmail();
		this.firstName=user.getFirstName();
		this.middleName=user.getMiddleName();
		this.lastName=user.getLastName();
		this.companyId=user.getCompanyId();
		this.martialStatus=user.getMartialStatus();
		this.roles=user.getRoles();
	}
	
	@JsonIgnore
	public boolean hasRole(String role2) {
		for(String role : this.roles)
			if(role.equals(role2))
				return true;
		return false;
	}
}
