package net.corda.core.crypto;

public final class FalconLib {
	/* Appel extérieur à la librairie falcon */
	private static native byte[] do_falcon_keygen();

	static {
		System.load("/Users/Brieuc/Documents/Stage_M1/corda/core/src/main/java/net/corda/core/crypto/libfalcon.so");
	}

	private FalconLib() {
	}

	public static byte[] doFalconKeygen() {
		/* Création des clés */
		/**
		 * Valeur retournée par do_falcon_keygen() :
		 *
		 * [s_k|pk]
		 *
		 * s_k (2305 octets) 	= clé privée
		 * p_k (897 octets) 	= clé publique
		 **/
		return do_falcon_keygen();
	}
}
