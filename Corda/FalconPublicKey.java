package net.corda.core.crypto;

import java.security.spec.X509EncodedKeySpec;
import java.security.PublicKey;

public class FalconPublicKey extends X509EncodedKeySpec implements PublicKey {
	public FalconPublicKey(byte[] encoded_key) {
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
}
