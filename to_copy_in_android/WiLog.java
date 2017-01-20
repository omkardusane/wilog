/*****
 *  How to install this Logger in Android project :
 *
 *  0. Read the Instructions to Install of the repository first.
 *  1. Copy the WiLog.java file into a suitable package in your android app
 *  2. in the Application class copy following code and change parameters:
 *
 ********************************* code *********************************
    WiLog.enableWifiLogs(true);
    String urlToServer = "http://192.168.0.101:3000" ; // this must be URL to your node server which is counter part of this library running on a machine within your network
    try {
     WiLog.initialize("UniqueUserIdentifier",urlToServer);
    } catch (URISyntaxException e) {
     e.printStackTrace();
    }
 *********************************      *********************************
 *
 *  3. Start using the method WiLog.d(Tag,message) instead of Log.d()
 *   ex. WiLog.d(TAG,"Wifilogger initialized successfully ");
 *
 * **/

package omkardusane.io.locationsample.WirelessLogger;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.Calendar;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Omkar Dusane on 20-Jan-17.
 **/



public class WiLog {

    /** Public functions**/
    public static void enableWifiLogs(boolean value){
        enableLogs = value;
    };

    public static void d(String tag, String message){
        try {
            getInstance().emitLog(tag, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void changeUserIdentification(String userIdentification) {
        try {
            getInstance().setUserIdentification(userIdentification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getListenerUri(){
        try {
            return getInstance().getListenerUriI();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "URI NOT YET SET";
    }
    /** internal use **/
    private static boolean enableLogs = true ;
    private static WiLog ourInstance = null ;
    private String userIdentification ;
    private String listenerUri ;
    private Socket socket = null;

    private static WiLog getInstance() throws Exception {
        if(ourInstance == null){
            throw new Exception("WiLog instance not initialized properly");
        }
        return ourInstance;
    }

    private WiLog() {
    }

    private void emitLog(String tag,String message){
        try {
            JSONObject log = new JSONObject()
                    .accumulate("tag",tag)
                    .accumulate("user",userIdentification)
                    .accumulate("message",message)
                    .accumulate("ts", ""+Calendar.getInstance().getTimeInMillis());
            socket.emit("wilog_export",log);
        } catch (JSONException e) {
            if(enableLogs){
                Log.d("WiLogger ","Could not emit logs");
            }
            e.printStackTrace();
        }
    }

    private void init() throws URISyntaxException {
        socket = IO.socket(getListenerUriI()+"");
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
              emitLog("Initialize","New User Initialized");
            }
        }).on("event", new Emitter.Listener() {
            @Override
            public void call(Object... args) {}
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
            }
        });
        socket.connect();
    }

    public static void initialize(String userIdentification,String serverUri) throws URISyntaxException {
        ourInstance = new WiLog();
        ourInstance.setListenerUri(serverUri);
        ourInstance.setUserIdentification(userIdentification);
        if(enableLogs) {
         ourInstance.init();
        }
    }

    private void setUserIdentification(String userIdentification) {
        this.userIdentification = userIdentification;
    }

    private String getListenerUriI() {
        return listenerUri;
    }

    private void setListenerUri(String listenerUri) {
        this.listenerUri = listenerUri;
    }

}
