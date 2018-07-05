# Corda

## Avancée

- [x] Compréhension de la technologie (contexte d'utilisation, méthodes utilisées, ...)
- [x] Survol du code open source
- [x] Compilation du code "as is" (pas toujours évidente) et essai de l'implémentation
- [x] Recherche de "fichiers intéressants", i.e. trouver l'ensemble fichiers implémentant les algorithmes utilisés pour la signature ainsi que l'ensemble des cas où ces algorithmes sont appelés
- [x] Essais de recompilations en faisant des modifications mineures de ces fichiers, pour voir l'impact sur l'ensemble du projet et la difficulté de l'ajout d'une librairie C dans le code (utilisation de la librairie [falcon](https://falcon-sign.info))
- [ ] Une fois que tout fonctionne bien, modification en profondeur du code pour ajouter l'algorithme

## Logs

### La classe `java.security.Provider`

Le terme "Cryptographic Service Provider" (CSP) correspond à un package ou un ensemble de packages qui fournissent une implémentation concrète d'un sous ensemble des fonctionnalités cryptographiques de la _JDK Security API_. La classe `Provider` est l'interface vers ce package ou ensemble de packages. 

Pour fournir l'implémentation d'un service cryptographique, il faut écrire le code de l'implémentation et créer une sous-classe de la classe `Provider`. Le constructeur de cette sous-classe donne des valeurs à différentes propriétés (la _JDK Security API_ utilise ces valeurs pour regarder les services implémentés par le provider). En d'autres termes, la sous-classe donne les noms des classes implémentant les services.

```java
public class monProvider extends Provider {
	.
	.
	put("MessageDigest.SHA-256","com.foo.SHA256");
	.
}
```
```java
package com.foo;

public class SHA256 extends MessageDigestSpi {
	.
	.
}
```

