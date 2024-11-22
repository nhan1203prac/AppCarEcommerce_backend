package Nhom7.car_ecommerce.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Nhom7.car_ecommerce.modal.Car;
import Nhom7.car_ecommerce.modal.ListFavouriteCar;
import Nhom7.car_ecommerce.modal.User;
import Nhom7.car_ecommerce.repository.ListFavouriteCarRepository;

@Service
public class ListFavouriteService {
	@Autowired
	private ListFavouriteCarRepository listFavouriteCarRepository;
	@Autowired
	private CarService carService;
	
	
	public Car addCarToList(Long carId, User user) throws Exception {
		Car car = carService.findCarPostById(carId);
		ListFavouriteCar list = listFavouriteCarRepository.findByUser_UserId(user.getUserId());
		if(list.getCars().contains(car)) {
			list.getCars().remove(car);
		}else {
			list.getCars().add(car);
		}
		listFavouriteCarRepository.save(list);
		return car;
		
	}
	
	public ListFavouriteCar findById(Long id) throws Exception {
		Optional<ListFavouriteCar> optional = listFavouriteCarRepository.findById(id);
		if(optional.isEmpty())
			throw new Exception("List not found");
		return optional.get();
	}
	public ListFavouriteCar findUserList(Long userId) throws Exception {
		ListFavouriteCar list = listFavouriteCarRepository.findByUser_UserId(userId);
		if(list==null) {
			throw new Exception("List not found");
		}
		return list;
	}
}
