package net.corda.core.crypto;

import java.security.PrivateKey;
import java.io.IOException;

public class FalconPrivateKey implements PrivateKey {
	private byte[] encoded_key;

	public FalconPrivateKey(byte[] encoded_key) {
		this.encoded_key = encoded_key;
	}

	public String getAlgorithm() {
		return FalconProvider.ALG_NAME;
	}

	public String getFormat() {
		return "PKCS#8";
	}

	public byte[] getEncoded() {
		return encoded_key;
	}

	public String toString() {
		String s = "0x";

		for (byte b : encoded_key) {
			s = s + String.format("%02x", b);
		}

		return s;
	}
}
