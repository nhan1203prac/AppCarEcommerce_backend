package Nhom7.car_ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Nhom7.car_ecommerce.modal.Car;
import Nhom7.car_ecommerce.modal.User;
import Nhom7.car_ecommerce.request.CarRequest;
import Nhom7.car_ecommerce.service.CarService;
import Nhom7.car_ecommerce.service.UserService;

@RestController
@RequestMapping("/api")
public class CarController {
	@Autowired
	private CarService carService;
	@Autowired
	private UserService userService;
	@PostMapping("/car")
	public ResponseEntity<Car> createCarPost(@RequestBody CarRequest req, @RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findUserByJwt(jwt);
		return ResponseEntity.status(HttpStatus.CREATED).body(carService.createCarPost(user, req.getBrand(), req.getName(), 
				req.getDescription(), req.getPrice(),req.getPictures()));
	}
	@GetMapping("/car/{carId}")
	public ResponseEntity<Car> findCarById(@PathVariable("carId") Long carId) throws Exception{
		return ResponseEntity.status(HttpStatus.OK).body(carService.findCarPostById(carId));
	}
	
	@PatchMapping("/car/{carId}/{accept}")
	public ResponseEntity<Car> updateCarPostStatus(@PathVariable("carId") Long carId,@PathVariable("accept") boolean accept) throws Exception{
		return ResponseEntity.status(HttpStatus.OK).body(carService.updateCarPostStatus(carId, accept));
	}
	
	@PutMapping("/car/{carId}")
	public ResponseEntity<Car> updateCarPost(@PathVariable("carId") Long carId, CarRequest req) throws Exception{
		return ResponseEntity.status(HttpStatus.OK).body(carService.updateCarPost(carId, req));
	}
	@GetMapping("/car")
	public ResponseEntity<List<Car>> getListStatusSuccess() throws Exception{
		return ResponseEntity.status(HttpStatus.OK).body(carService.getListPostCarStatusSuccess());
	}
	@GetMapping("/car/all")
	public ResponseEntity<List<Car>> getAll() throws Exception{
		return ResponseEntity.status(HttpStatus.OK).body(carService.getAllPostCar());
	}
	 
}
