package net.corda.core.crypto;

import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.KeyPairGeneratorSpi;

public abstract class FalconKeyPairGenerator extends KeyPairGeneratorSpi {
	/* Appel extérieur à la librairie falcon */
	private native byte[] do_falcon_keygen();

	/* Chargement de la librairie falcon */
	static {
		/* TODO : Chemin fixe, à modifier pour avoir quelque chose de dynamique */
		System.load("/Users/Brieuc/Documents/Stage_M1/corda/core/src/main/java/net/corda/core/crypto/falcon_jni.so");
	}

	public FalconKeyPairGenerator() {
		super();
	}

	public void initialize(int keysize, SecureRandom random) {
		/* Faire des choses ? */
	}

	public KeyPair generateKeyPair() {
		/* Création des clés */
		/**
		 * Valeur retournée :
		 *
		 * [s_k|pk]
		 *
		 * s_k (2305 octets) 	= clé privée
		 * p_k (897 octets) 	= clé publique
		 **/
		byte[] val   = do_falcon_keygen();

		byte[] b_priv = new byte[2305];
		byte[] b_pub  = new byte[897];

		/* Recopie des clés */
		for (int i=0; i<2305; i++) {
			b_priv[i] = val[i];
		}
		for (int i=0; i<897; i++) {
			b_pub[i] = val[2305 + i];
		}

		/* Création de la paire */
		return new KeyPair(new FalconPublicKey(b_pub), new FalconPrivateKey(b_priv));
	}
}
