package Nhom7.car_ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Nhom7.car_ecommerce.modal.User;
import Nhom7.car_ecommerce.repository.UserRepository;
import Nhom7.car_ecommerce.service.UserService;

@RestController
//@RequestMapping("/api")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserService userService;
	@GetMapping("/api/user")
	ResponseEntity<List<User>> getUser(){
		return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
	}
	
	@GetMapping("/api/user/profile")
	ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findUserByJwt(jwt);
		
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
	
	@GetMapping("/api/user/{userId}")
	ResponseEntity<User> getUserById(@PathVariable("userId") Long userId) throws Exception{
		User user = userService.findByUserId(userId);
		
		return ResponseEntity.status(HttpStatus.OK).body(user);
	}
	
	
}
