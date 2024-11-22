package Nhom7.car_ecommerce.response;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
	private String jwt;
	private String message;
	private boolean status;
	
	
}
