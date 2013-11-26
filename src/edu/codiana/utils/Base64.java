package edu.codiana.utils;

import java.nio.charset.Charset;



/**
 * @author Jan Hybš
 * @version 1.0
 */
public class Base64 {

    //--------------------------------------------------------------------------
    //
    // INIT
    //
    //--------------------------------------------------------------------------
    /** default encoding */
    private static final Charset UTF_8 = Charset.forName ("utf8");
    /** pattern for allowed characters */
    private static final String matchPattern = "^[a-zA-Z0-9\\.]+$";
    /** array containing base 64 map char */
    private static final char[] fromMap = new char[64];



    //# map fill
    static {
	int i = 0;
	for (char c = 'A'; c <= 'Z'; c++) fromMap[i++] = c;
	for (char c = 'a'; c <= 'z'; c++) fromMap[i++] = c;
	for (char c = '0'; c <= '9'; c++) fromMap[i++] = c;
	fromMap[i++] = '+';
	fromMap[i++] = '-';
    }
    /** array containing reversed ba64 map with 64 prefex (-1)*/
    private static final byte[] toMap = new byte[128];



    //# map fill
    static {
	for (int i = 0; i < toMap.length; i++) toMap[i] = -1;
	for (int i = 0; i < 64; i++) toMap[fromMap[i]] = (byte) i;
    }



    /**
     * Method performs (slightly altered) Base64 conversion
     * @param value string to be encoded
     * @return base 64 string with slash(/) replaced by dash(-)
     */
    public static String encode (String value) {
	return encode (value.getBytes (UTF_8));
    }



    /**
     * Method performs (slightly altered) Base64 conversion
     * @param bytes array of bytes to be encoded
     * @return base 64 string with slash(/) replaced by dash(-)
     */
    public static String encode (byte[] bytes) {
	int length = bytes.length;
	int outputLength = (length * 4 + 2) / 3;
	int fullOutputLength = ((length + 2) / 3) * 4;
	char[] result = new char[fullOutputLength];
	int bytesIndex = 0;
	int resultIndex = 0;

	int i0, i1, i2;
	int o0, o1, o2, o3;

	while (bytesIndex < length) {
	    i0 = bytes[bytesIndex++] & 0xff;
	    i1 = bytesIndex < length ? bytes[bytesIndex++] & 0xff : 0;
	    i2 = bytesIndex < length ? bytes[bytesIndex++] & 0xff : 0;
	    o0 = i0 >>> 2;
	    o1 = ((i0 & 3) << 4) | (i1 >>> 4);
	    o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
	    o3 = i2 & 0x3F;
	    result[resultIndex++] = fromMap[o0];
	    result[resultIndex++] = fromMap[o1];
	    result[resultIndex] = resultIndex < outputLength ? fromMap[o2] : '=';
	    resultIndex++;
	    result[resultIndex] = resultIndex < outputLength ? fromMap[o3] : '=';
	    resultIndex++;
	}
	return new String (result);
    }



    /**
     * Method performs (slightly altered) Base64 conversion
     * @param value string to be decoded
     * @return decoded string
     */
    public static String decode (String value) {
	return decode (value.toCharArray ());
    }



    /**
     * Method performs (slightly altered) Base64 conversion
     * @param bytes chars to be decoded
     * @return decoded string
     */
    public static String decode (char[] bytes) {
	int length = bytes.length;
	while (length > 0 && bytes[length - 1] == '=') length--;
	int outputLength = (length * 3) / 4;
	byte[] result = new byte[outputLength];
	int bytesIndex = 0;
	int resultIndex = 0;
	int i0, i1, i2, i3;
	int o0, o1, o2, o3;
	int b0, b1, b2, b3;

	while (bytesIndex < length) {
	    i0 = bytes[bytesIndex++];
	    i1 = bytes[bytesIndex++];
	    i2 = bytesIndex < length ? bytes[bytesIndex++] : 'A';
	    i3 = bytesIndex < length ? bytes[bytesIndex++] : 'A';
	    if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
		throw new IllegalArgumentException ("Illegal character in Base64 encoded data.");
	    b0 = toMap[i0];
	    b1 = toMap[i1];
	    b2 = toMap[i2];
	    b3 = toMap[i3];
	    if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
		throw new IllegalArgumentException ("Illegal character in Base64 encoded data.");
	    o0 = (b0 << 2) | (b1 >>> 4);
	    o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
	    o2 = ((b2 & 3) << 6) | b3;
	    result[resultIndex++] = (byte) o0;
	    if (resultIndex < outputLength) result[resultIndex++] = (byte) o1;
	    if (resultIndex < outputLength) result[resultIndex++] = (byte) o2;
	}
	return new String (result);
    }



    /**
     * Method encode given string if it contains illegal characters
     * In that case sharp char (#) is put at the begining
     * @param value String to be encoded
     * @return base 64 string or given string depend on chars in given string
     */
    public static String cleverEncode (String value) {
	if (value.matches (matchPattern)) {
	    return value;
	}
	return "#".concat (encode (value));
    }
}
