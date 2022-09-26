package schwarz.jobs.interview.coupon.core.exception;

public class AppException extends RuntimeException{
	    private String errorCode;
	    private String errorData;
	    
	    public AppException() {
		   
	    }

	    public AppException(String errorCode, String  errorData) {
	       
	        this.errorCode = errorCode;
	        this.errorData = errorData;
	    }

		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		

		public String getErrorData() {
			return errorData;
		}

		public void setErrorData(String errorData) {
			this.errorData = errorData;
		}

	
	   

}
