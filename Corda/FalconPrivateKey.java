package net.corda.core.crypto;

import java.security.spec.PKCS8EncodedKeySpec;
import java.security.PrivateKey;

public class FalconPrivateKey extends PKCS8EncodedKeySpec implements PrivateKey {
	public FalconPrivateKey(byte[] encoded_key) {
		super(encoded_key);
	}

	public String getAlgorithm() {
		return FalconProvider.ALG_NAME;
	}

	public String toString() {
		String s = "0x";

		for (byte b : getEncoded()) {
			s = s + String.format("%02x", b);
		}

		return s;
	}

	public byte[] sign() {
		return null;
	}
}
