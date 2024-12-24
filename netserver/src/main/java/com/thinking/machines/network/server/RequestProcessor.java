package com.thinking.machines.network.server;

import com.thinking.machines.network.common.*;
import java.io.*;
import java.net.*;
    
public class RequestProcessor extends Thread {
    RequestHandlerInterface requestHandler;
    Socket socket;

    RequestProcessor(Socket socket,RequestHandlerInterface requestHandler) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        start();
    }

    public void run() {
        try {
            // Initializing o/p and i/p streams
            InputStream is = this.socket.getInputStream();
            OutputStream os = this.socket.getOutputStream();

            // recieving request header from client
            int bytesToRecieve = 1024;
            int bytesReadCount;
            byte[] tmp = new byte[1024];
            byte[] header = new byte[1024];
            int k;
            int i = 0;
            int j = 0;
            while (j < bytesToRecieve) {
                bytesReadCount = is.read(tmp);
                if (bytesReadCount == -1)
                    continue;
                for (k = 0; k < bytesReadCount; k++) {
                    header[i] = tmp[k];
                    i++;
                }
                j = j + bytesReadCount;
            } // Request header recieved from client

            // getting the length of the request data from request header
            int requestLength = 0;
            i = 1;
            j = 1023;
            while (j >= 0) {
                requestLength = requestLength + (header[j] * i);
                i = i * 10;
                j--;
            }

            // Acknowledging the request Header
            byte ack[] = new byte[1];
            ack[0] = 1;
            os.write(ack);
            os.flush();

            // Recieving request data from client
            byte requestBytes[] = new byte[requestLength];
            bytesToRecieve = requestLength;
            i = 0;
            j = 0;
            while (j < bytesToRecieve) {
                bytesReadCount = is.read(tmp);
                if (bytesReadCount == -1)
                    continue;
                for (k = 0; k < bytesReadCount; k++) {
                    requestBytes[i] = tmp[k];
                    i++;
                }
                j += bytesReadCount;
            } // request data received



            System.out.println("Request recieved");// remove after testing

            // Deserializing byte array into Student Object
            ByteArrayInputStream bais = new ByteArrayInputStream(requestBytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Request request = (Request) ois.readObject();
            Response response = requestHandler.process(request);

            // creating and serializing response
            // String responseString = "Data Recieved";
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(response);
            oos.flush();
            byte objectBytes[] = baos.toByteArray();

            // sending response header to client
            int responseLength = objectBytes.length;
            int x;
            i = 1023;
            x = responseLength;
            header = new byte[1024];
            while (x > 0) {
                header[i] = (byte) (x % 10);
                x = x / 10;
                i--;
            }
            os.write(header, 0, 1024);
            os.flush();

            // Recieving acknowledgement of response header from client
            while (true) {
                bytesReadCount = is.read(ack);
                if (bytesReadCount == -1)
                    continue;
                break;
            }

            // sending response to client
            int bytesToSend = responseLength;
            int chunkSize = 1024;
            j = 0;
            while (j < bytesToSend) {
                if (bytesToSend - j < chunkSize)
                    chunkSize = bytesToSend - j;
                os.write(objectBytes, j, chunkSize);
                os.flush();
                j = j + chunkSize;
            } // response sent

            // Recieving acknowledgement of response from client
            while (true) {
                bytesReadCount = is.read(ack);
                if (bytesReadCount == -1)
                    continue;
                break;
            }
            System.out.println("Acknowledgement recieved");
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}