/**
 * 
 */
package com.tui.proof.ws.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * @Author : Giampiero Cicala
 *
 * @Copyright : 27-ago-2020
 * 
 * @Date : 27-ago-2020
 * 
 * @Time : 16.24.51
 * 
 * @Project : co-ordermanager
 * 
 * @Class : it.tim.cbe.co.ordermanager.utils.JsonUtil
 * 
 */
@Log4j2
public class JsonUtil {

	private static final ObjectMapper objectMapper = new ObjectMapper() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3666439261290002955L;

		@Override
		public ObjectMapper configure(
				DeserializationFeature f,
				boolean state) {
			return super.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false).configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE, false);
		};
	};

	public static final <T> void writeJSON(
			File path,
			String fileName,
			T root) throws Exception {

		File outputFile = new File(path, fileName);
		if (outputFile.exists()) {
			outputFile.delete();
		}
		if (!outputFile.exists()) {
			outputFile.getParentFile().mkdirs();
		}
		try (OutputStream outStream = new FileOutputStream(outputFile, true)) {
			objectMapper.writeValue(outStream, root);
			outStream.flush();
			outStream.close();
		} catch (Exception e) {
			throw e;
		}
	}

	public static <T> T loadJSON(
			String fileName,
			Class<T> valueType,
			boolean fromClasspath) throws Exception {

		try {
			String jsonString = "";
			if (fromClasspath) {
				jsonString = getJsonStringFromFileInClassPath(fileName);
			} else {
				fileName = "./src/main/resources" + fileName;
				jsonString = getJsonStringFromFile(fileName);
			}

			return objectMapper.readValue(jsonString, valueType);

		} catch (Exception ex) {
			throw ex;
		}
	}

	private static final String getJsonStringFromFile(
			String fileName) throws Exception {

		try (Stream<String> stream = Files.lines(Paths.get(fileName), StandardCharsets.UTF_8)) {
			StringBuilder contentBuilder = new StringBuilder();
			stream.forEach(s -> contentBuilder.append(s));
			stream.close();
			return contentBuilder.toString();
		} catch (Exception ex) {
			throw ex;
		}

	}

	private static final String getJsonStringFromFileInClassPath(
			String fileName) throws Exception {
		try (InputStream inputStream = TypeReference.class.getResourceAsStream(fileName)) {
			try (Stream<String> stream = new BufferedReader(new InputStreamReader(inputStream)).lines()) {
				StringBuilder contentBuilder = new StringBuilder();
				stream.forEach(s -> contentBuilder.append(s));
				stream.close();
				inputStream.close();
				return contentBuilder.toString();
			} catch (Exception ex) {

				throw ex;
			}

		} catch (Exception ex) {
			throw ex;
		}

	}

	public static String formatAsJsonString(
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

}
