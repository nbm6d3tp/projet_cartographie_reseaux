<div align="justify">

# *Toutes les routes mènent à Mido*

## Introduction

Ce projet se concentre sur la création d'une cartographie en temps réel du réseau Internet à l'aide de la
commande `traceroute` (ou équivalent). Cette commande est utilisée pour collecter des données sur la connectivité entre
un nœud d'origine et un autre nœud du réseau. Les résultats sont composés de lignes numérotées, chaque ligne
correspondant à un routeur intermédiaire. Chaque ligne de résultat contient des informations essentielles telles qu'un
numéro de ligne, une adresse IP (parfois accompagnée d'un nom) et des temps de réponse. Une ligne peut comporter
plusieurs adresses IP, et cela est dû au fait que `traceroute` effectue trois essais pour chaque étape du parcours. Il
peut également y avoir des interruptions marquées par des étoiles (`*`), indiquant l'absence de réponse.

### Exemple d'exécution d'un `traceroute`

  ```
  traceroute to www.google.fr (173.194.66.94), 30 hops max, 60 byte packets
  1 10.1.3.254 (10.1.3.254) 0.782 ms 0.784 ms 0.777 ms
  2 lamsade-gw.lamsade.dauphine.fr (193.48.71.254) 1.661 ms 1.683 ms 1.659 ms
  3 192.168.2.1 (192.168.2.1) 1.658 ms 1.656 ms 1.932 ms
  4 interco-9.01-auteuil.rap.prd.fr (195.221.127.153) 2.389 ms 2.841 ms 2.385 ms
  5 pe-odeon.rap.prd.fr (195.221.125.25) 2.839 ms 2.836 ms 2.832 ms
  6 vl260-te4-4-paris1-rtr-021.noc.renater.fr (193.51.186.102) 3.534 ms 2.754 ms 2.703 ms
  7 te0-0-0-3-paris1-rtr-001.noc.renater.fr (193.51.177.24) 6.088 ms * *
  8 te0-6-0-2-paris2-rtr-001.noc.renater.fr (193.51.177.89) 4.067 ms 4.046 ms 195.760 ms
  9 * * *
  11 72.14.238.234 (72.14.238.234) 196.899 ms 202.685 ms 204.217 ms
  12 * 209.85.245.70 (209.85.245.70) 4.357 ms 2.992 ms
  13 216.239.51.198 (216.239.51.198) 8.168 ms 209.85.242.229 (209.85.242.229) 18.170 ms 209.85.248.202 (209.85.248.202) 8.000 ms
  14 72.14.238.215 (72.14.238.215) 7.659 ms 66.249.95.238 (66.249.95.238) 8.824 ms 216.239.49.45 (216.239.49.45) 8.721 ms
  15 * * *
  16 we-in-f94.1e100.net (173.194.66.94) 8.000 ms 7.446 ms 7.780 ms
  ```

## Objectif

L'objectif ici est de construire un graphe non orienté du réseau Internet en utilisant ces informations collectées. Dans
ce graphe, chaque sommet représente une adresse IP. Les arêtes du graphe relient deux sommets si ces deux adresses IP
apparaissent consécutivement dans les résultats d'une exécution de `traceroute`. Il est important de noter que le réseau
Internet est dynamique, ce qui signifie que les chemins entre les nœuds ne sont pas figés, et les résultats peuvent
varier en fonction du moment de leur exécution. Les chemins ne sont pas non plus nécessairement les plus courts.

## Stockage des informations

Chaque sommet du graphe conserve des informations telles que son adresse IP, ses voisins (déterminés grâce aux résultats
des `traceroute`) et la distance minimale à l'origine. La machine sur laquelle les `traceroute` sont lancés est
considérée comme l'origine. Ces données permettent de construire une représentation précise du réseau et de répondre à
des questions importantes sur la connectivité des nœuds.

## Interface utilisateur

Le projet inclut une interface utilisateur qui est mise en œuvre en utilisant `JavaFX`. Cette interface permet à
l'utilisateur de contrôler et de visualiser divers aspects du projet, tels que le lancement de nouveaux `traceroute`, la
modification des paramètres, l'affichage de statistiques, la géolocalisation des sommets du graphe et la visualisation
en temps réel sur un globe virtuel.

## Configurations des `traceroute`

Les paramètres de `traceroute`, tels que le nombre maximal de sauts (hops) et le timeout par saut, sont configurables
par l'utilisateur. Il est également recommandé de désactiver la résolution de noms DNS pour accélérer le processus. Il
est aussi possible de choisir le protocole utilisé, à savoir `UDP`, `TCP` ou `ICMP`.

## Services de géolocalisation

Deux services de géolocalisation sont utilisés dans le projet : `GeoIP` et `HostIP`. L'utilisateur a la possibilité de
choisir l'un ou l'autre de ces services pour éviter les conflits. La géolocalisation est cruciale pour attribuer des
coordonnées GPS aux adresses IP, ce qui permet de les positionner sur un globe virtuel. On pourra explorer d'autres
possibilités de services de géolocalisation.

## Fonctionnalités

Vous trouverez ci-dessous une liste des fonctionnalités principales du projet (liste non exhaustive) :

- Exécution de plusieurs `traceroute` en parallèle pour collecter rapidement des informations sur le réseau.
- Ajout de nouvelles cibles pendant l'exécution (à la volée), qu'elles soient spécifiées par adresse IP, nom, plage
  d'adresses ou à partir d'un fichier.
- Ajout de cibles aléatoires, tout en prenant soin d'exclure les adresses IP réservées.
- Possibilité de changer de service de géolocalisation à tout moment.
- Affichage de statistiques sur le graphe construit, y compris le nombre de sommets observés et le classement par
  distance.
- Identification des voisins d'un sommet du graphe, avec la possibilité de les mettre en évidence.
- Détermination du plus court chemin en termes de sommets entre un point et l'origine, ainsi que la possibilité de
  calculer le chemin entre deux points spécifiques.
- Exportation et importation de données sans écraser l'existant, sous forme de fichiers CSV, qui peuvent contenir des
  informations sur les sommets, leurs distances à l'origine et leurs coordonnées GPS.
- Importation des résultats de `traceroute` effectués depuis une autre machine, bien que la distance à l'origine puisse
  être recalculée en option.

## Points d'attention

Quelques points auxquels il faudra porter une attention particulière durant la mise en place de ce projet :

- Le réseau Internet est dynamique, ce qui signifie qu'il n'y a pas nécessairement un seul chemin le plus court pour
  atteindre un nœud, et ce chemin peut varier au fil du temps.
- Des cas spéciaux surviennent lorsque des étoiles (`*`) apparaissent entre deux nœuds A et B dans les résultats
  du `traceroute`. Il est essentiel de déterminer comment gérer graphiquement ces cas pour refléter la connectivité.
- Le projet doit pouvoir s'exécuter aussi bien sous `Linux` que sous `Windows` sans nécessiter de modification du
  programme.
- Lors de l'utilisation du service `HostIP`, il est __obligatoire__ d'utiliser la méthode XML.
- Il est essentiel de comprendre pourquoi plusieurs adresses IP peuvent apparaître sur une même ligne de `traceroute` et
  de définir une approche pour les gérer.
- La création de graphes en `Java` à l'aide de bibliothèques standard doit être explorée, et il peut être nécessaire de
  développer une implémentation personnalisée pour répondre aux besoins du projet.
- Différencier graphiquement les sommets qui ne sont pas directement connectés, mais sont séparés par des étoiles (`*`).
  Il faut également définir clairement ce que signifie "A et B à la suite" et comment représenter graphiquement ces cas.
- Il est crucial de déterminer précisément ce qui constitue un voisin dans le contexte du projet, car il peut y avoir
  une tendance à considérer uniquement les voisins directs. Une explication détaillée des étoiles dans les résultats
  du `traceroute` est nécessaire.
- Lors de la modification des paramètres, il est important de décider si cela affectera uniquement les `traceroute` à
  venir ou s'il est nécessaire de relancer tous les `traceroute` déjà en cours et ceux déjà terminés.
- L'exploration de services de géolocalisation alternatifs est un point à prendre en compte pour améliorer la précision
  de la cartographie.
- L'intégration de `JavaFX` pour la visualisation des statistiques et des graphiques doit être confirmée et évaluée en
  tant que composante importante de l'interface utilisateur du projet.

## Conclusion

Le projet *Toutes les routes mènent à Mido* est une entreprise complexe visant à cartographier et à comprendre la
connectivité du réseau Internet en temps réel. Les spécifications détaillées dans le sujet fournissent une base solide
pour la conception et la mise en œuvre de ce projet, qui repose largement sur la propreté du code, sa lisibilité et une
architecture bien pensée pour garantir son succès.

</div>
