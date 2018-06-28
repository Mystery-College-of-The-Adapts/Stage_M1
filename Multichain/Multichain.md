# Multichain

## Résumé

- Fork du Bitcoin Core
- Permet de créer des __blockchains privées__
- Système de __permissions__ (connexion, création de monnaie, envoi/réception de monnaie, minage, ...)
- Assure l'__intimité__ (privacy) des transaction dans une blockchain privée
- Pas (vraiment) de PoW (sur les blockchains privées) ! Utilise à la place un système de __rotations__ des mineurs selon une constante appelée __diversité__. Chaque mineur vérifie les transactions dans le block et le valide (fait tout de même une PoW mais avec une difficulté très raisonnable)
- Codé majoritairement en __C et C++__.

## Fichiers intéressants du [repo](https://github.com/MultiChain/multichain)

- Fichier [src/keys/key.cpp](https://github.com/MultiChain/multichain/blob/master/src/keys/key.cpp) et son [header](https://github.com/MultiChain/multichain/blob/master/src/keys/key.h) :

Contiennent la classe `CKey` qui est ensuite utilisée partout pour représenter la __clé privée__.

```c++
class CKey
{
private:
	//! Whether this private key is valid. We check for correctness when modifying the key
	//! data, so fValid should always correspond to the actual state.
	bool fValid;
	
	//! Whether the public key corresponding to this private key is (to be) compressed.
	bool fCompressed;
	
	//! The actual byte data
	unsigned char vch[32];
	
	//! Check whether the 32-byte array pointed to be vch is valid keydata.
	bool static Check(const  unsigned  char* vch);

public :
	/* [...] */
	//! Simple read-only vector-like interface.
	unsigned int size() const { return (fValid ? 32 : 0); }
	const unsigned char* begin() const { return vch; }
	const unsigned char* end() const { return vch + size(); }
	//! Check whether this private key is valid.
	bool IsValid() const { return fValid; }

	//! Check whether the public key corresponding to this private key is (to be) compressed.
	bool IsCompressed() const { return fCompressed; }

	//! Initialize from a CPrivKey (serialized OpenSSL private key data).
	bool SetPrivKey(const CPrivKey& vchPrivKey, bool fCompressed);

	//! Generate a new private key using a cryptographic PRNG.
	void MakeNewKey(bool fCompressed);

	/**
	 * Convert the private key to a CPrivKey (serialized OpenSSL private key data).
	 * This is expensive.
	 */
	CPrivKey GetPrivKey() const;

	/**
	 * Compute the public key from a private key.
	 * This is expensive.
	 */
	CPubKey GetPubKey() const;

	/**
	 * Create a DER-serialized signature.
	 * The test_case parameter tweaks the deterministic nonce.
	 */
	bool Sign(const uint256& hash, std::vector<unsigned  char>& vchSig, uint32_t test_case = 0) const;
	
	/* [...] */
	
	/**
	 * Verify thoroughly whether a private key and a public key match.
	 * This is done using a different mechanism than just regenerating it.
	 */
	bool VerifyPubKey(const CPubKey& vchPubKey) const;

	/* [...] */
};
```
