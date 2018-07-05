package net.corda.core.crypto;

import java.security.Provider;
import java.security.Provider.Service;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public final class FalconProvider extends Provider {
	public static final ASN1ObjectIdentifier FALCON_KEY = new ASN1ObjectIdentifier("2.25.30086077608615255153862931087626791004");
	public static final String PROVIDER_NAME = "Falcon";

	public FalconProvider() {
		super("Falcon", 1.0, "Falcon provider v1.0, implementing Falcon key generation, signing " +
				"and signature verification");
		put("Signature.Falcon-512", "net.corda.core.crypto.FalconSignature");
		put("KeyPairGenerator.Falcon-512", "net.corda.core.crypto.FalconKeyPairGenerator");
	}
}
