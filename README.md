# Tiny Tanks

Projet de programmation L2 de l'Université de Paris 2021/2022



## Présentation rapide du jeu
Tiny tanks est un jeu d’action en 2 dimensions où le but est de détruire les tanks ennemis.
Notre jeu est une reprise du jeu original « Tiny Tanks »

### Build le projet
1. Clonez le dépôt
2. Ce placer dans le dossier tiny-tanks: `cd tiny-tanks`
3. Build la version souhaitée:
    1. La version .exe: `./gradlew createExe` le fichier build se trouve dans build\launch4j
    2. La version .jar: `./gradlew build` le fichier build se trouve dans build\libs


## Comment lancer le jeu
Vous pouvez lancer le jeu de 2 façon :
- lancez la commande ./gradlew run dans le dossier tiny-tank
- ou bien télécharger une des deux version (.jar ou .exe) qui se trouvent dans le dossier doc puis l'executer en double cliquant.

## Comment naviguer en jeu
Une fois en jeu vous avez 4 différents menus.
Vous pouvez lancer la campagne qui est le mode « Histoire » du jeu, c’est une suite de niveaux qui ont été fait par nos soins avec une difficulté croissante et un nombre de vie limité que vous gardez entre les niveaux.

Le 2e menu « Niveaux » représente le mode libre du jeu, vous pouvez y rejouer les niveaux déjà fini dans la campagne. Vous pouvez aussi si vous le voulez créer vos propres niveaux jouables. Ils seront stockés en local sur votre ordinateur et facilement modifiables.

Le 3e menu récapitule les différents succès disponibles et atteints dans le jeu. Il y est aussi dit comment les débloquer.

Ou alors pouvez aller dans les paramètres pour régler différents paramètres allant du volume du son du jeu jusqu’à la reconfiguration des touches.

## Le but du jeu
Lors d’une partie vous vous déplacez avec les flèches directionnelles et visez avec la souris, le niveau se termine quand vous vous faites toucher ou lorsque tous les ennemis ont été détruits.


## Comment créer des niveaux 

### En jeu
Au niveau de la création de niveaux, une fois dans le menu « Niveaux » il suffit de cliquer sur l’icone stylo en haut à gauche de l’écran.
Cela vous ouvre un autre menu avec le plateau de 23x16 cases.

L’écran est divisé en 2 parties, le plateau et les éléments. Pour placer des éléments il vous suffit de les sélectionner dans la barre en bas de l’écran et de cliquer sur la/les cases que vous voulez remplir.
Le plateau possède 2 dimensions différentes qui sont changeable en bas à gauche.
La section « Background » gère uniquement la partie décor .
Les décors sont purement décoratifs et n’impactent aucunement la jouabilité.
Tandis que la partie « Foreground » gère celle des obstacles et des joueurs, tout ce qui interagit avec le joueur en somme.
Pour finir, vous pouvez cocher la case "Activer le raid Aerien" pour activer la possibilité de subir des attaques venant du ciel pendant la partie.

### Manuellement
Vous pouvez aussi éditer manuellement vos niveaux. Ils sont stockés dans votre dossier « user/.tinyTank/Maps »
Il vous suffit donc d’aller dans le dossier du nom de votre niveau et de modifier le fichier .yaml.
Dans ce fichier sont présentés deux rectangles encadrés de « # » un représentant le « background » du niveau (bg), l’autre représentant la partie « foreground » (fg).

Pour modifier un niveau il vous suffit de remplacer les caractères présents par une des lettres (ou l’espace vide) présentées dans le fichier. Chaque caractère correspond à un élément en particulier, ils vous sont listés à l’ouverture d’un des fichiers.


Dans la partie « background » il est nécessaire de remplir tout le cadre par des éléments herbe ou sable, sinon votre carte sera incorrecte.

Dans la partie « foreground » vous n’êtes pas obligé de remplir tout le cadre, l’espace vide représentant l’absence d’obstacle.
Pour finir vous devez préciser si vous voulez activer ou non les raid aérien en dessous du cadre "foreground".

/!\ Chaque niveau doit posséder au minimum 1 joueur et 1 ordinateur.
Vous devez cependant limiter chaque carte à maximum 1 Joueur et 1 Boss
Il est aussi interdit d’entrer des éléments du « background » dans le « foreground » et inversement.
Votre carte doit toujours faire du 23x16
Si une de ces conditions n’est pas remplie votre carte sera considérée comme incorrecte et n’apparaitra pas en jeu.

(Exemple de présentation de fichier niveau en fin de document)

### Librairies utilisées :
Pour ce jeu nous avons utilisé plusieurs librairies différentes : 
- Jackson Yaml qui nous permet d’analyser facilement des fichiers yaml qui sont utilisés pour stocker les informations des niveaux et aussi de les créer facilement 
- log4j qui nous permet d’avoir des logs pour déboguer le jeu , mais aussi en cas de problème pour remonter facilement les problèmes des utilisateurs
- Spring Core qui permet de récupérer la liste des fichiers dans le dossier ressources pour l’utiliser après la compilation en .jar
- JamePad qui permet de récupérer des contrôleurs externes tel que des manettes et leurs mouvements
- sdl2gdx qui est une dépendance pour utiliser JamePad
- mockneat qui nous permet de traiter les probabilités pour l’apparition des bonus
- commons-io qui permet d’effacer plus rapidement des fichiers



### Exemple de niveau personnalisé

```
#BackGround:
# H : Herbe
# S : Sable

#ForeGround:
#   : Supprimer
# M : Mur
# N : Mur Cassable
# B : Buisson Vert
# C : Buisson Orange
# T : Trou
# J : Joueur
# R : Bot
# Z : Boss
bg: |-
  #########################
  #HHHHHHHHHHHSSSSSSSSSSSS#
  #HHHHHHHHHHHSSSSSSSSSSSS#
  #HHHHHHHHHHHSSSSSSSSSSSS#
  #HHHHHHHHHHHSSSSSSSSSSSS#
  #HHHHHHHHHHHSSSSSSSSSSSS#
  #HHHHHHHHHHHSSSSSSSSSSSS#
  #HHHHHHHHHHHSSSSSSSSSSSS#
  #HHHHHHHHHHHSSSSSSSSSSSS#
  #HHHHHHHHHHHSSSSSSSSSSSS#
  #HHHHHHHHHHHSSSSSSSSSSSS#
  #HHHHHHHHHHHSSSSSSSSSSSS#
  #HHHHHHHHHHHSSSSSSSSSSSS#
  #HHHHHHHHHHHSSSSSSSSSSSS#
  #HHHHHHHHHHHSSSSSSSSSSSS#
  #HHHHHHHHHHHSSSSSSSSSSSS#
  #HHHHHHHHHHHSSSSSSSSSSSS#
  #########################
fg: |-
  #########################
  #                       #
  #                       #
  # J  MMMMMMM   R        #
  #          N            #
  #          N     R      #
  #          N            #
  #          N            #
  #          NCCCCC       #
  #          TBBBBB       #
  #          T            #
  #          T      Z     #
  #          T            #
  #                       #
  #                       #
  #                       #
  #                       #
  #########################
  raid: true
```


Merci d’avoir lu, et bon jeu.

