#!/bin/sh



echo "Install ->                                                                       "
echo " ######   #######  ##          ###    ########  ########     ###    ########    ###    ##        #######   ######    ######   ######## ########   "                                 
echo "##    ## ##     ## ##         ## ##   ##     ## ##     ##   ## ##      ##      ## ##   ##       ##     ## ##    ##  ##    ##  ##       ##     ##  "                               
echo "##       ##     ## ##        ##   ##  ##     ## ##     ##  ##   ##     ##     ##   ##  ##       ##     ## ##        ##        ##       ##     ##  " 
echo " ######  ##     ## ##       ##     ## ########  ##     ## ##     ##    ##    ##     ## ##       ##     ## ##   #### ##   #### ######   ########   "
echo "      ## ##     ## ##       ######### ##   ##   ##     ## #########    ##    ######### ##       ##     ## ##    ##  ##    ##  ##       ##   ##    "
echo "##    ## ##     ## ##       ##     ## ##    ##  ##     ## ##     ##    ##    ##     ## ##       ##     ## ##    ##  ##    ##  ##       ##    ##   "
echo " ######   #######  ######## ##     ## ##     ## ########  ##     ##    ##    ##     ## ########  #######   ######    ######   ######## ##     ##  "
echo "1. check that you habe gradle installed "
echo "2. check Ip Adresse in File: Ansible/hosts "
echo "3. check remote_user setting in File Ansible/ansible.cfg"
echo " "
read -p "Press any key to continue... " bla

./gradlew build

cd Ansible

ansible-playbook -b --ask-become-pass smaDataLogger.yml