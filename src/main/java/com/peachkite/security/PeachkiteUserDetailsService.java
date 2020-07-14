package com.peachkite.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.peachkite.db.domain.User;
import com.peachkite.db.repository.UserRepository;

@Service
public class PeachkiteUserDetailsService implements UserDetailsService{
	
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user=this.userRepository.findByEmail(email);
		if(user == null)
			throw new UsernameNotFoundException("No exists with email : "+email);
		return new PeachkiteUserDetail(user);
	}

}
