#!/bin/bash

echo "Compiling..."
# compile shared regions mains
javac serverSide/GIRMain.java
javac serverSide/ALMain.java
javac serverSide/ATEMain.java
javac serverSide/ATTQMain.java
javac serverSide/BCPMain.java
javac serverSide/BROMain.java
javac serverSide/DTEMain.java
javac serverSide/DTTQMain.java
javac serverSide/TSAMain.java
# compile entities mains
javac clientSide/PorterMain.java
javac clientSide/BusDriverMain.java
javac clientSide/PassengerMain.java