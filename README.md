# Corda

## Résumé

* N'est pas une une blockchain !
* Généralisation de l'idée de smart contracts et de ledgers appliquée au monde du business et de l'entreprise
* Programmée en [Kotlin](https://kotlinlang.org), visant la JVM
* Les Dapps (appelées _CorDapps_) sont programmables en __Java__ ou en __Kotlin__

## Fichiers intéressants du 

* Fichier `core/src/main/kotlin/net/corda/core/crypto/Crypto.kt` :

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
}
```

Contient :
