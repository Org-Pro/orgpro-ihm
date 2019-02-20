[![Build Status](https://travis-ci.org/Org-Pro/orgpro-ihm.svg?branch=master)](https://travis-ci.org/Org-Pro/orgpro-ihm)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/2bcdbf832d18455d8e3f8dcaceb63af2)](https://www.codacy.com/app/Trellorg/orgpro-ihm?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=Org-Pro/orgpro-ihm&amp;utm_campaign=Badge_Grade)
[![Coverage Status](https://coveralls.io/repos/github/Org-Pro/orgpro-ihm/badge.svg?branch=master)](https://coveralls.io/github/Org-Pro/orgpro-ihm?branch=master)
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://github.com/CodeChillAlluna/code-chill/blob/master/LICENSE)

# Guide d'installation

## Pour Linux

### Pré-Requis
Installer JAVA 8  
Les commandes :  
- sudo add-apt-repository ppa:webupd8team/java  
- sudo apt-get update  
- sudo apt-get install oracle-java8-installer  
 
Puis décompresser le projet
    
### Compilation
A partir du répertoire courant du projet :  
Puis :  
- chmod +x gradlew  
- ./gradlew build  
    
### Lancer l'application
A partir du répertoire courant du projet :  
Les commandes :  
- java -classpath build/classes/java/main:libs/* fr.orgpro.ihm.project.Main  
Ou :  
- alias op="java -classpath build/classes/java/main:libs/* fr.orgpro.ihm.project.Main"
    
## Pour Windows

### Pré-Requis
Installer JAVA 8 (Version 8)  
Via le site : <https://www.java.com/fr/download/>  

Puis décompresser le projet

### Compilation
A partir du répertoire courant du projet :  
Lancer le fichier "build.bat"  

Ou depuis un terminal :  
Les commandes :  
- gradlew.bat build

###  Lancer l'application
A partir du répertoire courant du projet :  
Lancer le fichier "run.bat"  

Ou depuis un terminal :  
Les commandes :  
- java -classpath build/classes/java/main;libs/* fr.orgpro.ihm.project.Main

### Connection à google

Afin de se connecter à l'api de google calendar : 

* Ajouter un dossier ```src/resources/"NomDuCollaborateur"/``` avec le nom du collaborateur utilisé dans orgpro
* Se rendre sur ```https://developers.google.com/calendar/quickstart/java``` puis cliquer sur Enable The Google Calendar API 
* Valider l'utilisation de l'api puis récupérer les configuration du client sous la forme de fichier en cliquant sur Download.
* Insérer le fichier credentials.json obtenu dans ```src/resources/"NomDuCollaborateur"/```
* Lors de la première connexion à google, le navigateur par défaut s'ouvrira et demandera la validation de l'utilisateur. 
* Une fois accepter un dossier token est créer automatiquement qui contient les identifiants pour se connecter
* Vous pouvez à présent envoyer des taches sur google

### Connection à Trello

Afin de se connecter à l'api de Trello : 

* Connectez vous à Trello sur votre navigateur
* Allez sur la page ```https://trello.com/app-key```
* Validez que vous souhaitez utiliser l'api de trello et cliquer sur afficher la clé. Mémoriser cette clé.
* Sur cette page cliquez sur ```générer manuellement un jeton.``` et autoriser l'utilisation du token. Un token de sera alors générer. Mémoriser le token.
* Dans OrgPro afin de rentrer ces identifiants taper :```col trello "nameCol" credentials "apikey" "token"``` . Remplacer nameCol, apiKey, token par les attributs correspondant
* Dans Orgpro taper ```col trello "nameCol" generate```. Cela permet à orgpro de générer le board qui sera utilisé par OrgPro pour stocker les taches 

