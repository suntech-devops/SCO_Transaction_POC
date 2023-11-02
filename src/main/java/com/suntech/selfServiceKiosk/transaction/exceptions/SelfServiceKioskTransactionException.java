package com.suntech.selfServiceKiosk.transaction.exceptions;

import com.suntech.selfServiceKiosk.transaction.constants.SelfServiceKioskErrorMessageConstants;

public class SelfServiceKioskTransactionException extends RuntimeException {

	private static final long serialVersionUID = 549397557660196883L;

	private final String errorMessage;
	private final String errorCode;

	public SelfServiceKioskTransactionException(String message, String errorCode, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
		errorMessage = message;
	}

	public SelfServiceKioskTransactionException(String message, String errorCode) {
		super(message, new Throwable(message));
		this.errorCode = errorCode;
		errorMessage = message;
	}

	public SelfServiceKioskTransactionException(ErrorCodes error) {
		super(error.getMessage());
		errorCode = error.getCode();
		errorMessage = error.getMessage();
	}

	public SelfServiceKioskTransactionException(ErrorCodes error, String fieldName) {
		super(error.getMessage());
		errorCode = error.getCode();
		errorMessage = error.getMessage().replace(SelfServiceKioskErrorMessageConstants.FIELD_NAME, fieldName);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getErrorCode() {
		return errorCode;
	}

}