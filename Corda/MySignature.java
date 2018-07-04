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
	private byte[] falcon_priv_key;
	private byte[] falcon_signature_nonce;
	private byte[] falcon_data_to_sign;

	private native byte[] do_falcon_sign(byte[] priv_key, byte[] data_to_sign);

	static {
		System.loadLibrary("falcon_jni");
	}

	public MySignature(String algorithm, Provider provider) throws NoSuchAlgorithmException {
		falcon_signature_nonce = new byte[40];

		if (algorithm.equals("Falcon")) {
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
			/* Choses spécifiques à mon algo :
			 * - Récupérer la clé privée
			 * - TODO : initialiser falcon avec falcon_sign_start() */
			falcon_priv_key = privateKey.getEncoded();
		}
	}

	public void update(byte[] data) throws SignatureException {
		if (isStandardSig) {
			sig.update(data);
		} else {
			/* Choses spécifiques à mon algo */
			falcon_data_to_sign = data;
		}
	}

	public byte[] sign() throws SignatureException {
		if (isStandardSig) {
			return sig.sign();
		} else {
			/* Choses spécifiques à falcon */
			byte[] tmp = do_falcon_sign(falcon_priv_key, falcon_data_to_sign);
			return tmp;
		}
	}
}
