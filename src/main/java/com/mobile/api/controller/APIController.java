package com.mobile.api.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobile.api.config.Utility;
import com.mobile.api.model.RqUser;
import com.mobile.api.model.RsError;
import com.mobile.api.model.RsToken;
import com.mobile.api.model.User;
import com.mobile.api.repos.UserRepository;
import com.mobile.api.service.JWTService;

@RestController
@CrossOrigin
public class APIController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	JWTService jwtService;

	@Autowired
	private Utility utility;

	@Autowired
	private UserRepository userRepos;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;

	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public ResponseEntity<RsToken> saveUser(@RequestBody  RqUser user) throws Exception {
		User newUser = validate(user);
		ResponseEntity<RsToken> responseEntity = null;
		if (newUser != null) {
			User saved = userRepos.save(newUser);
			authenticate(saved.getUsername(), saved.getPassword());
			final UserDetails userDetails = jwtService.loadUserByUsername(saved.getUsername());
			final String token = utility.generateToken(userDetails);
			return ResponseEntity.ok(new RsToken(token));
		}

		RsError error = new RsError();
		error.setErrorCode("9999");
		error.setErrorDesc("data invalid");

		RsToken rsToken = new RsToken("");
		rsToken.setError(error);

		responseEntity = new ResponseEntity<RsToken>(rsToken, HttpStatus.BAD_REQUEST);
		return responseEntity;
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public ResponseEntity<User> getUser(@RequestParam("username") String username) throws Exception {
    	if(userRepos.findByUsername(username).getUsername()==null) {
    		return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    	}
		return new ResponseEntity<>(userRepos.findByUsername(username),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.PUT)
    public void updateTrader(@RequestBody RqUser user) throws Exception {
		User updateUser = validate(user);
		userRepos.save(updateUser);
    }
	
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	public User validate(RqUser rquser) throws Exception {
		String phone = rquser.getPhone();
		String memberType = "Platinum";
		String pattern = "YYYYMMDD";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		String refCode = date + phone.substring(phone.length() - 5, phone.length() - 1);

		int salary = 0;
		if (salary < 15000) {
			return null;
		} else if (salary < 30000) {
			memberType = "Silver";
		} else if ((30000 <= salary) && (salary <= 50000)) {
			memberType = "Gold";
		}

		User user = new User();
		user.setUsername(rquser.getUsername());
		user.setPassword(bcryptEncoder.encode(rquser.getPassword()));
		user.setReferenceCode(refCode);
		user.setSalary(rquser.getSalary());
		user.setAddress(rquser.getAddress());
		user.setPhone(rquser.getPhone());
		user.setMemberType(memberType);

		return user;
	}
	
	

	 
}
