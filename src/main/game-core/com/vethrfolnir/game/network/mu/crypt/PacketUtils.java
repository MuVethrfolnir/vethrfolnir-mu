/*
 This application is part of corvus engine, and its strictly bounded to its owner.
 Using corvus engine unauthorized is strictly forbidden.
 */
package com.vethrfolnir.game.network.mu.crypt;

/**
 * @author PsychoJr
 */
public class PacketUtils {

	public static String byteArrayToHex(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1)+" ";
		}
		return result.toUpperCase();
	}

	public static String byteArrayToHex2(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += "-" + Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result.toUpperCase().substring(1);
	}

	public static byte[] hex2Bytes(String hex) {
		hex = hex.trim().replace(" ", "");
		int len = hex.length();
		if (len % 2 == 1)
			return null;
		byte[] b = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			b[i >> 1] = (byte) Integer.parseInt(hex.trim().substring(i, i + 2), 16);
		}

		return b;
	}

	public static byte[] hex2Bytes2(String hex) {
		hex = hex.trim().replace("-", "");
		int len = hex.length();
		
		if (len % 2 == 1)
			return null;
		
		byte[] b = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			b[i >> 1] = (byte) Integer.parseInt(hex.trim().substring(i, i + 2), 16);
		}

		return b;
	}

	public static int[] hex2IntBytes(String hex) {
		hex = hex.trim().replace(" ", "");
		int len = hex.length();
		if (len % 2 == 1)
			return null;
		int[] b = new int[len / 2];
		for (int i = 0; i < len; i += 2) {
			b[i >> 1] = (byte) Integer.parseInt(hex.trim().substring(i, i + 2), 16);
		}

		return b;
	}

	/**
	 * @param contents
	 * @return
	 */
	public static String byteArrayToHex(short[] contents) {
		String result = "";
		for (int i = 0; i < contents.length; i++) {
			result += Integer.toString((contents[i] & 0xff) + 0x100, 16).substring(1)+" ";
		}
		return result.toUpperCase();
	}

}
