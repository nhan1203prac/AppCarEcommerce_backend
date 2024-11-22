package Nhom7.car_ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Nhom7.car_ecommerce.modal.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//	List<Comment> findAllByUser_UserId(Long userId);
	List<Comment> findByCar_CarId(Long carId);
	List<Comment> findByUser_UserIdAndCar_CarId(Long userId, Long carId);

}
