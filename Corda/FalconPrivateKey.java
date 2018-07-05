package net.corda.core.crypto;

import java.security.PrivateKey;

public class FalconPrivateKey implements PrivateKey {
	private static final String ALG_NAME = "Falcon-512";
	private static final String ENCODING = "None";
	private byte[] encoded_key;

	public FalconPrivateKey(byte[] encoded_key) {
		this.encoded_key = encoded_key;
	}

	public String getAlgorithm() {
		return ALG_NAME;
	}

	public String getFormat() {
		return ENCODING; 
	}

	public byte[] getEncoded() {
		return encoded_key;
	}
}
