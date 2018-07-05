
# [JNI](https://fr.wikipedia.org/wiki/Java_Native_Interface) (Java Native Interface)

## Résumé

- Interface qui petmet d'importer du code C dans Java
- Facilite beaucoup l'import mais reste délicate à comprendre

## Mode opératoire

### Modification dans la classe `FalconSignature`

Il nous faut modifier la classe pour qu'elle charge notre librairie C. Pour cela, on fait appel à la méthode `System.load()` (il est à noter que les fonctions qui seront appelées dans cette librairie sont déclarée dans la classe avec le mot-clé `native` et que le fichier `falcon_jni.so` mentionné n'existe pas encore, on va le créer).

```java
public abstract class FalconSignature extends SignatureSpi {
  /* [...] */
  
  /* Appels extérieurs à la librairie falcon */
  private native byte[] do_falcon_sign(byte[] priv_key, byte[] data_to_sign);
  private native int do_falcon_verify(byte[] public_key, byte[] signature);
  
  /* Chargement de la librairie falcon */
  static {
    /* TODO : Chemin fixe, à modifier pour avoir quelque chose de dynamique */
    System.load("~/corda/core/src/main/java/net/corda/core/crypto/falcon_jni.so");
  }
  
  /* [...] */
}
```

### Création du fichier `net_corda_core_crypto_FalconSignature.h`

Pour générer le header donnant les déclarations (en C) des méthodes `native`s de notre classe, il nous faut la compiler :

```
$ javac FalconSignature.java
$ 
```

Ensuite, pour générer le header, on se rend à la racine des sources, puis on exécute la commande `javah` :

```
$ cd ~/corda/core/src/main/java/
$ javah -jni net.corda.core.crypto.FalconSignature
$ mv ./net_corda_core_crypto_FalconSignature.h ./net/corda/core/crypto/
$ 
```

On obtient alors, dans le dossier `~/corda/core/src/main/java/net/corda/core/crypto/`, le [header](https://github.com/b1d0u/Stage_M1/blob/master/Corda/net_corda_core_crypto_MySignature.h) que notre code C va utiliser.

On considère à présent que notre bibliothèque [Falcon](https://falcon-sign.info) est déjà compilée (grâce à son Makefile) et que l'ensemble de ses fichiers se trouve dans le dossier `~/corda/core/src/main/java/net/corda/core/crypto/falcon/`.

On peut alors implémenter nos fonctions dans le fichier [net_corda_core_crypto_FalconSignature.c](https://github.com/b1d0u/Stage_M1/blob/master/Corda/net_corda_core_crypto_MySignature.c), puis compiler notre _shared library_ avec les commandes :

```
$ gcc -Wall -Werror -c -I"/Library/Java/JavaVirtualMachines/jdk1.8.0_171.jdk/Contents/Home/include/" -I"/Library/Java/JavaVirtualMachines/jdk1.8.0_171.jdk/Contents/Home/include/darwin/" -o net_corda_core_crypto_MySignature.o net_corda_core_crypto_MySignature.c
$ gcc -shared -o falcon_jni.so net_corda_core_crypto_FalconSignature.o falcon/falcon-enc.o falcon/falcon-vrfy.o falcon/frng.o falcon/shake.o falcon/falcon-fft.o falcon/falcon-keygen.o falcon/falcon-sign.o
```

> __Attention :__ Ici, l'exemple compile sur MacOS, donc les headers nécessaires à la compilation se trouvent dans `/Library/Java/JavaVirtualMachines/jdk1.8.0_171.jdk/Contents/Home/include/` et `/Library/Java/JavaVirtualMachines/jdk1.8.0_171.jdk/Contents/Home/include/darwin/` mais il faut remplacer ces valeurs si on compile sur un autre OS. Par exemple :
> 
> - __Windows :__ `C:\program files\java\jdk9.0.X\include` et `C:\program files\java\jdk9.0.X\include\win32`
> - __Linux (ubuntu) :__ `<CHEMIN_VERS_VOTRE_JAVA_HOME>\include\linux`
> - ...

On obtient enfin le fichier `falcon_jni.so` chargé dans notre classe Java !
