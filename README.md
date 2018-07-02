# Stage M1

Blockchains regardées jusqu'ici :

- Openchain https://www.openchain.org
- Multichain https://www.multichain.com
- Corda https://www.corda.net

## Définitions

### Blockchain

Une __blockchain__ est un ensemble de __blocs__ chaînés les uns aux autres de telle sorte que la modification d’un bloc va compromettre (on dit qu’elle va "invalider") ce bloc ainsi que tous les blocs suivants dans la chaîne. Ainsi, chaque bloc contient une certaine quantité d’information qui, en principe, ne peut pas être modifiée une fois le bloc rajouté dans la chaîne.

Un __bloc__ est une structure de données constituée de deux éléments :

- Un __en-tête__ (que l’on appellera le __"header"__), il contient des informations sur le bloc ainsi que sur le bloc précédent, permettant d’assurer l’intégrité de la chaîne.
- Les __données__, empilées les unes à la suite des autres dans le bloc. Un bloc a donc la structure suivante (ce bloc est disponible parmi d’autres dans plusieurs plateformes Bitcoin) :

![Exemple d'un header de bloc](https://github.com/b1d0u/Stage_M1/blob/master/images/bloc_bitcoin.png)

