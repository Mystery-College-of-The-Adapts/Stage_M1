package net.corda.core.crypto;

import java.security.Signature;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.PrivateKey;
import java.security.SignatureException;
import java.security.InvalidKeyException;

public class MySignature {
	private Signature sig;
	private boolean isStandardSig;

	public MySignature(String algorithm, Provider provider) throws NoSuchAlgorithmException {
		if (algorithm.equals("Mon Algorithme")){
		/* Choses spécifiques à mon algo, initialisations etc. */
			this.isStandardSig = false;
		} else {
			sig = Signature.getInstance(algorithm, provider);
			this.isStandardSig = true;
		}
	}

	public void initSign(PrivateKey privateKey) throws InvalidKeyException {
		if (isStandardSig) {
			sig.initSign(privateKey);
		} else {
			/* Choses spécifiques à mon algo */
		}
	}

	public void update(byte[] data) throws SignatureException {
		if (isStandardSig) {
			sig.update(data);
		} else {
			/* Choses spécifiques à mon algo */
		}
	}

	public byte[] sign() throws SignatureException {
		if (isStandardSig) {
			return sig.sign();
		} else {
			/* Choses spécifiques à mon algo */
			return null;
		}
	}
}
