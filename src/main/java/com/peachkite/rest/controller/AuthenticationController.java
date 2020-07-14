package com.peachkite.rest.controller;

import com.peachkite.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class AuthenticationController {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	TokenBasedRememberMeServices rememberMeService;

	@Autowired
	private UserService userService;
	
	@ResponseBody
	@RequestMapping(value="/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse response) {

		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
			token.setDetails(new WebAuthenticationDetails(request));

			Authentication authentication = authenticationManager.authenticate(token);
			SecurityContext securityContext = SecurityContextHolder.getContext();
			securityContext.setAuthentication(authentication);

			if(authentication.isAuthenticated()){
				/*HttpSession session = request.getSession(true);
				session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);*/
				if(Boolean.TRUE.equals(loginRequest.getRememberme())){
					rememberMeService.onLoginSuccess(request, response, authentication);
				}
				return new ResponseEntity<>(userService.getLoggedInUser(),HttpStatus.OK);
			}else{
				SecurityContextHolder.getContext().setAuthentication(null);

				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (UsernameNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ResponseBody
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			new SecurityContextLogoutHandler().logout(request, response, auth);
			rememberMeService.logout(request, response, auth);
		}
		SecurityContextHolder.getContext().setAuthentication(null);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
}

@Data
class LoginRequest{
	private String username;
	private String password;
	private Boolean rememberme;
}
