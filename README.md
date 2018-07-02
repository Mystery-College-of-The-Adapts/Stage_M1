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
![h : {0,1}^* --> {0,1}^n, x |--> h(x)](https://latex.codecogs.com/svg.latex?h:\\{0,1\\}^*\rightarrow\\{0,1\\}^n,x\mapsto%20h(x))
telles que pour tout ![y € {0,1}^n](https://latex.codecogs.com/svg.latex?y\in%20\\{0,1\\}^n), il est calculatoirement impossible de trouver ![x € {0,1}^*](https://latex.codecogs.com/svg.latex?x\in%20\\{0,1\\}^*) tel que ![h(y)=x](https://latex.codecogs.com/svg.latex?h(y)=x), c’est-à-dire que trouver un tel ![x](https://latex.codecogs.com/svg.latex?x) n’est pas réalisable en temps raisonnable par un ordinateur classique.

- La __cryptographie asymétrique__ est fondée sur l’idée de posséder __deux clés__ pour chiffrer et déchiffrer une information entre deux entités : une clé __publique__ ![p_x](https://latex.codecogs.com/svg.latex?p_x), que tout le monde peut connaître et une clé __privée__ (ou secrète) ![s_x](https://latex.codecogs.com/svg.latex?s_x), connue uniquement de celui qui la possède.
En principe, si A veut communiquer avec B, A va chiffrer le message avec la clé publique ![p_b](https://latex.codecogs.com/svg.latex?p_b) de B et seul B, qui possède la clé secrète associée ![s_b](https://latex.codecogs.com/svg.latex?s_b), pourra déchiffrer le message. Cette cryptographie implique un autre principe :

- La __signature__ d’une information est le fait de chiffrer une information avec la clé secrète d’une entité. Ce procédé permet par exemple à B de prouver son identité auprès de A. Il lui suffit en effet d’envoyer à A un message clair (non chiffré) ![m](https://latex.codecogs.com/svg.latex?m) ainsi que le chiffré du message avec sa clé privée ![s_b](https://latex.codecogs.com/svg.latex?s_b). On note un tel chiffré ![{m}_sb](https://latex.codecogs.com/svg.latex?\\{m\\}_{s_b}).
B envoie donc à A le couple ![(m, {m}_sb)](https://latex.codecogs.com/svg.latex?(m,\\{m\\}_{s_b})) et A, qui connaît ![pb](https://latex.codecogs.com/svg.latex?p_b) peut appliquer le chiffrement avec cette clé pour vérifier l’égalité ![{{m}_sb}_pb = m](https://latex.codecogs.com/svg.latex?\\{\\{m\\}_{s_b}\\}_{p_b}=m) 
On considère en effet que pour tout couple de clés  ![(s_x, p_x)](https://latex.codecogs.com/svg.latex?(s_x,p_x)), ![{{m}_sx}_px} = {{m}_px}_sx} = m](https://latex.codecogs.com/svg.latex?\\{\\{m\\}_{s_x}\\}_{p_x}%20=%20\\{\\{m\\}_{p_x}\\}_{s_x}%20=%20m).

## Étude des blockchains

### Blockchains inspectées jusqu'ici

- Corda https://www.corda.net
- Hyperledger Fabric https://www.hyperledger.org/projects/fabric
- Multichain https://www.multichain.com
- Openchain https://www.openchain.org

### Mode opératoire

Pour chaque blockchain étudiée :

- [ ] Compréhension de la technologie (contexte d'utilisation, méthodes utilisées, ...)
- [ ] Survol du code open source
- [ ] Compilation du code "as is" (pas toujours évidente) et essai de l'implémentation
- [ ] Recherche de "fichiers intéressants", i.e. trouver l'ensemble fichiers implémentant les algorithmes utilisés pour la signature ainsi que l'ensemble des cas où ces algorithmes sont appelés
- [ ] Essais de recompilations en faisant des modifications mineures de ces fichiers, pour voir l'impact sur l'ensemble du projet et la difficulté de l'ajout d'une librairie C dans le code (utilisation de la librairie [falcon](https://falcon-sign.info))
- [ ] Une fois que tout fonctionne bien, modification en profondeur du code pour ajouter l'algorithme
