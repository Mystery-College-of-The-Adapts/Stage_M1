package net.corda.core.crypto;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.KeyPairGenerator;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;

public class FalconKeyPairGenerator extends KeyPairGenerator {
	public FalconKeyPairGenerator() {
		super(FalconProvider.ALG_NAME);
	}

	public void initialize(int keysize, SecureRandom random) {
		/* Faire des choses ? */
	}

	public KeyPair generateKeyPair() {
		/* Création des clés */
		/**
		 * Valeur retournée par do_falcon_keygen() :
		 *
		 * [s_k|pk]
		 *
		 * s_k (2305 octets) 	= clé privée
		 * p_k (897 octets) 	= clé publique
		 **/
		byte[] val = FalconLib.doFalconKeygen();

		byte[] b_priv = new byte[2305];
		byte[] b_pub  = new byte[897];

		/* Recopie des clés */
		for (int i=0; i<2305; i++) {
			b_priv[i] = val[i];
		}
		for (int i=0; i<897; i++) {
			b_pub[i] = val[2305 + i];
		}

		/* Encodage des clés */
		PrivateKeyInfo pki;
		SubjectPublicKeyInfo spki;
		KeyPair kp;
		AlgorithmIdentifier ai = new AlgorithmIdentifier(FalconProvider.FALCON_KEY);

		try {
			pki = new PrivateKeyInfo(ai, new DERBitString(b_priv));
			spki = new SubjectPublicKeyInfo(ai, new DERBitString(b_pub));
			kp = new KeyPair(new FalconPublicKey(spki.getEncoded()), new FalconPrivateKey(pki.getEncoded()));
		} catch (IOException ioe) {
			System.err.println(ioe.getCause());
			return null;
		}

		/* Création de la paire */
		System.out.println("[FalconKeyPairGenerator] New Falcon pair generated :\nPrivate : " + kp.getPrivate() + "\n + Public : " + kp.getPublic());

		return kp;
	}

	public KeyPair generateKeyPairFromEntropy(BigInteger bigint) {
		return generateKeyPair();
	}
}
