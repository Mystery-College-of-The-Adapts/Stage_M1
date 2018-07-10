package net.corda.core.crypto;

import java.security.PublicKey;
import java.io.IOException;

public class FalconPublicKey implements PublicKey {
	private byte[] encoded_key;

	public FalconPublicKey(byte[] encoded_key) {
		this.encoded_key = encoded_key;
	}

	public String getAlgorithm() {
		return FalconProvider.ALG_NAME;
	}

	public String getFormat() {
		return "X.509";
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
