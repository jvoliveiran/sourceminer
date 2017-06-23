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
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

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

	/**
	 * Decodes a string by trying several charsets until one does not throw a
	 * coding exception. Last resort is to interpret as UTF-8 with illegal
	 * character substitution.
	 * 
	 * @param content
	 * @param charsets
	 *            optional
	 * @return a string
	 */
	public static String decodeString(byte[] content, String... charsets) {
		Set<String> sets = new LinkedHashSet<String>();
		if ((charsets != null)) {
			sets.addAll(Arrays.asList(charsets));
		}
		String value = null;
		sets.addAll(Arrays.asList("UTF-8", "ISO-8859-1", Charset.defaultCharset().name()));
		for (String charset : sets) {
			try {
				Charset cs = Charset.forName(charset);
				CharsetDecoder decoder = cs.newDecoder();
				CharBuffer buffer = decoder.decode(ByteBuffer.wrap(content));
				value = buffer.toString();
				break;
			} catch (CharacterCodingException e) {
				// ignore and advance to the next charset
			} catch (IllegalCharsetNameException e) {
				// ignore illegal charset names
			} catch (UnsupportedCharsetException e) {
				// ignore unsupported charsets
			}
		}
		if (value.startsWith("\uFEFF")) {
			// strip UTF-8 BOM
			return value.substring(1);
		}
		return value;
	}
	
	public static boolean isEmpty(String str){
		return str == null || str.trim().equals("") || str.trim().length() == 0;
	}
	
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
	
	public static boolean isFirstUpperCase(String str){
		return Character.isUpperCase(str.charAt(0));
	}
}
