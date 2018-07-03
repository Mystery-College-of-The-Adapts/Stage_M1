package net.corda.core.crypto

import java.security.PrivateKey
import org.bouncycastle.asn1.*

/** 
 * Représente une clé privée de Mon algo
 **/
class MonAlgoPrivateKey(key: ByteArray) : PrivateKey {

	override fun getAlgorithm() = "Mon Algorithme"

	override fun getFormat() = "IDK"

	override fun getEncoded(): ByteArray {
		val rv = ByteArray(32)
		/* Faire des trucs pour encoder */
		return rv
	}
}