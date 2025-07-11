package cl.previred.common.exception;

public class CredencialesInvalidasException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private final ErrorCode errorCode;

	public CredencialesInvalidasException(String mensaje, ErrorCode errorCode) {
		super(mensaje);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

}
