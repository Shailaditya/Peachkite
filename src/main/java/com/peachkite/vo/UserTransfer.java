package com.peachkite.vo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.peachkite.enums.MartialStatus;
import com.peachkite.security.PeachkiteUserDetail;

import lombok.Data;

@Data
public class UserTransfer {
	private String id;
	private String email;
	private String firstName;
	private String middleName;
	private String lastName;
	private String companyId;
	private MartialStatus martialStatus;
	private List<String> roles=new ArrayList<>();
	public UserTransfer(PeachkiteUserDetail userDetail) {
		this.id=userDetail.getId();
		this.email=userDetail.getUsername();
		this.firstName=userDetail.getFirstName();
		this.middleName=userDetail.getMiddleName();
		this.lastName=userDetail.getLastName();
		this.companyId=userDetail.getCompanyId();
		this.martialStatus=userDetail.getMartialStatus();
		for(GrantedAuthority ga:userDetail.getAuthorities()) {
			roles.add(ga.toString());
		}
	}
	
	public UserTransfer() {}
}