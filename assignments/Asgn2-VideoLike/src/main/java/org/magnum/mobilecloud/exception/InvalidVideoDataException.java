package org.magnum.mobilecloud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.BAD_REQUEST, reason="Invalid video data")
public class InvalidVideoDataException extends RuntimeException {

	public InvalidVideoDataException() {
		super();	
	}

	private static final long serialVersionUID = 3L;

}
