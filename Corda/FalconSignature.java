package net.corda.core.crypto;

import java.security.Signature;
import java.security.Provider;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.security.SignatureSpi;

/* Falcon Signature Service Provider Interface */
public abstract class FalconSignature extends SignatureSpi {
	private byte[] falcon_key;
	private byte[] falcon_data_to_sign;

	/* Appels extérieurs à la librairie falcon */
	private native byte[] do_falcon_sign(byte[] priv_key, byte[] data_to_sign);
	private native int do_falcon_verify(byte[] public_key, byte[] signature);

	/* Chargement de la librairie falcon */
	static {
		/* TODO : Chemin fixe, à modifier pour avoir quelque chose de dynamique */
		System.load("/Users/Brieuc/Documents/Stage_M1/corda/core/src/main/java/net/corda/core/crypto/falcon_jni.so");
	}

	public FalconSignature() {
		super();
	}

	protected void engineInitSign(PrivateKey privateKey) throws InvalidKeyException {
		if (!(privateKey instanceof FalconPrivateKey)) {
			throw new InvalidKeyException("Not a Falcon private key !");
		}

		falcon_key = privateKey.getEncoded();
	}

	protected void engineInitVerify(PublicKey publicKey) throws InvalidKeyException {
		if (!(publicKey instanceof FalconPublicKey)) {
			throw new InvalidKeyException("Not a Falcon public key !");
		}

		falcon_key = publicKey.getEncoded();
	}

	protected void engineUpdate(byte[] data) {
		/* Choses spécifiques à Falcon */
		falcon_data_to_sign = data;
	}

	protected byte[] engineSign() {
		/**
		 * Valeur renvoyée :
		 *
		 * 40 premiers octets   = nonce
		 * 1 octet suivant      = taille de la signature
		 * 2049 octets restants = signature falcon
		 **/
		byte[] tmp = do_falcon_sign(falcon_key, falcon_data_to_sign);
		return tmp;
	}

	protected boolean engineVerify(byte[] signature) {
		int res = do_falcon_verify(falcon_key, signature);

		return (res != 0);
	}
}
