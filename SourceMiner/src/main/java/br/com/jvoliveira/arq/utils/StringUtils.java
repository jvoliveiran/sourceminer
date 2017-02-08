/**
 * 
 */
package br.com.jvoliveira.arq.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @author Joao Victor
 *
 */
public class StringUtils {

	public static String getFileNameInPath(String path, String separator) {

		Integer lastIndex = path.lastIndexOf(separator);

		String name = path.substring(lastIndex + 1, path.length());

		return name;
	}

	public static String getFromCharBuffer(OutputStream buffer) {
		ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) buffer;
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())));
		String line = null;
		StringBuffer sb = new StringBuffer();
		try {
			while ((line = bufferedReader.readLine()) != null)
				sb.append(line + " \n ");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static String getExtensionInFileName(String fileNameWithExtension) {
		String[] piecesFileName = fileNameWithExtension.split("\\.");
		Integer lastPiecesNumber = piecesFileName.length - 1;

		try {
			return piecesFileName[lastPiecesNumber];
		} catch (ArrayIndexOutOfBoundsException boundException) {
			boundException.printStackTrace();
			return "";
		}
	}

	public static boolean isEmpty(String value) {
		return value == null || value.trim().length() == 0;
	}
}
