# Manuel d'utilisateur

Ce projet est dans le cadre de la matière “Programmation Objet avancée”, formation Miage M1 en alternance, université PSL - Paris Dauphine.

Auteurs: **Nelson PROIA, Binh Minh NGUYEN**

# Présentation de l’application

## ****Quelques concepts à comprendre en avance****

Ce sont les concepts de base sur lesquels l'application s'appuie.

### La commande `traceroute/tracert`

Pour ce projet, nous utilisons exclusivement des adresses IP IPv4, en utilisant l'option “-4” par défaut. Pour plus d'informations sur la commande **`traceroute/tracert`**, veuillez consulter [ce lien](https://www.cloudns.net/blog/traceroute-command-tracert/#:~:text=Use%20the%20Traceroute%20command%20by,the%20Command%20Prompt%20on%20Windows.&text=On%20macOS%2C%20you%20can%20also,Then%20write%20Network%20Utility).

### Geolocalisation des IP adresses

Les IP adresses publiques peuvent être géolocalisées. Pour déterminer la géolocalisation des adresses IP, vous pouvez vous référer à [ce lien](https://whatismyipaddress.com/geolocation). Quelques services de géolocalisation populaires incluent:

- GeoIP2: [https://www.maxmind.com/en/home](https://www.maxmind.com/en/home)
- HostIP: [https://www.hostip.info/](https://www.hostip.info/)

### ****Graphe, distance minimale entre deux nœuds****

Les concepts de graphe et de distance minimale entre deux nœuds sont essentiels à comprendre. Pour plus d'informations, vous pouvez consulter les liens suivants:

- [https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)](https://en.wikipedia.org/wiki/Graph_(discrete_mathematics))
- [https://en.wikipedia.org/wiki/Shortest_path_problem](https://en.wikipedia.org/wiki/Shortest_path_problem)

### ****Adresses IP réservées et adresses IP valides****

Il est crucial de comprendre ce sont quoi et quels sont les adresses IP réservées et les adresses IP valides. Pour plus de détails, veuillez consulter les liens suivants:

[https://www.ipv4mall.com/blogs/valid-ipv4-address/](https://www.ipv4mall.com/blogs/valid-ipv4-address/)

[https://en.wikipedia.org/wiki/Reserved_IP_addresses](https://en.wikipedia.org/wiki/Reserved_IP_addresses)

## ****L'application "Internet Cartographier"****

Il s'agit d'une application qui permet aux utilisateurs d'exécuter des commandes traceroute vers de nombreuses cibles différentes. Les résultats renvoyés par ces exécutions de traceroute formeront un graphe non orienté d'Internet. Les nœuds et les arêtes de ce graphe seront affichés sur un globe 3D.

Chaque sommet du graphe sera une adresse IP. Il y aura une arête entre deux sommets A et B s'il y a un traceroute qui indique que A et B sont la suite. Dans le cas d'étoiles entre A et B, on considère que A et B sont reliés tout de même, mais cette arête sera étiquetée "indirecte" et sera affichée différemment sur le globe.

Nous permettons aux utilisateurs de choisir entre 3 métriques : constante, distance et temps de réponse. Le poids d'une arête peut être différent en fonction de la métrique choisie, à savoir :

- **1**, si on choisit la métrique "Constant"
- **La distance physique réelle entre ces deux nœuds/adresses IP**, si on choisit la métrique "Distance"
- **Le plus petit temps de réponse dans la ligne de résultat du deuxième nœud**, si on choisit la métrique "Temps de réponse"

**Exemple 1:**

![Untitled](https://github.com/Nelson-PROIA/POA_PROJET_INTERNET_CARTOGRAPHIER/assets/79254818/52e338e8-1950-4a4c-b72d-1c94f958e268)
- Noeud 1 : l'adresse IP de la hôte qui lance la commande traceroute (ce qui n'est pas affiché ici)
- Noeud 2: 10.1.3.254
- Noeud 3: 193.48.71.254
- Arête 1 : entre Noeud 1 et Noeud 2, ayant comme poids :
    - 1 pour la métrique Constant
    - La distance physique réelle entre l'adresse IP de la hôte qui lance la commande traceroute et l'adresse IP 10.1.3.254 pour la métrique Distance
    - 0.777 pour la métrique Temps de réponse
- Arête 2 : entre Noeud 2 et Noeud 3, ayant comme poids :
    - 1 pour la métrique Constant
    - La distance physique réelle entre l'adresse IP 10.1.3.254 et l'adresse IP 193.48.71.254 pour la métrique Distance
    - 1.659 pour la métrique Temps de réponse

**Exemple 2:**

![Untitled 1](https://github.com/Nelson-PROIA/POA_PROJET_INTERNET_CARTOGRAPHIER/assets/79254818/15106de5-377c-4cb6-bd10-442b4a526bd5)

Ce résultat implique 2 nœuds : 193.51.177.89 et 193.51.182.197 et une arête reliant ces 2 nœuds, étiquetée "indirecte".

# ****Comment récupérer l’application sur PC?****

- Clonez le dépôt de l'application en utilisant un gestionnaire de versions (comme Git).
- L'ensemble des librairies nécessaires au fonctionnement du projet étant trop lourd pour être stocké sur le dépôt, il vous faudra les télécharger via ce lien Google Drive : [libs](https://drive.google.com/drive/folders/1HDlpwNEFHvFhqPnZg4fPb01tPEJFqJvG?usp=sharing). Si vous n'y avez pas accès, faites en la demande. Téléchargez le sous dossier "lib" dans le dossier du projet.
- Lancer Main.java avec les VM Options indiqué dans le fichier VM_OPTIONS.txt
  
# ****Comment utiliser l’application + Fonctionnalités clés****

## ****Authentification****

Dès que vous exécutez l'application, l'écran de connexion apparaîtra. Les utilisateurs doivent se connecter pour accéder aux fonctionnalités principales. 

Actuellement, les utilisateurs doivent contacter un administrateur pour créer un compte. Cette fonctionnalité sera améliorée dans un avenir proche, permettant aux utilisateurs de s'inscrire eux-mêmes. 

Les utilisateurs peuvent également contacter l'administrateur pour récupérer les statistiques et les informations sur les graphes créés lors des leurs utilisations précédentes de l'application. Cette fonctionnalité sera améliorée dans un avenir proche, permettant aux utilisateurs d'afficher ces statistiques directement dans l'application.

## Page principale

Une fois connecté, l'utilisateur sera redirigé vers la page principale où se trouvent les principales fonctionnalités de l'application.

### Globe 3D

Sur la page principale, les utilisateurs verront à droite un globe 3D qui affiche les nœuds et les arêtes du graphe d'Internet construit. 

Au début, juste après le lancement de l'application, il y a un nœud en rouge sur le globe indiquant la position actuelle de l'utilisateur.

### ****Barre de menu à gauche : l’onglet “Targets”****

#### Ajouter de nouvelles cibles

Vous pouvez ajouter de nouvelles cibles à partir de l'onglet "Targets" de trois manières :

1. Ajouter 1 adresse IP : l'utilisateur peut saisir manuellement une adresse IP ou générer automatiquement une adresse IP aléatoire.
2. Ajouter 1 plage d'adresses IP : toutes les adresses IP d'une adresse IP à une autre.
3. Ajouter une liste d'adresses IP dans un fichier importé depuis l'ordinateur de l'utilisateur.

💡 ***Quelques contraintes à respecter lors de l'ajout des cibles*** :
1. *Les adresses IP entrées doivent être valides et ne doivent pas être réservées.*
2. *’adresse IP entrée dans “from” doit être inférieure à celle entrée dans “To”.*
3. *Le fichier importé doit être au format .csv, chaque ligne du fichier correspondant à une adresse IP valide et non réservée.*

Appuyez sur le bouton “Add to queue” pour ajouter les cibles dans la file d'attente. Ces cibles seront lancées automatiquement.

#### Configuration du lancement des traceroutes

Il y a trois paramètres à indiquer avant le lancement des traceroutes :

- Host name resolution : correspond à l’option “- d” de **`tracert`** et “- n” de **`traceroute`**.
- Max hops : correspond à l’option “- h” de **`tracert`** et “- m” de **`traceroute`**.
- Time out : correspond à l’option “- w” de **`tracert`** et “- w” de **`traceroute`**.

Il y a deux paramètres à indiquer concernant l’exécution des traceroutes :

- Simultaneous traceroute : Indiquez le nombre de traceroutes à lancer simultanément.
- Traceroute par minute : Indiquez le nombre de traceroutes à lancer dans une minute.

### ****Barre de menu à gauche : l’onglet “Queue”****

Affiche les listes des traceroutes avec leurs statuts :

- Pending : Liste des traceroutes qui viennent d’être ajoutées à partir de l’onglet “Targets”, en attente d’être lancées.
- Executing : Liste des traceroutes qui sont en train d’être exécutées.
- Executed : Liste des traceroutes qui ont été exécutées avec succès.
- Failed : Liste des traceroutes qui ont été exécutées sans succès.

Les résultats des traceroutes lancées avec succès seront transformés en un graphe. Ses nœuds et arêtes seront tracés sur le globe 3D. Les arêtes “indirectes” seront tracées sous forme de lignes pointillées.

### **Barre de menu à gauche : l’onglet “Statistics”**

Affiche les statistiques sur l’application, le graphe et les traceroutes.

### **Changer le service de géolocalisation**

Les informations de géolocalisation ainsi que la localisation globale d'un nœud sont déterminées par divers services de géolocalisation. Dans cette application, nous permettons aux utilisateurs de choisir entre 2 services, GeoIp2 et HostIp, via une liste déroulante au-dessus du globe.

Après que l’utilisateur a changé le service de géolocalisation, tous les nœuds et arêtes sur le globe seront mis à jour en fonction de leurs nouvelles coordonnées, générées par le nouveau service de géolocalisation choisi.

Les nœuds qui n’ont pas de coordonnées de géolocalisation, avec toutes leurs arêtes, ne seront pas affichés sur le globe.

### **Changer la métrique**

L’utilisateur peut changer la métrique via une liste déroulante au-dessus du globe.

Il y aura 3 options :

- “Constant”
- “Distance”
- “Time response”

### **Bulle d’info d’un nœud**

En tapant sur un nœud sur le globe, une bulle d’info liée à ce nœud apparaîtra, contenant toutes les informations de ce nœud, à savoir :

- Son adresse IP
- Sa position : Pays, ville
- Une liste déroulante de ses voisins ayant des coordonnées de géolocalisation. En tapant au-dessus d'un voisin, on y ira directement sur le globe.
- Une liste déroulante de tous les nœuds dans le graphe, ayant des coordonnées de géolocalisation. En tapant au-dessus d'un nœud, il y aura un pop-up indiquant la distance minimale entre le nœud source et le nœud choisi.
    - Cette distance diffère en fonction du service de géolocalisation et de la métrique choisie.
    - Quand la métrique est “Constant” ou “Time response”, le résultat/le calcul de la distance minimale est indépendant du choix du service de géolocalisation.

### **Barre de menu en haut à droite**

#### Chercher un nœud sur le globe

En appuyant sur l’icône de la loupe, une barre de recherche apparaîtra, permettant de chercher un nœud sur le globe.

En tapant sur un nœud dans la liste de nœuds trouvés, on ira directement sur le globe.

#### Gestion du compte

En appuyant sur l’icône de l’avatar, une liste d’options concernant la gestion du compte apparaîtra, à savoir les réglages du compte, l'affichage détaillé du profil de l’utilisateur et la déconnexion.

#### Notifications

En appuyant sur l’icône de la cloche, il y aura une liste de notifications permettant à l’utilisateur de suivre les états de l'application.

#### Réglage de l’application

En appuyant sur l’icône du dent de scie, on y trouve une option pour changer les réglages de l’application, comme la taille de l'écran, le son, etc.

#### Exporter/Importer

On pourrait trouver en appuyant sur l’icône du dent de scie aussi deux options:

- Exporter : Exporter le graphe complet généré dans cette session en 2 fichiers .csv. Un premier avec la liste des sommets, leur distance à l’origine et leurs coordonnées GPS (1 par ligne). Un second avec l’ensemble des couples de sommets reliés (1 couple par ligne).
    - Le fichier contenant la liste des sommets a des colonnes comme suit :
        - Node
        - Distance to source (constant)
        - Distance to source (real distance, HostIP)
        - Distance to source (real distance, GeoIP2)
        - Distance to source (Response Time)
        - GPS(HostIP)
        - Country(HostIP)
        - City(HostIP)
        - Is HostIP geo data computed already?
        - GPS(GeoIP2)
        - Country(GeoIP2)
        - City(GeoIP2)
        - Is GeoIP2 geo data computed already?
    - Le fichier contenant la liste des arêtes a des colonnes comme suit :
        - Node A
        - Node B
        - Is direct
        - Weight in real distance(HostIP)
        - Weight in real distance(GeoIP2)
        - Weight in response time
- Importer : Importer un graphe exporté dans une autre session et le rajouter dans le graphe actuel (importer tous les 2 fichiers : Le fichier contenant la liste des sommets et le fichier contenant la liste des arêtes).
    - Les fichiers doivent avoir les mêmes colonnes que celles dans les fichiers exportés.
    - Les nœuds et les arêtes de ce graphe importé seront ajoutés directement dans le graphe actuel (sans écraser les nœuds et les arêtes déjà existants).
    - Les données de géolocalisation de ces nouveaux nœuds seront générées par le service de géolocalisation actuellement choisi. Si ces données existent déjà dans le fichier, alors l'application les utilisera directement sans devoir les recalculer.
