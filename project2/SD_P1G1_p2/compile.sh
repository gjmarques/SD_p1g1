#!/bin/bash

echo "Compiling..."
#compile entities mains
javac clientSide/PassengerMain.java
javac clientSide/BusDriverMain.java
javac clientSide/PorterMain.java
# compile shared regions mains
javac serverSide/ALMain.java
javac serverSide/ATEMain.java
javac serverSide/ATTQMain.java
javac serverSide/BCPMain.java
javac serverSide/BROMain.java
javac serverSide/DTEMain.java
javac serverSide/DTTQMain.java
javac serverSide/GIRMain.java
javac serverSide/TSAMain.java