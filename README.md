[![Build Status](https://travis-ci.org/Org-Pro/orgpro-ihm.svg?branch=master)](https://travis-ci.org/Org-Pro/orgpro-ihm)
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
