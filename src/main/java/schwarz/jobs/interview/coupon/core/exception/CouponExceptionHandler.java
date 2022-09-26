package schwarz.jobs.interview.coupon.core.exception;


import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CouponExceptionHandler{
	
  
    @ExceptionHandler(AppException.class)
    public ResponseEntity<Object> handleAppException(AppException exception){
    	Map<String,Object>body = new HashMap<String, Object>();
        HttpStatus status = null;
        String code =  exception.getErrorCode();

    	body.put("message", exception.getErrorData());

        if("CouponNotFound".equals(code))
               status = HttpStatus.NOT_FOUND;               
        else if  ("BasketNotFound".equals(code))
                status = HttpStatus.BAD_REQUEST;                
        else if  (("DiscountNegative".equals(code)) || ("CouponMinValue".equals(code)))
                status = HttpStatus.NOT_ACCEPTABLE;
    	
        return new ResponseEntity<>(body,status);
    }
    
    
   

}
