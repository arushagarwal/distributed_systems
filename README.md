# Project Readme

[//]: # (## General guidelines)

[//]: # (* Please spend some time to make a proper `ReadME` markdown file, explaining all the steps necessary to execute your source code.)

[//]: # (* Do not hardcode IP address or port numbers, try to collect these configurable information from config file/env variables/cmd input args.)

[//]: # (* Attach screenshots of your testing done on your local environment.)

## overview of the Project
* In this project, we have made 2 different, servers and clients, one each for TCP and UDP.
* Code for both the clients is in folder `client` and for the server, it is in folder `server`.

### Project structure
```bash
├── README.md
├── client
│   ├── AbstractClient
│   ├── ClientTCP
│   ├── ClientUDP
│   ├── TCPClientMain
│   └── UDPClientMain
├── distributed_systems.iml
├── server
│   ├── AbstractServer
│   ├── KeyValueDataStore
│   ├── ServerTCP
│   ├── ServerUDP
│   ├── TCPServerMain
│   └── UDPServerMain
```
* Compile the code using `javac server/*.java client/*.java`
* For running the server : 
  * UDP : `java server/TCPServerMain <udp-port-num>`
  * TCP : `java server/TCPServerMain <tcp-port-num>`
* For running the client :  
  * TCP : `java client/TCPClientMain <host-name> <port-number>`
  * UDP : `java client/UDPClientMain <host-name> <port-number>`
* TCP client communicates with TCP server and UDP client communicates with UDP server
* Logs for the server and client for both TCP and UDP are generated independently and automatically in the main folder.
* Log file names : 
  * UDP Client : `clientUDP.log`
  * TCP Client : `clientTCP.log`
  * UDP Server : `serverUDP.log`
  * TCP Server : `serverTCP.log`

  
# OUTPUT and SCREENSHOTS

* for outputs to different edge cases , please navigate to screenshots folder.