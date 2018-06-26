# Corda

## Résumé

- N'est pas une une blockchain !
- Généralisation de l'idée de smart contracts et de ledgers appliquée au monde du business et de l'entreprise
- Programmée en [Kotlin](https://kotlinlang.org), visant la JVM
- Les Dapps (appelées _CorDapps_) sont programmables en __Java__ ou en __Kotlin__

## Fichiers intéressants du [repo](https://github.com/corda/corda)

- Fichier [`core/src/main/kotlin/net/corda/core/crypto/Crypto.kt` :](https://github.com/corda/corda/blob/master/core/src/main/kotlin/net/corda/core/crypto/Crypto.kt)

Contient :

-- L'objet `Crypto` dans lequel sont définis les différents schémas de signature (à modifier donc pour en rajouter un nouveau) :

```kotlin
object Crypto {
  /* [...] */
  
  /** ECDSA signature scheme using the secp256k1 Koblitz curve and SHA256 for message hashing. */
  @JvmField
  val ECDSA_SECP256K1_SHA256 = SignatureScheme(
    2,   
    "ECDSA_SECP256K1_SHA256",
    AlgorithmIdentifier(X9ObjectIdentifiers.ecdsa_with_SHA256, SECObjectIdentifiers.secp256k1),
    listOf(AlgorithmIdentifier(X9ObjectIdentifiers.id_ecPublicKey, SECObjectIdentifiers.secp256k1)),
    cordaBouncyCastleProvider.name,
    "ECDSA",
    "SHA256withECDSA",
    ECNamedCurveTable.getParameterSpec("secp256k1"),
    256, 
    "ECDSA signature scheme using the secp256k1 Koblitz curve."
  )

  /* [...] */
  
  /* DEBUT AJOUT */
  
  @JvmField
  val MON_ALGO = SignatureScheme(
    7,   
    "NOM_DE_CODE_DE_MON_ALGO",
    AlgorithmIdentifier(ASN1ObjectIdentifier("OID_DE_MON_ALGO"), null/* paramètres de l'algorithmes (e.g. courbe pour ECDSA) */),
    listOf(AlgorithmIdentifier(ASN1ObjectIdentifier("OID_algo_alternatif"), "Paramètres_algo_alternatif")),
    "Nom du fournisseur de mon l'algo",
    "Nom de mon algo",
    "Nom de la signature",
    null/* spec de l'algorithme*/,
    256/* taille de la clé */, 
    "Description de mon algo."
  )

  /* FIN AJOUT */
  
  /* [...] */
}
```

Où l'objet `SignatureScheme` est défini dans le fichier [SignatureScheme.kt](https://github.com/corda/corda/blob/master/core/src/main/kotlin/net/corda/core/crypto/SignatureScheme.kt) du même dossier :

```kotlin
@KeepForDJVM
data class SignatureScheme(
	val schemeNumberID: Int,
	val schemeCodeName: String,
	val signatureOID: AlgorithmIdentifier,
	val alternativeOIDs: List<AlgorithmIdentifier>,
	val providerName: String,
	val algorithmName: String,
	val signatureName: String,
	val algSpec: AlgorithmParameterSpec?,
	val keySize: Int?,
	val desc: String
)
```

L'objet `Crypto` contient également une map à modifier :

```kotlin
private val signatureSchemeMap: Map<String, SignatureScheme> = listOf(
	RSA_SHA256,
	ECDSA_SECP256K1_SHA256,
	ECDSA_SECP256R1_SHA256,
	EDDSA_ED25519_SHA512,
	SPHINCS256_SHA256,
	COMPOSITE_KEY,
	MON_ALGO/* AJOUT */
).associateBy { it.schemeCodeName }
```

Ainsi que plusieurs fonctions liées aux clés (il nous faut créer une classe `MonAlgoPrivateKey` et une classe `MonAlgoPublicKey` qui héritent respectivement de `PrivateKey` et `PublicKey`) :

```kotlin
// Check if a private key satisfies algorithm specs.
private  fun validatePrivateKey(signatureScheme: SignatureScheme, key: PrivateKey): Boolean {
	return  when (key) {
		is BCECPrivateKey -> key.parameters == signatureScheme.algSpec
		is EdDSAPrivateKey -> key.params == signatureScheme.algSpec
		is BCRSAPrivateKey, is  BCSphincs256PrivateKey  ->  true  // TODO: Check if non-ECC keys satisfy params (i.e. approved/valid RSA modulus size).
		is MonAlgoPrivateKey /* AJOUT */ -> true
		else  ->  throw IllegalArgumentException("Unsupported key type: ${key::class}")
	}
}
```

```kotlin
// Check if a public key satisfies algorithm specs (for ECC: key should lie on the curve and not being point-at-infinity).
private fun validatePublicKey(signatureScheme: SignatureScheme, key: PublicKey): Boolean {
	return when (key) {
		is BCECPublicKey, is EdDSAPublicKey -> publicKeyOnCurve(signatureScheme, key)
		is BCRSAPublicKey -> key.modulus.bitLength() >= 2048 // Although the recommended RSA key size is 3072, we accept any key >= 2048bits.
		is BCSphincs256PublicKey, is MonAlgoPublicKey /* AJOUT */ -> true
		else -> throw IllegalArgumentException("Unsupported key type: ${key::class}")
	}
}
```

Q : Faut-it modifier ```toSupportedPublicKey(key: PublicKey)``` et ```toSupportedPrivateKey(key: PrivateKey)``` ?

## À regarder

- [(...)/crypto/internal/ProviderMap.kt](https://github.com/corda/corda/blob/master/core/src/main/kotlin/net/corda/core/crypto/internal/ProviderMap.kt) : ajouter l'algorithme dans la liste des _providers_ ?
- 
