#include "net_corda_core_crypto_FalconSignature.h"
#include "net_corda_core_crypto_FalconKeyPairGenerator.h"
#include "falcon/falcon.h"
#include <stdlib.h>

JNIEXPORT jbyteArray JNICALL Java_net_corda_core_crypto_FalconSignature_do_1falcon_1sign(JNIEnv *env, jobject this, jbyteArray private_key, jbyteArray data_to_sign) {
	/* Récupération de la clé privée depuis l'objet Java */
	jbyte /* = signed char */ *sk = (*env)->GetByteArrayElements(env, private_key, NULL);
	jsize key_length = (*env)->GetArrayLength(env, private_key);
	/* Récupération des deonnées depuis l'objet Java */
	jbyte *data = (*env)->GetByteArrayElements(env, data_to_sign, NULL);
	jsize data_length = (*env)->GetArrayLength(env, data_to_sign);
	/**
	 * Valeur renvoyée : 
	 *
	 * 40 premiers octets	= nonce
	 * 1 octet suivant	= taille de la signature
	 * 2049 octets restants = signature falcon
	 **/
	unsigned char buf[2090];
	size_t sig_len;
	/* Résultat renvoyé à l'objet */
	jbyteArray res = (*env)->NewByteArray(env, sizeof buf);

	/* Initialisation de Falcon */
	falcon_sign *fs = falcon_sign_new();
	falcon_sign_set_private_key(fs, sk, key_length);
	falcon_sign_start(fs, buf);

	/* Mise à jour de la signature avec les données */
	falcon_sign_update(fs, data, data_length);

	/* Obtention de la signature */
	sig_len = falcon_sign_generate(fs, buf + 41, 2049, FALCON_COMP_STATIC);

	/* Sauvegarde de la taille de la signature //!\\ Elle doit être < 256 !!! */
	buf[40] = (unsigned char) sig_len;

	/* Sauvegarde de la signature */
	(*env)->SetByteArrayRegion(env, res, 0, sizeof buf, (jbyte *) buf);

	/* Libération de la structure */
	falcon_sign_free(fs);

	return res;
}


JNIEXPORT jint JNICALL Java_net_corda_core_crypto_FalconSignature_do_1falcon_1verify(JNIEnv *env, jobject this, jbyteArray public_key, jbyteArray signature) {
	/* Récupération de la clé publique depuis l'objet Java */
	jbyte *pk = (*env)->GetByteArrayElements(env, public_key, NULL);
	jsize key_length = (*env)->GetArrayLength(env, public_key);
	/* Récupération de la signature depuis l'objet Java */
	/**
	 * Forme de la signature : 
	 *
	 * 40 premiers octets	= nonce
	 * 1 octet suivant	= taille de la signature
	 * 2049 octets restants = signature falcon
	 **/
	jbyte *sig = (*env)->GetByteArrayElements(env, signature, NULL);
	/* Résultat renvoyé à l'objet */
	jint res = 0;
	
	/* Initialisation de Falcon */
	falcon_vrfy *fv = falcon_vrfy_new();
	falcon_vrfy_set_public_key(fv, pk, key_length);
	falcon_vrfy_start(fv, sig, 40 /* Longueur (fixée) du nonce */);
	falcon_vrfy_update(fv, sig + 41 /* signature */, sig[40] /* longueur signature */);

	res = falcon_vrfy_verify(fv, sig + 41, sig[40]);

	/* Libération de la structure */
	falcon_vrfy_free(fv);

	return res;
}

JNIEXPORT jbyteArray JNICALL Java_net_corda_core_crypto_FalconKeyPairGenerator_do_1falcon_1keygen(JNIEnv *env, jobject this) {
	jbyteArray res;
	/* Initialisation de Falcon */
	falcon_keygen *fk = falcon_keygen_new(9 /* log2(512) */, 0 /* pas de FFS ternaire */);
	size_t privkey_len = 2305;
	size_t pubkey_len = 897;
	size_t sum = privkey_len + pubkey_len;
	int val;
	/**
	 * Structure de buf :
	 *
	 * [sk|pk]
	 *
	 * Où :
	 *
	 * sk est la clé privée (2305 octets)
	 * pk est la clé publique (897 octets)
	 * */
	unsigned char *buf = malloc(sum);
	
	/* Génération des clés */
	val = falcon_keygen_make(fk, FALCON_COMP_STATIC, buf, &privkey_len,
		       		 buf + privkey_len, &pubkey_len);

	/* Résultat renvoyé à l'objet */
	res = (*env)->NewByteArray(env, sum);

	(*env)->SetByteArrayRegion(env, res, 0, sum, (jbyte *) buf);

	/* Libération de la structure */
	falcon_keygen_free(fk);
	free(buf);

	return res;
}
