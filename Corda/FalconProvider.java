package net.corda.core.crypto;

import java.security.Provider;
import java.security.Provider.Service;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public final class FalconProvider extends Provider {
	public static final ASN1ObjectIdentifier FALCON_KEY = new ASN1ObjectIdentifier("2.25.30086077608615255153862931087626791004");
	public static final String ALG_NAME = "FALCON512";
	public static final String PROVIDER_NAME = "Falcon";
	public static final String INFO = "Falcon provider v1.0, implementing Falcon key generation, signing and signature verification";

	public FalconProvider() {
		/* We are the Falcon provider */
		super("Falcon", 1.0, INFO);

		put("Signature.FALCON512", "net.corda.core.crypto.FalconSignature");
		put("KeyPairGenerator.FALCON512", "net.corda.core.crypto.FalconKeyPairGenerator");
		put("KeyFactory.FALCON512", "net.corda.core.crypto.FalconKeyFactory");
	}
}
