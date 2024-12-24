package com.thinking.machines.network.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import com.thinking.machines.network.common.*;
import com.thinking.machines.network.common.exceptions.*;

import java.net.*;

public class NetworkClient {
    public Response send(Request request) throws NetworkException {
        /*
         * Wrap all the network / socket programming code over here
         * 1. Serialize request Object
         * 2. connect to server
         * 3.  Send header and serialize form of Request in chunks as we did earlier
         * 4.  Recieve back header and then serialized form of response and desirialize it
         * 5.  return the response object 
         */


        try {
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(request);
            oos.flush();
            byte objectBytes[];
            objectBytes = baos.toByteArray();

            // Creating header , contains size/length of byteArray;
            int requestLength = objectBytes.length;
            byte header[] = new byte[1024];
            // logic to put length into header
            int x;
            int i;
            i = 1023;
            x = requestLength;
            while (x > 0) {
                header[i] = (byte) (x % 10);
                x = x / 10;
                i--;
            } // header is created and initialized here

            // Creating connection to server
            Socket socket = new Socket(Configuration.getHost(), Configuration.getPort());
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            // sending the request header to the server
            os.write(header, 0, 1024);
            os.flush();

            // getting acknowledgement from server
            byte ack[] = new byte[1];
            int bytesReadCount;
            while (true) {
                bytesReadCount = is.read(ack);
                if (bytesReadCount == -1)
                    continue;
                break;
            } // acknowledgement recieved

            // sending request data to server
            int bytesToSend = requestLength;
            int chunkSize = 1024;
            int j = 0;
            while (j < bytesToSend) {
                if ((bytesToSend - j) < chunkSize)
                    chunkSize = bytesToSend - j;
                os.write(objectBytes, j, chunkSize);
                os.flush();
                j = j + chunkSize;
            }

            // recieving response header from server
            int bytesToRecieve = 1024;
            byte[] tmp = new byte[1024];
            int k;
            i = 0;
            j = 0;
            header = new byte[1024];
            while (j < bytesToRecieve) {
                bytesReadCount = is.read(tmp);
                if (bytesReadCount == -1)
                    continue;
                for (k = 0; k < bytesReadCount; k++) {
                    header[i] = tmp[k];
                    i++;
                }
                j = j + bytesReadCount;
            } // response header recieved from server

            // getting the length of the response data from response header
            int responseLength = 0;
            i = 1;
            j = 1023;
            while (j >= 0) {
                responseLength = responseLength + (header[j] * i);
                i = i * 10;
                j--;
            }

            // Acknowledging the response Header
            ack[0] = 1;
            os.write(ack);
            os.flush();

            // Recieving response data from server
            byte response[] = new byte[responseLength];
            bytesToRecieve = responseLength;
            i = 0;
            j = 0;
            while (j < bytesToRecieve) {
                bytesReadCount = is.read(tmp);
                if (bytesReadCount == -1)
                    continue;
                for (k = 0; k < bytesReadCount; k++) {
                    response[i] = tmp[k];
                    i++;
                }
                j += bytesReadCount;
            } // response data received
            System.out.println("Response recieved");

            // acknowledging response data
            ack[0] = 1;
            os.write(ack);
            os.flush(); // acknowledgement sent to server

            // closing the connection
            socket.close();

            // converting response data (byte array) into Student type object
            ByteArrayInputStream bais = new ByteArrayInputStream(response);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Response responseObject = (Response) ois.readObject();
            System.out.println("Response is " + responseObject);
            return responseObject;
        } catch (Exception e) {
            throw new NetworkException(e.getMessage());
        }
        }
}