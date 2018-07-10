package net.corda.core.crypto;

import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyFactorySpi;
import java.security.spec.KeySpec;
import java.security.InvalidKeyException;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;

public final class FalconKeyFactory extends KeyFactorySpi {
	public FalconKeyFactory() {
		super();
	}

	protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
		if (keySpec instanceof FalconPrivateKeySpec) {
			return new FalconPrivateKey(((FalconPrivateKeySpec) keySpec).getPrivateKey());
		} else if (keySpec instanceof PKCS8EncodedKeySpec) {
			return new FalconPrivateKey(((PKCS8EncodedKeySpec) keySpec).getEncoded());
		} else {
			throw new InvalidKeySpecException("Not a Falcon private key spec");
		}
	}

	protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
		if (keySpec instanceof FalconPublicKeySpec) {
			return new FalconPublicKey(((FalconPublicKeySpec) keySpec).getPublicKey());
		} else if (keySpec instanceof X509EncodedKeySpec) {
			return new FalconPublicKey(((X509EncodedKeySpec) keySpec).getEncoded());
		} else {
			throw new InvalidKeySpecException("Not a Falcon public key spec");
		}
	}

	protected Key engineTranslateKey(Key key) throws InvalidKeyException {
		if (key == null) {
			throw new InvalidKeyException("Key must not be null");
		}
		String keyAlg = key.getAlgorithm();
		if (!keyAlg.equals(FalconProvider.ALG_NAME)) {
			throw new InvalidKeyException("Not a Falcon key: " + keyAlg);
		}
		if (key instanceof PublicKey) {
			return translatePublicKey((PublicKey) key);
		} else if (key instanceof PrivateKey) {
			return translatePrivateKey((PrivateKey) key);
		} else {
			throw new InvalidKeyException("Neither a public nor a private key");
		}
	}

	protected <T extends KeySpec> T engineGetKeySpec(Key key, Class<T> keySpec) throws InvalidKeySpecException {
		try {
			key = engineTranslateKey(key);
		} catch (InvalidKeyException ike) {
			throw new InvalidKeySpecException(ike);
		}

		if ((key instanceof FalconPrivateKey) && (FalconPrivateKeySpec.class.isAssignableFrom(keySpec))) {
			return keySpec.cast(new FalconPrivateKeySpec(key.getEncoded()));
		}

		if ((key instanceof FalconPublicKey) && (FalconPublicKeySpec.class.isAssignableFrom(keySpec))) {
			return keySpec.cast(new FalconPublicKeySpec(key.getEncoded()));
		}

		throw new InvalidKeySpecException("Not a Falcon key spec");
	}

	private PublicKey translatePublicKey(PublicKey key) throws InvalidKeyException {
		if (key instanceof FalconPublicKey) {
			return key;
		} else {
			throw new InvalidKeyException("Neither a Falcon public key");
		}
	}

	private PrivateKey translatePrivateKey(PrivateKey key) throws InvalidKeyException {
		if (key instanceof FalconPrivateKey) {
			return key;
		} else {
			throw new InvalidKeyException("Neither a Falcon private key");
		}
	}
}
