# Stage M1

## Définitions

Une __blockchain__ est un ensemble de __blocs__ chaînés les uns aux autres de telle sorte que la modification d’un bloc va compromettre (on dit qu’elle va "invalider") ce bloc ainsi que tous les blocs suivants dans la chaîne. Ainsi, chaque bloc contient une certaine quantité d’information qui, en principe, ne peut pas être modifiée une fois le bloc rajouté dans la chaîne.

Un __bloc__ est une structure de données constituée de deux éléments :

- Un __en-tête__ (que l’on appellera le __"header"__), il contient des informations sur le bloc ainsi que sur le bloc précédent, permettant d’assurer l’intégrité de la chaîne.
- Les __données__, empilées les unes à la suite des autres dans le bloc. Un bloc a donc la structure suivante (ce bloc est disponible parmi d’autres dans plusieurs plateformes Bitcoin) :

![Exemple d'un header de bloc](https://github.com/b1d0u/Stage_M1/blob/master/images/bloc_bitcoin.png)

## Sécurité de la blockchain

La sécurité de la blockchain est assurée par différents principes cryptographiques :

- Les __fonctions de hachage à sens unique__ sont des fonction de la forme :
$$h : \{0,1\}^* \rightarrow \{0,1\}^n, x \mapsto h(x)$$
telles que pour tout $y \in \{0, 1\}^n$, il est calculatoirement impossible de trouver $x \in \{0,1\}^*$ tel que $h(x) = y$, c’est-à-dire que trouver un tel $x$ n’est pas réalisable en temps raisonnable par un ordinateur classique.

- La __cryptographie asymétrique__ est fondée sur l’idée de posséder __deux clés__ pour chiffrer et déchiffrer une information entre deux entités : une clé __publique__ $p_x$, que tout le monde peut connaître et une clé __privée__ (ou secrète) $s_x$, connue uniquement de celui qui la possède.
En principe, si A veut communiquer avec B, A va chiffrer le message avec la clé publique $p_b$ de B et seul B, qui possède la clé secrète associée $s_b$, pourra déchiffrer le message. Cette cryptographie implique un autre principe :

- La __signature__ d’une information est le fait de chiffrer une information avec la clé secrète d’une entité. Ce procédé permet par exemple à B de prouver son identité auprès de A. Il lui suffit en effet d’envoyer à A un message clair (non chiffré) $m$ ainsi que le chiffré du message avec sa clé privée $s_b$. On note un tel chiffré $\{m\}_{s_b}$.
B envoie donc à A le couple ![(m, {m}_sb)](https://latex.codecogs.com/svg.latex?(m,%20\\{m\\}_{s_b})) et A, qui connaît ![pb](https://latex.codecogs.com/svg.latex?p_b) peut appliquer le chiffrement avec cette clé pour vérifier l’égalité ![{{m}_sb}_pb = m](https://latex.codecogs.com/svg.latex?\\{\\{m\\}_{s_b}\\}_{p_b}%20=%20m) 
On considère en effet que pour tout couple de clés  ![(s_x, p_x)](https://latex.codecogs.com/svg.latex?(s_x,p_x)), ![{{m}_sx}_px} = {{m}_px}_sx} = m](https://latex.codecogs.com/svg.latex?\\{\\{m\\}_{s_x}\\}_{p_x}%20=%20\\{\\{m\\}_{p_x}\\}_{s_x}%20=%20m)
