package net.corda.core.crypto;

import java.security.spec.KeySpec;

public class FalconPublicKeySpec implements KeySpec {
	private byte[] pub_encoded;

	public FalconPublicKeySpec(byte[] pub_encoded) {
		this.pub_encoded = pub_encoded;
	}

	public byte[] getPublicKey() {
		return pub_encoded;
	}
}
