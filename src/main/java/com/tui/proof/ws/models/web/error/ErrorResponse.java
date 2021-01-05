package com.tui.proof.ws.models.web.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class ErrorResponse {

	@Schema(required = true, description = "Codice errore", example = "ERR_001", type = "string")
	private String errorCode;

	@Schema(required = true, description = "Messaggio di errore", example = "Errore dal sistema esterno", type = "string")
	private String errorMessage;

	public ErrorResponse(
		String errorCode,
		String errorMessage) {

		this.errorCode = errorCode;
		this.errorMessage = errorMessage;

	}

}
