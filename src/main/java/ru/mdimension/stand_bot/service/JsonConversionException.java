package ru.mdimension.stand_bot.service;

public class JsonConversionException extends RuntimeException {

	private static final long serialVersionUID = 5886976235097211295L;


	public JsonConversionException(Throwable cause) {
        super(cause);
    }

    public JsonConversionException(String message) {
        super(message);
    }
}
