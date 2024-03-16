# Project Readme

[//]: # (## General guidelines)

[//]: # (* Please spend some time to make a proper `ReadME` markdown file, explaining all the steps necessary to execute your source code.)

[//]: # (* Do not hardcode IP address or port numbers, try to collect these configurable information from config file/env variables/cmd input args.)

[//]: # (* Attach screenshots of your testing done on your local environment.)

## overview of the Project
* In this project, we have made RMI server and RMI client and leveraged RMI for RPC communication.
* Code for the client is in folder `RMI_Client` and for the server, it is in folder `RMI_Server`.

### Project structure
```bash
├── README.md
├── RMI_Client
│   ├── ClientRMI
│   ├── RMiClientMain
├── distributed_systems.iml
├── RMI_Server
│   ├── DataStore
│   ├── KeyValueDataStore
│   ├── ServerRMI

```
* Compile the code using `javac RMI_Server/*.java RMI_Client/*.java`
* For running the server : `java server/ServerRMI`
* For running the client : `java client/RMIClientMain <port-number>` (port name for this application is 1099 since this
what I mentioned for the server)
* Logs for the client are generated independently and automatically in the main folder.
* Log file names : 
  * RMI Client : `clientRMI.log`
* For the initial transactions of GET, PUT and DELETE, logs for these actions are mentioned in the log file.
  
# OUTPUT and SCREENSHOTS

* for outputs to different edge cases , please navigate to screenshots folder.