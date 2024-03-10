# Rapport de développement
Ce projet est dans le cadre de la matière “Programmation Objet avancée”, formation Miage M1 en alternance, université PSL - Paris Dauphine.

Auteurs: **Nelson PROIA, Binh Minh NGUYEN**

# ****Présentation de l’application + Manuels****

[Manuel d'utilisateur](https://github.com/Nelson-PROIA/POA_PROJET_INTERNET_CARTOGRAPHIER/blob/minh-model/docs/manuel_user.md)

# ****Choix des technologies****

*Étant donné que l'ensemble des bibliothèques nécessaires au fonctionnement du projet est trop lourd pour être stocké sur le dépôt, vous devrez les télécharger via ce lien Google Drive : [libs](https://drive.google.com/drive/folders/1HDlpwNEFHvFhqPnZg4fPb01tPEJFqJvG?usp=sharing). Si vous n'y avez pas accès, faites-en la demande.*

##

## ****Interface: JavaFX****

**Moderne et Très Populaire**: JavaFX est choisi pour son aspect moderne, offrant une interface utilisateur élégante et une expérience utilisateur agréable. Il est également largement adopté, assurant une base solide de support communautaire.

**Compétences Déjà Acquises**: La familiarité avec JavaFX est un atout. Les membres de l'équipe possèdent déjà des compétences dans cette technologie, facilitant ainsi le développement rapide et efficace de l'interface utilisateur.

**Très Stable**: JavaFX est réputé pour sa stabilité, garantissant une exécution fluide de l'application et minimisant les risques de bugs ou d'erreurs.

Java SDK : `17.0.8 openjdk` :

- Site : [https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).

## Globe 3D: Nasa World Wind Java

Utilisé pour la création d'un globe 3D interactif dans l'application, Nasa World Wind Java offre des fonctionnalités puissantes pour la visualisation des données géospatiales.

- Site : [https://worldwind.arc.nasa.gov/java/](https://worldwind.arc.nasa.gov/java/).
- GitHub : [https://github.com/NASAWorldWind/WorldWindJava/releases/tag/v2.2.1/](https://github.com/NASAWorldWind/WorldWindJava/releases/tag/v2.2.1/).

## Tests: Junit

JUnit est sélectionné en tant que framework de test en raison de sa stabilité et de son approche défensive. Des tests unitaires solides sont cruciaux pour garantir le bon fonctionnement de chaque composant de l'application.

- Junit : `5.10.0` :
    - Site : [https://junit.org/junit5/](https://junit.org/junit5/) & [https://junit.org/junit5/docs/current/user-guide/](https://junit.org/junit5/docs/current/user-guide/).
    - GitHub : [https://github.com/junit-team/junit5/](https://github.com/junit-team/junit5/).

## Services de géolocalisation

- [GeoIP2](https://dev.maxmind.com/geoip/geolite2-free-geolocation-data): On a choisit de mettre que la base de donnée des villes dans le projet, version “lite”, parce que dans le cadre du projet ça sera suffit.
- [HostIP](https://www.hostip.info/)

## ****Géolocalisation de l’IP Publique de l'Hôte****

**[Check IP Amazon AWS](http://checkip.amazonaws.com)**: Pour obtenir l'adresse IP publique de l'hôte, nous utilisons l'endpoint.

# **Structure du Projet**

## **Diagramme de Classe et d’Architecture**

Nous avons élaboré un diagramme de classe et d’architecture pour offrir une vision détaillée de la structure de notre projet. Pour le consulter, veuillez visiter le lien suivant sur Figma : [Diagramme sur Figma](https://www.figma.com/file/AxyONnTjZJQQQzgvUqqhL6/Internet-Cartographier---Diagrams?type=design&node-id=0-1&mode=design&t=nGY9hEbh5shXAaNY-0).

## **Choix de la Structure du Projet : MVC (Modèle-Vue-Contrôleur)**

Nous avons adopté le modèle MVC pour organiser notre projet de manière claire et modulaire. Cette approche permet de séparer distinctement les composants du projet, facilitant ainsi la maintenance et l'évolutivité du code.

- **Séparation du Code et des Tâches :** La structure MVC nous permet de diviser le code en trois composants principaux, chacun ayant des responsabilités spécifiques. Cette séparation facilite la collaboration et la gestion du code source.
- **Utilisation de JavaFX:** JavaFX a été choisi comme plateforme de conception pour tirer parti de son architecture MVC intégrée. Cela nous permet de créer une interface utilisateur moderne et réactive tout en maintenant une structure de code organisée.

# Design de l’interface

[Design de l’interface](https://www.figma.com/file/J17I2gVWh05HCVIE5auQhS/Internet-Cartographier---UX%2FUI-Design?type=design&node-id=6-330&mode=design&t=3GWM5EGWPmV5SLX7-0)

![Untitled](https://github.com/Nelson-PROIA/POA_PROJET_INTERNET_CARTOGRAPHIER/assets/79254818/676e8489-5061-4a80-8eca-f2ac2dfc2291)
![Untitled 1](https://github.com/Nelson-PROIA/POA_PROJET_INTERNET_CARTOGRAPHIER/assets/79254818/52e7cfdd-f3ed-47fb-a926-4929d0f10518)
![Untitled 2](https://github.com/Nelson-PROIA/POA_PROJET_INTERNET_CARTOGRAPHIER/assets/79254818/cf9bc7ea-0ec7-415a-bd77-f02daded6948)
![Untitled 3](https://github.com/Nelson-PROIA/POA_PROJET_INTERNET_CARTOGRAPHIER/assets/79254818/53b54142-1486-4c10-9adc-fd003038de9d)
![Untitled 4](https://github.com/Nelson-PROIA/POA_PROJET_INTERNET_CARTOGRAPHIER/assets/79254818/0ece9dd0-1fb8-4099-926b-59eb1dfefa35)

# Communication/Gestion du projet

Nous avons centralisé la gestion des tâches, des problèmes, et des questions sur GitHub, utilisant l'onglet "Issues". Cela nous permet de maintenir un suivi détaillé des éléments suivants :

- **Tâches à Réaliser :** La liste complète des tâches à accomplir est documentée sur GitHub, permettant à chaque membre de l'équipe de connaître les priorités et les objectifs du projet.
- **Problèmes et Bugs :** Les problèmes techniques, les bugs et les erreurs identifiés sont signalés dans la section des issues, avec des discussions sur les solutions possibles.
- **Questions et Remarques :** Toutes les questions et remarques concernant le projet sont enregistrées, facilitant ainsi les discussions et les prises de décision en équipe.

Nous utilisons divers canaux de communication pour favoriser une collaboration fluide et efficace entre les membres de l'équipe :

- **WhatsApp :** Pour une communication rapide et directe, notamment pour les échanges informels, les mises à jour instantanées et les discussions ponctuelles.
- **Échanges en Présentiel :** Des réunions en personne sont organisées pour discuter des aspects cruciaux du projet, résoudre des problèmes complexes et prendre des décisions stratégiques.
- **GitHub (onglet "Issues") :** En complément des autres canaux, les discussions importantes et les décisions sont également documentées sur GitHub, assurant une traçabilité et une référence facile.

![image](https://github.com/Nelson-PROIA/POA_PROJET_INTERNET_CARTOGRAPHIER/assets/79254818/2480a289-1bfb-4e8e-9951-ed18830c6aa3)
![image](https://github.com/Nelson-PROIA/POA_PROJET_INTERNET_CARTOGRAPHIER/assets/79254818/d9eb5e4a-337e-44a2-938d-4900eb420adc)
![image](https://github.com/Nelson-PROIA/POA_PROJET_INTERNET_CARTOGRAPHIER/assets/79254818/bd53a98e-1e2c-450d-b20a-2afd30dcff61)

Vous pourriez consulter nos échanges [ici](https://github.com/Nelson-PROIA/POA_PROJET_INTERNET_CARTOGRAPHIER/issues)

# ****Réponses à l’exigence de l’énoncé****

- [x]  ~~Transformer les résultats de traceroutes en graphe~~
- [x]  ~~Exécuter les commandes traceroutes depuis l’application~~
- [x]  ~~Exécuter plusieurs traceroutes en parallèle~~
- [x]  ~~Choisir à la volée le nombre de traceroutes simultanés~~
- [x]  ~~Choisir le nombre de traceroutes par minute (optionnel)~~
- [x]  ~~Ajouter à la volée de nouvelles cibles pour traceroute~~
- [x]  ~~Ajouter des cibles aléatoires~~
- [x]  ~~Changer de service de géolocalisation (entre GeoIp et HostIp)~~
- [x]  ~~HostIp - Effectuer un GET HTTP avec URLConnection, retour en XML, utilisation de SAX pour le parsing~~
- [ ]  Services supplémentaires (optionnel)
- [x]  ~~Visualiser des statistiques sur le graphe construit~~
- [x]  ~~Indiquer les voisins d’un sommet du graphe~~
- [x]  ~~Visualiser en temps réel le graphe sur le globe~~
- [x]  ~~Déterminer le plus court chemin dans votre graphe entre un point et l’origine, et entre deux points (optionnel)~~
- [x]  ~~Importer les résultats de traceroute effectués depuis une autre machine (optionnel)~~
- [x]  ~~L'utilisateur peut choisir le nombre maximum de sauts (hop) et le temps maximum d’attente par saut~~
- [x]  ~~Tests unitaires avec JUnit~~
- [x]  ~~Commentaires et Conventions de Codage (en anglais)~~
    - On a décidé de ne pas faire les tests et les commentaires pour les controlleurs et la vue, parce qu'il n'y a pas beaucoup de logiques intriques dans ces parties là
- [x]  ~~Projet compilant sans erreur et fonctionnant sur les machines de l’université~~
- [x]  ~~Gestion propre des différentes exceptions~~
- [x]  ~~La documentation ne doit pas être un copié-collé du code source du projet~~
- [x]  ~~Le projet doit être propre pour chaque binôme~~
- [x]  ~~L’utilisation d’un gestionnaire de version type CVS, SVN ou GIT est conseillée~~
- Le nom des variables, classes et méthodes doivent être choisis judicieusement
- Le code doit être propre, les exceptions correctement gérées, les classes correctement organisées en packages. La visibilité des méthodes et champs doit être pertinente (privée ou non...)

# Points clés/spéciaux du projet
*Ce sont les points qui, selon nous, rendent notre projet spécial et unique...*

## Choix du structure du graphe

Nous avons choisi de représenter notre graphe de la manière suivante :

- Un ensemble (**`Set`**) de nœuds (**`Nodes`**).
- Une carte (**`Map`**) représentant la liste d'adjacences. Les clés de la carte correspondent aux adresses IP, sous forme de chaînes de caractères, de tous les nœuds du graphe. Les valeurs associées à chaque clé sont des ensembles (**`Set`**) d'arêtes (**`Edges`**).

Nous avons opté pour cette représentation pour plusieurs raisons :

- Nous souhaitons que le type des clés d'une carte soit immutable. C'est pourquoi nous avons choisi d'utiliser des chaînes de caractères (**`String`**) plutôt que des nœuds (**`Node`**). Bien que nous aurions pu implémenter des modifications pour rendre les nœuds immuables, par précaution et afin d'éviter des erreurs potentielles, nous avons préféré utiliser des chaînes de caractères.
- C'est la raison pour laquelle nous avons ajouté un autre attribut de nœud, de type **`Set<Node>`**, afin de créer un dictionnaire de recherche permettant d'obtenir les informations d'un nœud à partir de son adresse IP.
- Nous avons choisi de représenter les valeurs de la liste d'adjacences sous forme de **`Set<Edge>`**. Dans ce projet, nous avons déterminé que nous aurions besoin de travailler et d'accéder à de nombreuses arêtes dans un graphe, car les arêtes jouent un rôle important. Elles contiennent une fonction relativement complexe qui calcule leur poids, ainsi que la mise à jour de la géolocalisation de leurs nœuds adjacents. En représentant les arêtes de cette manière, nous bénéficions d'un accès direct, plus facile et plus rapide, à une arête.

## **Cache**

En prévision de l'exécution simultanée de la commande de traceroute vers plusieurs cibles, créant ainsi un graphe avec de nombreux nœuds et arêtes, nous avons envisagé la possibilité que l'application fonctionne très lentement si nous devions recalculer à chaque fois les données de géolocalisation des nœuds ainsi que la distance la plus courte entre eux. C'est pourquoi nous avons mis en place un système de cache nous permettant de sauvegarder les données calculées, comprenant :

- Les données de géodonnées d'un nœud générées par chaque service de géolocalisation.
- La distance la plus courte entre un nœud et tous les autres nœuds restants de la carte, dans les 4 cas suivants :
    - Lorsque la métrique est “Constant” (le service de géolocalisation sélectionné n'affecte pas le résultat de la distance la plus courte entre 2 nœuds).
    - Lorsque la métrique est “Time response” (le service de géolocalisation sélectionné n'affecte pas les résultats de la distance la plus courte entre 2 nœuds).
    - Lorsque la métrique est “Distance” et que le service de géolocalisation est GeoIp2.
    - Lorsque la métrique est “Distance” et que le service de géolocalisation est HostIP.

Le cache sera réinitialisé à chaque fois qu'il y a :

- Ajout/suppression d'une arête du graphe.
- Suppression d'un nœud du graphe.

*(Ajout d'un nœud au graphe (cela ne réinitialise pas le cache s'il n'a aucune connexion avec les autres nœuds))*

Pour implémenter cette fonctionnalité, nous utilisons la structure de données Map.

![Untitled 5](https://github.com/Nelson-PROIA/POA_PROJET_INTERNET_CARTOGRAPHIER/assets/79254818/4b947392-2640-425f-bd8d-89729b053be4)

![Untitled 6](https://github.com/Nelson-PROIA/POA_PROJET_INTERNET_CARTOGRAPHIER/assets/79254818/59feb0eb-bc03-4610-88f6-8b26d3935119)

![Untitled 7](https://github.com/Nelson-PROIA/POA_PROJET_INTERNET_CARTOGRAPHIER/assets/79254818/27dfbe72-42a3-43ce-9877-d5a5906edc3b)

![Untitled 8](https://github.com/Nelson-PROIA/POA_PROJET_INTERNET_CARTOGRAPHIER/assets/79254818/4b2c86d5-9f16-4a03-a80d-2e7f22e77095)

### **Divers algorithmes de recherche du chemin le plus court**

Afin d'améliorer les performances de l'application, nous avons décidé d'utiliser deux algorithmes pour trouver la distance la plus courte entre deux nœuds : **BFS et Dijkstra**. BFS ne peut pas trouver la distance la plus courte dans un graphe pondéré, mais il est très rapide (complexité O(V+E)). D'autre part, Dijkstra peut trouver la distance la plus courte dans un graphe pondéré, même la distance la plus courte depuis la source jusqu'à tous les autres nœuds du graphe, mais il est plus lent (O(V^2)). Par conséquent, nous utilisons l'algorithme BFS lorsque la métrique est constante, et Dijkstra lorsque la métrique est le temps de réponse ou la distance. Tous les résultats renvoyés par ces algorithmes sont enregistrés en cache, et avec l'algorithme Dijkstra, le cache stocke même la distance minimale entre le nœud source et tous les autres nœuds du graphe.

### Classe “**Properties”**

Nous stockons les variables qui contiennent des valeurs telles que les points de terminaison dans le fichier **`config.properties`** du dossier des ressources, et utilisons la classe **Properties** pour accéder à ces variables. Il s'agit d'une bonne pratique en programmation Java, nous permettant de modifier la valeur des variables en dehors du code compilé.

[Documentation sur la classe Properties](https://docs.oracle.com/javase/8/docs/api/java/util/Properties.html)

### “**Design Pattern Singleton”**

Pour certaines classes pour lesquelles nous prévoyons qu'une seule instance existera à chaque exécution de l'application, et dont l'initialisation est coûteuse (par exemple, les classes qui doivent se connecter aux bases de données, aux ressources, etc.), telles que “GeoIP2”, “HostIP”, et “PropertiesSingleton”, nous avons choisi d'utiliser le modèle de conception “Singleton” pour éviter une initialisation répétée.

[Singleton Design Pattern](https://refactoring.guru/design-patterns/singleton)

### **Inscription/Connexion/Historique des exécutions**

Pour enrichir l'application, nous avons décidé d'ajouter une fonctionnalité de connexion et de stocker les statistiques des résultats des exécutions précédentes dans une base de données. Cependant, pour créer un compte et consulter l'historique, vous devez actuellement contacter l'administrateur. La base de données que nous utilisons est Firebase.

[Firebase](https://firebase.google.com/)

### **Interface animée et interactive**

C'est l'un des aspects sur lesquels nous avons consacré beaucoup de temps et dont nous sommes très fiers.

### **Changement de la métrique pour le poids de l'arête**

Nous donnons aux utilisateurs la possibilité de modifier la métrique. Chaque métrique différente apportera des valeurs de poids d'arête différentes, et donc les résultats des distances les plus courtes entre les nœuds seront également différents.

### Classe “**Optional”**

Nous utilisons la classe Optional pour représenter des variables/attributs qui peuvent logiquement être nuls. Il s'agit d'une bonne pratique en programmation Java, aidant les programmeurs à éviter les erreurs de type Nullpointerexception.

### **Autres fonctionnalités**

- Options pour la gestion de leur compte.
- Notifications
- Réglages de l'application
- Recherche d'un nœud sur le globe

# ****Comment nous avons abordé le projet****

Nous avons commencé par une étude approfondie du sujet, en examinant chaque point, fonctionnalité clé, et élément important.

Nous avons pris des décisions cruciales, telles que la structure du projet, les bibliothèques/technologies à utiliser, et les fonctionnalités à implémenter.

La conception de la structure du projet a suivi, avec la création de diagrammes de classes et la division claire des tâches.

Chacun de nous a travaillé sur sa partie assignée, développant sur une branche dédiée.

Les deux branches de travail individuelles ont été fusionnées, intégrant ainsi les contributions de chacun.

Nous avons consacré du temps à la correction des bugs et des erreurs, assurant la stabilité et la cohérence du projet.

La phase finale a impliqué la rédaction des rapports et la préparation de la présentation pour documenter notre travail et expliquer notre approche de manière détaillée.

# **Répartition des tâches**

**Nelson:**

- Conception de la structure du projet
- Élaboration du diagramme de classe
- Division des tâches
- Implémentation du contrôleur, du gestionnaire de traceroute et de la vue
- Intégration du modèle avec le contrôleur et la vue

**Minh:**

- Conception de l'interface utilisateur
- Implémentation du modèle, BDD, utiles (Path, Statistics, CsvWriter, CsvReader, …), et des tests
- Rédaction des rapports et préparation de la présentation

# ****Points à améliorer/développer****

- Permettre aux utilisateurs de créer leurs propres comptes sur l'application et d'afficher les statistiques de leurs comptes au lieu d'avoir à demander à l'administrateur.
- Ajouter d'autres services de géolocalisation en plus de GeoIP2 et HostIP.
- Amélioration de l'interface utilisateur.

# ****Difficultés et points intéressants****

## **Confusion sur les délais du projet**

Nous avons tous les deux fait l'erreur de penser que le projet devait être déposé le 21 décembre au soir au lieu du 19 décembre au soir. Dès que nous avons compris la date limite exacte, nous avons dû travailler de manière urgente et intensive les derniers jours pour respecter le délai.

## **Nouvelles technologies**

Aucun de nous n'avait d'expérience préalable en programmation avec des technologies telles que les traceroutes, les services de géolocalisation, les adresses IP, Globe 3D (Nasa World Wind Java), le format de fichier CSV, le format de données XML, le multi-threading, etc. Nous avons donc rencontré quelques difficultés au début en essayant d'apprendre et de nous habituer à ces technologies.

## **Absence de projets similaires en ligne**

Étant donné que notre projet était plutôt unique, il était difficile de trouver des projets similaires en ligne auxquels nous pouvions nous référer pour la structure, les approches et les bonnes pratiques. Nous avons dû élaborer notre propre solution à partir de zéro.

# Conclusion

Malgré les difficultés rencontrées, ce projet a été une opportunité d'apprentissage précieuse. Nous avons acquis une compréhension approfondie du fonctionnement des réseaux et amélioré nos compétences en programmation, communication et gestion de projet. 
De plus, nous avons constaté que les séances en classe nous ont beaucoup aidés pour ce projet. Nous avons utilisé bon nombre de connaissances acquises en classe, notamment les méthodes abstraites dans les énumérations, la sélection des structure de données appropriés, les concepts de classe interne, classe interne static, et les notions d'immuabilité et de mutabilité.
Dans l'ensemble, c'était un projet extrêmement intéressant et formateur, qui sera certainement bénéfique pour nos futures carrières.
