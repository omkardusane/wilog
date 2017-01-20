# WiLog.d
A simple library to monitor realtime Android debug (Log.d) logs wirelessly on a remote machine.


# How to RUN and USE

Requirements : nodejs and npm , same router connected to development machine and android devices

##STEPS: 

## A. Set Up Server first

* 1. `git clone https://github.com/omkardusane/wilog.git`
* 2. `npm install`
* 3. `npm start`

* watch the logs in this shell and also in http:localhost:3000/show

## B. How to install this Logger in Android project :
 
 *  1. Copy the `WiLog.java` file from this repo into a suitable package in your android app
 *  2. in the Application class copy following code and change parameters:
 
 ********************************* code *********************************
    // in GRADLE:
    `compile ('io.socket:socket.io-client:0.8.3') {
        exclude group: 'org.json', module: 'json'
    }`

     // Application Class :

    `WiLog.enableWifiLogs(true);
    String urlToServer = "http://192.168.0.101:3000" ; // this must be URL to your node server which is counter part of this library running on a machine within your network
    try {
     WiLog.initialize("UniqueUserIdentifier",urlToServer);
    } catch (URISyntaxException e) {
     e.printStackTrace();
    }`
 *********************************      *********************************
 
 *  3. Start using the method WiLog.d(Tag,message) instead of Log.d()
    ex. `WiLog.d(TAG,"Wifilogger initialized successfully ");`
 

Thanks
