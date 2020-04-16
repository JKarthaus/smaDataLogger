# smaDataLogger

This Project connect an **SMA Sunny Boy 5000TL-21** Photovoltaik 
Inverter to an MQTT Broker / InfluxDB Database.

The SMA Inverter can be connected via Bluetooth or RS485.

In this Project I use the RS485 Connection to an Raspberry Pi.

![overview](https://joern-karthaus.de/blog/img/smaDataLogger/overview.png)

**smaDataLogger** is a SpringBoot App that can be completly deployed
by a [Ansible Playbook](https://www.ansible.com/).

## included Projects
this Project use the native C-Library [YASDI](https://www.sma.de/produkte/monitoring-control/yasdi.html) from SMA Company.  
Thank you for sharing the Library to the community.

Second Projekt ist [YASDI4J](https://github.com/SolarNetwork/yasdi4j) this Projekt delivers the JAVA binding 
for [YASDI](https://www.sma.de/produkte/monitoring-control/yasdi.html)

## More Information
If you are interested in the project take a look at my detailed blog post about it.  
[Goto the Blog (German)](https://joern-karthaus.de/blog/2020-04-14_smaDataLogger.html)


