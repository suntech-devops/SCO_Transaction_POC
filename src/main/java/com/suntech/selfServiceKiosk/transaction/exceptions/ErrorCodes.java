package com.suntech.selfServiceKiosk.transaction.exceptions;





import com.suntech.selfServiceKiosk.transaction.constants.SelfServiceKioskErrorMessageConstants;

public enum ErrorCodes {

	INVALID_FIELD(SelfServiceKioskErrorMessageConstants.FUNCTIONAL, SelfServiceKioskErrorMessageConstants.ERROR_CODE_INVALID_PARAMETER, "[fieldName] is In-valid"),
	DW_EXPIRY_EXCEED_MAX_DAYS(SelfServiceKioskErrorMessageConstants.FUNCTIONAL, "ERROR_DW_EXTEND_EXPIRY",
			"Wallet entry Requested expiry days must be less than Maxmium allowed days [fieldName]"),
	ENTRY_ALREADY_EXDENED(SelfServiceKioskErrorMessageConstants.FUNCTIONAL, "ERROR_ENTRY_ALREADY_EXPIERD",
			"Wallet entry expity date is already extended cannot extend again"),
	INVALID_JSON(SelfServiceKioskErrorMessageConstants.FUNCTIONAL, "ERROR_INVALID_JSON_REQUEST", "Invalid JSON"),
	NOTIFCATION_FAILURE(SelfServiceKioskErrorMessageConstants.FUNCTIONAL, "ERROR_NOTIFICATION_FAILURE", "Failed to send Notification messages");

	private final String type;
	private final String code;
	private final String message;

	private ErrorCodes(String type, String code, String message) {
		this.type = type;
		this.code = code;
		this.message = message;
	}

	public String getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
