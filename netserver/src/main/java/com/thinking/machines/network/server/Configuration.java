package com.thinking.machines.network.server;

import com.thinking.machines.network.common.exceptions.*;

import org.xml.sax.*;
import javax.xml.xpath.*;

import java.io.*;

class Configuration {
    private static int port = -1;
    private static boolean malformed = false;  
    private static boolean fileMissing = false;

    private static String FILE_NAME = "server.xml";

    static {
        try {
            File file = new File(FILE_NAME);
            if(file.exists()) {
                InputSource inputSource = new InputSource(FILE_NAME);
                XPath xpath = XPathFactory.newInstance().newXPath();
                String port = xpath.evaluate("//server/@port",inputSource);
                Configuration.port = Integer.parseInt(port);
            } else {
                fileMissing = true;
            }
        } catch (Exception e) {
            // TODO: handle exception
            malformed = true;  
        }
    }

    public static int getPort() throws NetworkException {
        if(fileMissing) throw new NetworkException(FILE_NAME + " is missing , read Documentation");
        if(malformed) throw new NetworkException(FILE_NAME + " is not configured according to Documentation");
        if(port < 0 || port > 49151) throw new NetworkException(FILE_NAME + " contains invalid port number, read Documentation");
        return port;    
    }
}