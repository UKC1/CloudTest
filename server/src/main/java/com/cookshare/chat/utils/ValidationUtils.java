package com.cookshare.chat.utils;

import java.util.Optional;

public class ValidationUtils {

	public static String validateNotEmpty(String input, String paramName) {
		return Optional.ofNullable(input)
			.filter(s -> !s.trim().isEmpty())
			.orElseThrow(() -> new IllegalArgumentException(String.format("%s의 값이 비어있다", paramName)));
	}

}
