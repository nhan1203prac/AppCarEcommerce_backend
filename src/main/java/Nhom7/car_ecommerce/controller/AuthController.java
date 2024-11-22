package Nhom7.car_ecommerce.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import Nhom7.car_ecommerce.config.JwtProvider;
import Nhom7.car_ecommerce.modal.ListFavouriteCar;
import Nhom7.car_ecommerce.modal.User;
import Nhom7.car_ecommerce.repository.ListFavouriteCarRepository;
import Nhom7.car_ecommerce.repository.UserRepository;
import Nhom7.car_ecommerce.request.EmailRequest;
import Nhom7.car_ecommerce.response.AuthResponse;
import Nhom7.car_ecommerce.service.EmailService;
import Nhom7.car_ecommerce.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
    private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ListFavouriteCarRepository listFavouriteCarRepository;
	@PostMapping("/signup")
	ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception{
		User isEmailExist = userRepository.findByEmail(user.getEmail());
		if(isEmailExist!=null) {
			throw new Exception("Email is used in another account");
		}
		user.setCreatedAt(LocalDate.now());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		System.out.println("----------create user1"+ user);
		userRepository.save(user);
		System.out.println("----------create user2"+ user);
		List<GrantedAuthority> authorities = Collections.singletonList(
		        new SimpleGrantedAuthority(user.getRole().toString()) 
		    );

		Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(),null,authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwt = JwtProvider.GenerateToken(auth);
		System.out.println("----------create user3"+ user);
		
		ListFavouriteCar favourite = new ListFavouriteCar();
		favourite.setUser(user);
		listFavouriteCarRepository.save(favourite);
		return ResponseEntity.status(HttpStatus.CREATED).body(AuthResponse.builder()
				.jwt(jwt)
				.message("Register success")
				.status(true)
				.build());
	}
	
	@PostMapping("/signin")
	ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception{
		User isEmailInvalid = userService.findUserByEmail(user.getEmail());
		boolean authenticated = passwordEncoder.matches(user.getPassword(),isEmailInvalid.getPassword());
		if(!authenticated) {
			throw new BadCredentialsException("Wrong email or password");
		}
		
		List<GrantedAuthority> authorities = Collections.singletonList(
		        new SimpleGrantedAuthority(isEmailInvalid.getRole().toString()) 
		    );
		System.out.printf("roles_signin: %s%n", authorities);

		Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(),null,authorities);
		SecurityContextHolder.getContext().setAuthentication(auth);
		String jwt = JwtProvider.GenerateToken(auth);
		return ResponseEntity.status(HttpStatus.OK).body(AuthResponse.builder()
				.jwt(jwt)
				.message("Login success")
				.status(true)
				.build()
				);
	}
	
	@PostMapping("/forgot-password/send-otp") 
	ResponseEntity<String> sendOtp(@RequestBody EmailRequest email) throws Exception{
		String newPass = userService.generatePassword();
		User user = userService.findUserByEmail(email.getEmail());
		userService.updatePassword(user, newPass);
		emailService.sendVerifycationOtpEmail(email.getEmail(), newPass);
		return ResponseEntity.status(HttpStatus.OK).body("Password reset sent successfully");
		
	}
	
	@GetMapping("/user")
	ResponseEntity<List<User>> getUser(){
		return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
	}
}
