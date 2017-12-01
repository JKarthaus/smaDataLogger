# smaDataLogger

This Project connect an **SMA Sunny Boy 5000TL-21** Photovoltaik 
Converter to an MQTT Broker.

The SMA Inverter can be connected via Bluetooth or RS485.

In this Project I use the RS485 Connection to an Raspberry Pi.

**smaDataLogger** is a SpringBoot App that can be completly deployed
by a ![Ansible Playbook](https://www.ansible.com/).

## included Projects
this Project use the native C-Library ![YASDI](https://www.sma.de/produkte/monitoring-control/yasdi.html) from SMA Company.  
Thank you for sharing the Library to the community.

Second Projekt ist ![YASDI4J](https://github.com/SolarNetwork/yasdi4j) this Projekt delivers the JAVA binding for ![YASDI](https://www.sma.de/produkte/monitoring-control/yasdi.html)

## Technical Overview
![smaDataLoggerOverview](http://joern-karthaus.de/iot_challenge/img/iot_challenge_1.png)


## Architecture Overview
![smaDataLoggerOverview](http://joern-karthaus.de/iot_challenge/img/architecture.png)

## How to Install
The installation Procedure is automated via Ansible.  
You need a Linux Maschine as installation Host with Ansible installed.
**sudo apt install ansible**
And your passwordless Login to the target RPI should work.  

![Passwordless Login to RPI Blog Entry](http://www.joern-karthaus.de/blog/sshkey.html)

then put the IP adress of your Target RPI in the File **smaDataLogger/Ansible/hosts**  

and start the installation with **install.sh**

## More Info ?
![More Information in my Blog](http://joern-karthaus.de/iot_challenge/index.html)



