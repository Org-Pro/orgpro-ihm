#!/bin/sh

total=java\ -classpath\ build\/classes\/java\/main\;libs\/\*\ fr.orgpro.ihm.project.Main

for i in "$*"
do
   total=$total\ $i
done

$total
