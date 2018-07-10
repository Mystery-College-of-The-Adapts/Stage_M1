package net.corda.core.crypto;

import java.security.spec.KeySpec;

public class FalconPrivateKeySpec implements KeySpec {
	private byte[] priv_encoded;

	public FalconPrivateKeySpec(byte[] priv_encoded) {
		this.priv_encoded = priv_encoded;
	}

	public byte[] getPrivateKey() {
		return priv_encoded;
	}
}
