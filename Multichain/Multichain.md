# Multichain

## Résumé

- Fork du Bitcoin Core
- Permet de créer des __blockchains privées__ et implémente un système de création de biens, d'échange de biens ainsi que de d'offre pour une certaine quantité de biens.
- Système de __permissions__ (connexion, création de monnaie, envoi/réception de biens, minage, ...)
- Il est possible de créer des __flux de données__ (streams) qui ne représentent pas des biens mais dont l'envoi et la réception se font avec des transactions, qui ne peuvent être lue que par ceux qui y sont autorisés (qui y sont __abonnés__)
- Assure l'__intimité__ (privacy) des transaction dans une blockchain privée
- Pas (vraiment) de PoW (sur les blockchains privées) ! Utilise à la place un système de __rotations__ des mineurs selon une constante appelée __diversité__. Chaque mineur vérifie les transactions dans le block et le valide (fait tout de même une PoW mais avec une difficulté minime)
- Codé majoritairement en __C++__ et en __C__
- Le plus compliqué a été de __compiler__ le code, utilisation pour cela de Docker et d'un __Dockerfile__ (voir [Installation](#installation))

## Fichiers intéressants du [repo](https://github.com/MultiChain/multichain)

- Fichier [src/keys/key.cpp](https://github.com/MultiChain/multichain/blob/master/src/keys/key.cpp) et son [header](https://github.com/MultiChain/multichain/blob/master/src/keys/key.h) :

Contiennent la classe `CKey` qui est ensuite utilisée partout pour représenter la __clé privée__ et implémente toutes les fonctions liées à ce type de clés :

```c++
/*** Dans key.h ***/

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

public:
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

On y trouve également les fonctions d'initialisation et d'opérations liées à l'ECC :

```c++
/*** Dans key.h ***/

/** Initialize the elliptic curve support. May not be called twice without calling ECC_Stop first. */
void ECC_Start(void);

/** Deinitialize the elliptic curve support. No-op if ECC_Start wasn't called first. */
void ECC_Stop(void);

/** Check that required EC support is available at runtime. */
bool ECC_InitSanityCheck(void);
```

C'est donc l'implémentation de ces fonctions dans [key.cpp](https://github.com/MultiChain/multichain/blob/master/src/keys/key.cpp) qu'il faut modifier, ce qui ne devrait pas trop poser de problème.

- Fichier [src/keys/pubkey.cpp](https://github.com/MultiChain/multichain/blob/master/src/keys/pubkey.cpp) et son [header](https://github.com/MultiChain/multichain/blob/master/src/keys/pubkey.h) :

Contiennent la classe `CPubKey` qui est ensuite utilisée partout pour représenter la __clé publique__ et implémente toutes les fonctions liées à ce type de clés :

```c++
/*** Dans pubkey.h ***/

class CPubKey
{
private:
	/**
	 * Just store the serialized data.
	 * Its length can very cheaply be computed from the first byte.
	 */
	unsigned char vch[65];

	//! Compute the length of a pubkey with a given first byte.
	unsigned int static GetLen(unsigned char chHeader)
	{
		if (chHeader == 2 || chHeader == 3)
			return 33;
		if (chHeader == 4 || chHeader == 6 || chHeader == 7)
			return 65;
		return 0;
	}

	//! Set this key data to be invalid
	void Invalidate()
	{
		vch[0] = 0xFF;
	}

public:
	/* [...] */

	/**
	 * Verify a DER signature (~72 bytes).
	 * If this public key is not fully valid, the return value will be false.
	 */
	bool Verify(const uint256& hash, const std::vector<unsigned char>& vchSig) const;

	/* [...] */

	//! Derive BIP32 child pubkey.
	bool Derive(CPubKey& pubkeyChild, ChainCode &ccChild, unsigned int nChild, const ChainCode& cc) const;

```

C'est donc l'implémentation de ces fonctions dans [pubkey.cpp](https://github.com/MultiChain/multichain/blob/master/src/keys/pubkey.cpp) qu'il faut modifier, ce qui ne devrait pas trop poser de problème non plus.

##  À regarder

- Encodage/décodage de clés (sûrement des choses à modifier de ce côté)

## Installation

- Télécharger et installer Docker (Community Edition) depuis le [site officiel](https://www.docker.com/get-docker)
- Installer git (`apt-get install git`)
- Cloner le git de Multichain :
```
$ git clone https://github.com/MultiChain/multichain
Cloning into 'multichain'...
remote: Counting objects: 6039, done.
remote: Compressing objects: 100% (79/79), done.
remote: Total 6039 (delta 58), reused 55 (delta 32), pack-reused 5928
Receiving objects: 100% (6039/6039), 3.83 MiB | 1.13 MiB/s, done.
Resolving deltas: 100% (4446/4446), done.
$ 
```
- Copier/coller le [Dockerfile](https://github.com/b1d0u/Stage_M1/blob/master/Multichain/Dockerfile) dans le dossier multichain fraîchement créé
- Lancer Docker
- Construire l'image, cette opération prend une quinzaine de minutes. Plusieurs warnings s'affichent pendant la compilation du code, majoritairement des `-Wunused-variable`, qui ne posent pas de problèmes normalement
```
$ docker build -t multichain:compiled .
Sending build context to Docker daemon 11.2MB
Step 1/6 : FROM ubuntu:16.04
---> 5e8b97a2a082
Step 2/6 : WORKDIR /
---> Using cache
---> 81f867d612a8
Step 3/6 : ADD . /multichain
---> a21db08a4e7b
Step 4/6 : RUN apt-get update && apt-get -y install build-essential libtool autotools-dev automake pkg-config libssl-dev libevent-dev bsdmainutils && apt-get -y install libboost-all-dev && apt-get -y install git && apt-get -y install software-properties-common && add-apt-repository ppa:bitcoin/bitcoin && apt-get update && apt-get -y install libdb4.8-dev libdb4.8++-dev && apt-get -y install nano && apt-get -y install curl
---> Running in e24d08f2697e

*Avalanche de téléchargements, d'installations et de compilations*

Removing intermediate container f6a328cab6bb
---> 5e8e02f5a555
Successfully built 5e8e02f5a555
Successfully tagged multichain:compiled
$ 
```
- On peut maintenant vérifier que notre image a bien été créée :
```
$ docker images
REPOSITORY  TAG       IMAGE ID      CREATED         SIZE
multichain  compiled  5e8e02f5a555  40 seconds ago  2.16GB
ubuntu      16.04     5e8b97a2a082  3 weeks ago     114MB
$ 
```
- Fini ! On peut maintenant lancer l'image et créer notre noeud Multichain :
```
$ docker run -it multichain:compiled /bin/bash
root@427a6ab78cdd:/multichain# multichain-util create my_chain

MultiChain 2.0 alpha 3 Utilities (latest protocol 20003)

Blockchain parameter set was successfully generated.
You can edit it in /root/.multichain/my_chain/params.dat before running multichaind for the first time.

To generate blockchain please run "multichaind my_chain -daemon".
root@427a6ab78cdd:/multichain# cat /root/.multichain/my_chain/params.dat
Affiche les paramètres de la chaîne qui s'apprête à être créée
root@427a6ab78cdd:/multichain# multichaind my_chain -daemon

MultiChain 2.0 alpha 3 Daemon (latest protocol 20003)

Starting up node...

Looking for genesis block...
Genesis block found

Other nodes can connect to this node using:
multichaind my_chain@172.17.0.2:2687

Listening for API requests on port 2686 (local only - see rpcallowip setting)

Node ready.

root@427a6ab78cdd:/multichain#
```
- Le noeud est alors lancé et prêt à recevoir des requêtes ! Pour une utilisation plus poussée, voir le [guide du site officiel](https://www.multichain.com/getting-started/).
