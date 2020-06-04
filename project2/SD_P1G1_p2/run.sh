#!/bin/bash

echo "Running..."
# run shared regions mains
gnome-terminal -e "java serverSide/GIRMain" -t "GIRMain"
gnome-terminal -e "java serverSide/ALMain" -t "ALMain"
gnome-terminal -e "java serverSide/ATEMain" -t "ATEMain"
gnome-terminal -e "java serverSide/ATTQMain" -t "ATTQMain"
gnome-terminal -e "java serverSide/BCPMain" -t "BCPMain"
gnome-terminal -e "java serverSide/BROMain" -t "BROMain"
gnome-terminal -e "java serverSide/DTEMain" -t "DTEMain"
gnome-terminal -e "java serverSide/DTTQMain" -t "DTTQMain"
gnome-terminal -e "java serverSide/TSAMain" -t "TSAMain"
# run entities mains
# gnome-terminal -e "java clientSide/PorterMain" -t "PorterMain"
# gnome-terminal -e "java clientSide/BusDriverMain" -t "BusDriverMain"
# gnome-terminal -e "java clientSide/PassengerMain" -t "PassengerMain"
