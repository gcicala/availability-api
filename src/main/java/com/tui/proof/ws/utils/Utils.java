package com.tui.proof.ws.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tui.proof.ws.exception.ServiceException;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class Utils {

	private ObjectMapper objectMapper;

	@Autowired
	public Utils(
		ObjectMapper objectMapper) {
		super();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
		this.objectMapper = objectMapper;
	}

	public String formatAsJsonString(
			Object o) {
		String res = "";
		if (o == null)
			return res;
		try {
			res = objectMapper.writeValueAsString(o);
		} catch (Throwable e) {
			log.error(e.getMessage());

		}
		return res;
	}

	public static <T> T formatJsonStringAsObject(
			String jsonString,
			Class<? extends T> type) throws ServiceException {

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			return objectMapper.readValue(jsonString, type);

		} catch (Throwable ex) {
			throw new ServiceException("Errore Interno", "ERR_01");
		}
	}

	private static final Pattern CF_PATTERN = Pattern.compile("[a-zA-Z]{6}[A-Z0-9a-z0-9]{10}");
	private static final Pattern CF_TEMP_PATTERN = Pattern.compile("[0]{5}[0-9]{11}");

	public static boolean isFiscalCode(
			String text) {
		return text != null && CF_PATTERN.matcher(text).matches();
	}

	public static boolean isTempFiscalCode(
			String text) {
		return text != null && CF_TEMP_PATTERN.matcher(text).matches();
	}

	public <T> T copyBeanProperties(
			Object src,
			Class<T> claszDest) throws IOException {
		if (src == null) {
			throw new RuntimeException("src cannot be null");
		}
		if (claszDest == null) {
			throw new RuntimeException("typereference cannot be null");
		}
		return copyBeanProperties(src, objectMapper.getTypeFactory().constructType(claszDest));
	}

	public <T> T copyBeanProperties(
			Object src,
			TypeReference<T> typereference) throws IOException {
		if (src == null) {
			throw new RuntimeException("src cannot be null");
		}
		if (typereference == null) {
			throw new RuntimeException("typereference cannot be null");
		}

		return copyBeanProperties(src, objectMapper.getTypeFactory().constructType(typereference));
	}

	public <T> T copyBeanProperties(
			Object src,
			JavaType typereference) throws IOException {
		if (src == null) {
			throw new RuntimeException("src cannot be null");
		}
		if (typereference == null) {
			throw new RuntimeException("typereference cannot be null");
		}
		String serializedObj = objectMapper.writeValueAsString(src);

		T result = objectMapper.readValue(serializedObj, typereference);

		return result;
	}

	public static String getJsonStringFromFileInClassPath(
			String fileName) throws Exception {

		InputStream inputStream = null;
		try {
			StringBuilder contentBuilder = new StringBuilder();
			inputStream = TypeReference.class.getResourceAsStream(fileName);
			String line = "";

			BufferedReader bR = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = bR.readLine()) != null) {

				contentBuilder.append(line);

			}
			inputStream.close();

			return contentBuilder.toString();

		} catch (Exception ex) {
			throw ex;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static String getJsonStringFromFile(
			String fileName) throws Exception {

		Stream<String> stream = null;
		try {

			StringBuilder contentBuilder = new StringBuilder();
			stream = Files.lines(Paths.get(fileName), StandardCharsets.ISO_8859_1);

			stream.forEach(s -> contentBuilder.append(s));

			return contentBuilder.toString();
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static <T> T loadResponse(
			String fileName,

			Class<T> type,
			boolean fromClasspath) throws Exception {

		try {

			String jsonString = "";
			if (fromClasspath) {
				jsonString = getJsonStringFromFileInClassPath(fileName);
			} else {
				fileName = "./src/main/resources" + fileName;
				jsonString = getJsonStringFromFile(fileName);
			}

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			return objectMapper.readValue(jsonString, type);

		} catch (Exception ex) {
			System.out.println("Unable to load Response from file [" + fileName + "] " + ex);
			throw ex;
		}
	}

}
