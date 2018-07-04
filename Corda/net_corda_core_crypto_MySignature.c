#include "net_corda_core_crypto_MySignature.h"
#include "falcon/falcon.h"

JNIEXPORT jbyteArray JNICALL Java_net_corda_core_crypto_MySignature_do_1falcon_1sign(JNIEnv *env, jobject this, jbyteArray private_key, jbyteArray data_to_sign) {
	/* Récupération de la clé privée depuis l'objet Java */
	jbyte /* = signed char */ *sk = (*env)->GetByteArrayElements(env, private_key, NULL);
	jsize key_length = (*env)->GetArrayLength(env, private_key);
	jbyte *data = (*env)->GetByteArrayElements(env, data_to_sign, NULL);
	jsize data_length = (*env)->GetArrayLength(env, data_to_sign);
	/* Valeur retouvnée : 
	 * 40 premiers octets	: nonce
	 * 2049 octets suivants : signature falcon*/
	unsigned char buf[2089];
	size_t sig_len;
	jbyteArray ret = (*env)->NewByteArray(env, sizeof buf);

	/* Initialisatio de Falcon */
	falcon_sign *fs = falcon_sign_new();
	falcon_sign_set_private_key(fs, sk, key_length);
	falcon_sign_start(fs, buf);

	/* Mise à jour de la signature avec les données */
	falcon_sign_update(fs, data, data_length);

	/* Obtention de la signature */
	sig_len = falcon_sign_generate(fs, buf + 40, 2049, FALCON_COMP_STATIC);

	/* Sauvegarde de la signature */
	(*env)->SetByteArrayRegion(env, ret, 0, sizeof buf, (jbyte *) buf);

	/* Libération de la structure */
	falcon_sign_free(fs);

	return ret;
}
