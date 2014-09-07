package org.magnum.dataup.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_FOUND, reason="No such video")
public class VideoNotFoundException extends RuntimeException {

	public VideoNotFoundException(long id) {
		super();	
	}

	private static final long serialVersionUID = 1L;

}
