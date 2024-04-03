
package com.shopnest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopnest.config.JwtProvider;
import com.shopnest.exception.UserException;
import com.shopnest.modal.Cart;
import com.shopnest.modal.User;
import com.shopnest.repository.UserRepository;
import com.shopnest.request.LoginRequest;
import com.shopnest.response.AuthResponse;
import com.shopnest.service.CartService;
import com.shopnest.service.CustomUserServiceImplementation;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private UserRepository userRepository;
	private JwtProvider jwtProvider;
	private PasswordEncoder passwordEncoder;
	private CustomUserServiceImplementation customUserServiceImplementation;
	private CartService cartService;
	
	
	
	public AuthController(UserRepository userRepository, JwtProvider jwtProvider, PasswordEncoder passwordEncoder,
			CustomUserServiceImplementation customUserServiceImplementation, CartService cartService) {
		this.userRepository = userRepository;
		this.jwtProvider = jwtProvider;
		this.passwordEncoder = passwordEncoder;
		this.customUserServiceImplementation = customUserServiceImplementation;
		this.cartService = cartService;
	}

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse>createUserHandler(@RequestBody User user) throws UserException{
		String email = user.getEmail();
		String password = user.getPassword();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		
		User isEmailExist=userRepository.findByEmail(email);
		
//		checking if user is already present in the database
		if(isEmailExist!=null)
		{
			throw new UserException("Email is Already Used with Another Account");
		}
//		Creating new user
		User createdUser = new User();
		createdUser.setEmail(email);
		createdUser.setPassword(passwordEncoder.encode(password));
		createdUser.setFirstName(firstName);
		createdUser.setLastName(lastName);
		
//		saving new user in the database
		User savedUser = userRepository.save(createdUser);
		Cart cart=cartService.createCart(savedUser);
		Authentication authentication = new UsernamePasswordAuthenticationToken(savedUser.getEmail(), savedUser.getPassword()); 
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtProvider.generateToken(authentication);
		
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("SignUp success");
		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse>loginUserHandler(@RequestBody LoginRequest loginRequest){
		
		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();
		
		Authentication authentication = authenticate(username,password);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token = jwtProvider.generateToken(authentication);
		

		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Login Success");
		
		return new ResponseEntity<AuthResponse>(authResponse,HttpStatus.CREATED);
	}

	private Authentication authenticate(String username, String password) {
		
		UserDetails userDetails = customUserServiceImplementation.loadUserByUsername(username);
		
		if(userDetails==null)
		{
			throw new BadCredentialsException("Invalid username");
		}
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid Pasword......");
		}
	
		return new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
	}
	
}
