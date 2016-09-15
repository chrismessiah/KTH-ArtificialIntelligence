#!/bin/bash

# javac HelloWorld.java; 
# java Helloworld;

if javac -cp . Main.java;
then
   echo "Compilation successful!"
   java Main
else
   echo "Compilation failed."
fi