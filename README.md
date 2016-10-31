# Network_Manager

Network Manager is android based application.

Features:

? Network : Discovers all devices connected to a Wi-Fi network
  Displays IP address, MAC address
  Checks the availability of Internet connection
  
? IP Calculator : Shows you the subnet host address range, the subnet ID, and the subnet broadcast address for given input IP address with CIDR notation. The binary representation and hexadecimal representations of IP addresses are supported.

? Device manager : Enter your own names for eachdevice

? Sharing : Share information via Twitter, Facebook, Message andE-mail

? Port Scanner: Find hundreds of open ports in a fewseconds

? Network Performance : Understand your network performances by packet loss percentage, TTL, delay time using Ping andTraceroute

? System Properties : Enlists the system properties of localhost

? System performance : Displays memory and CPU utilization, representation using graph is also supported

? Misc Services:-
    DNS lookup
    TCP Port Scanner (connect() scan)

####Network Discovery function####
Discover Machines on a LAN (connect/ping discovery)
NIC vendor database
Export results to your sdcard in XML
Fast access to Wifi Settings
Adaptive scanning rate (slow start, then adaptive to network latency)


####The "multiping" function supports multiple destinations to check IP connectivity####

Translate "*" to dot
Maximum 12 hostnames or IP addresses support.
Update delay about every 5 secs.
Trying to connect TCP port 80 or TCP echo port.
Display my local IP addresses
admob

####An Android library which implements Traceroute using Ping commands####

Traceroute wiki : http://en.wikipedia.org/wiki/Traceroute

####The System Performance function can run in the background. Then, the second option is specially interesting since, in the background, itconsumes little resources and can monitor and record the CPU and memory values that other applications are using in the foreground.####
 
-In order to get the CPU usage the app does not make use of the `Top` command from Linux but instead it parses the `/proc/stat` file and work out the calculatios with the user and system time.

#### How CPU and memory usage are obtained####

+In order to get the CPU usage the app does NOT make use of the `Top` command from Linux but instead it parses `/proc/stat` and rest of process folders from the `procfs` file system and work out the calculations with the user and system time. This is implemented on [ServiceReader.class]You can find more information about it on:
+- [procfs - Wikipedia](https://en.wikipedia.org/wiki/Procfs)
+- [Calculating CPU usage of a process in Linux - Stack Overflow](http://stackoverflow.com/questions/1420426/calculating-cpu-usage-of-a-process-in-linux)+

#### About multi-core devices####

+The app does not support showing of information regarding a specific device's core in multi-core devices. The implementation of this functionality would require considerable time. So there is no schedule for this feature.
 
 #### Retriving device processes since Android 5.1.1+
 

versions of tools used for development of software are: 
-> above project is developed in java and android
-> jdk used is 8
->eclipse version: MARS 4.5
-> android studio version used : 2.2

###TESTING HARDWARE AND SOFTWARE###
EMULATORS
ANDROID PHONES 
ANDROID TABLETS

API level used:
->  android:minSdkVersion="14"
      android:targetSdkVersion="24"

Library used: 
-> https://github.com/jaredrummler/AndroidProcesses (For Getting running processes on Android)


