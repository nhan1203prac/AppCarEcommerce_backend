package Nhom7.car_ecommerce.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import Nhom7.car_ecommerce.Domain.CAR_STATUS;
import Nhom7.car_ecommerce.modal.Car;
import Nhom7.car_ecommerce.modal.ImageCar;
import Nhom7.car_ecommerce.modal.User;
import Nhom7.car_ecommerce.repository.CarRepository;
import Nhom7.car_ecommerce.repository.ImageCarRepository;
import Nhom7.car_ecommerce.request.CarRequest;

@Service
public class CarService {
	@Autowired
	private CarRepository carRepository;
	@Autowired
	private ImageCarRepository imageCarRepository;
	@Autowired
	private UserService userService;
	public Car createCarPost(User user,String brand,String name,String description, double price, List<String> pictures) {
		Car newCar = new Car();
		newCar.setBrand(brand);
		newCar.setCreatedAt(LocalDate.now());
		newCar.setName(name);
		newCar.setDescription(description);
		newCar.setPrice(price);
		newCar.setUser(user);
		
		carRepository.save(newCar);
		List<ImageCar> imageCar = new ArrayList<>();
		for (String url : pictures) {
			ImageCar image = new ImageCar();
			image.setCar(newCar);
			image.setUrl(url);
			imageCarRepository.save(image);
			imageCar.add(image);
		}
		newCar.setImages(imageCar);
		carRepository.save(newCar);
		return newCar;
	}
	
	public Car findCarPostById(Long id) throws Exception {
		Optional<Car> optionCar = carRepository.findById(id);
		if(optionCar.isEmpty())
			throw new Exception("Car post not found");
		return optionCar.get();
	}
	public Car updateCarPostStatus(Long id, boolean accept) throws Exception {
		Car isExistCar = findCarPostById(id);
		if(accept) {
			isExistCar.setStatus(CAR_STATUS.SUCCESS);
		}
		return carRepository.save(isExistCar);
	}
	
	public Car updateCarPost(Long id, CarRequest req) throws Exception {
		Car isExistCar = findCarPostById(id);
		isExistCar.setBrand(req.getBrand());
		isExistCar.setDescription(req.getDescription());
		isExistCar.setName(req.getName());
		isExistCar.setPrice(req.getPrice());
		
		isExistCar.setImages(new ArrayList<ImageCar>());
		
		List<ImageCar> imageCar = new ArrayList<>();
		for (String picture : req.getPictures()) {
			ImageCar img = new ImageCar();
			img.setCar(isExistCar);
			img.setUrl(picture);
			imageCar.add(img);
		}
		isExistCar.setImages(imageCar);
		return carRepository.save(isExistCar);
	}
	
	public List<Car> getListPostCarStatusSuccess(){
		return carRepository.findByStatus(CAR_STATUS.SUCCESS);
	}
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<Car> getAllPostCar(){
		return carRepository.findAll();
	}
	
	
}
