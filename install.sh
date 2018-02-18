#!/bin/sh

echo "Install ->                                                                       "
echo "		      _            _____        _        _                                 "                                 
echo "           | |          |  __ \      | |      | |                                "                               
echo "  ___  ___ | | __ _ _ __| |  | | __ _| |_ __ _| |     ___   __ _  __ _  ___ _ __ " 
echo " / __|/ _ \| |/ _` | '__| |  | |/ _` | __/ _` | |    / _ \ / _` |/ _` |/ _ \ '__|"
echo " \__ \ (_) | | (_| | |  | |__| | (_| | || (_| | |___| (_) | (_| | (_| |  __/ |   "
echo " |___/\___/|_|\__,_|_|  |_____/ \__,_|\__\__,_|______\___/ \__, |\__, |\___|_|   "
echo "                                                            __/ | __/ |          "
echo "                                                           |___/ |___/           "
echo " "
echo "Check Ip Adresse in File: Ansible/hosts "
echo "Check remote_user setting in File Ansible/ansible.cfg"
echo " "
read -p "Press any key to continue... " -n1 -s

./gradlew build

cd Ansible

ansible-playbook --ask-sudo-pass smaDataLogger.yml
