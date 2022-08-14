package lk.ijse.chat_room.Server;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import javafx.scene.layout.VBox;
import lk.ijse.chat_room.controller.ServerFormController;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    // used to listen to incoming connections,once it receives a connection it creates a socket object to communicate with whoever connected
    private ServerSocket serverSocket;

    // socket used to communicate with the client
    private Socket socket;

    // to receive & read msgs from client
    private BufferedReader bufferedReader;

    // to wrap and send msgs to client more efficiently
    private BufferedWriter bufferedWriter;

    public Server(ServerSocket serverSocket) {
        try{
            this.serverSocket = serverSocket;

            /**accept() is a blocking method - which means the program will halt here until a client connects to the server
             * when a client connects it returns the socket object which we(server) can use to communicate with the client*/
            this.socket = serverSocket.accept();

            /**a socket has
             * - an output stream for writing content to whoever server is connected to/clients
             * - an input stream to read content from whoever server is connected to/clients
             *
             * In Java there are 2 types of streams
             * (1) Character Streams - used to send over characters
             * (2) Byte Streams - used to send over bytes
             *
             * In Java,
             * - if it ends with "reader" or "writer" - it can be identified as a Character Stream
             *      ex :- BufferedReader, BufferedWriter
             *
             * */

            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            System.out.println("Error");
            e.printStackTrace();
            closeAll(this.socket,this.bufferedReader,this.bufferedWriter);
        }
    }

    public void sendMessageToClient(String message){
        try{
            this.bufferedWriter.write(message);
            this.bufferedWriter.newLine(); // says this is the end of the message
            this.bufferedWriter.flush();

        }catch (IOException e){
            e.printStackTrace();
            System.out.println("Error Occurred While Delivering Message to Client");
            closeAll(this.socket,this.bufferedReader,this.bufferedWriter);
        }

    }

    public void receiveMessagesFromClients(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
               while(socket.isConnected()) {
                   try {
                       String msgFromClient = bufferedReader.readLine();
                       ServerFormController.displayMsgOnChat(msgFromClient,vBox);
                       System.out.println("Client : "+msgFromClient);
                   } catch (IOException e) {
                       e.printStackTrace();
                       System.out.println("Error Occurred While Receiving Messages from Client");
                       closeAll(socket,bufferedReader,bufferedWriter);
                       break; // to avoid executing error again and again
                   }
               }
            }
        }).start();
    }

    /**We want to close down our socket & streams
     * Whenever we aren't using our socket or streams anymore we should always close them to ensure that,
     * we not wasting resources - Good Practice
     *
     * When closing down the bufferedStream, it will also close the underlying streams,
     * bcz we have wrapped the input/output streams of the socket from InputStreamReader/OutputStreamWriter &
     * those were wrapped from another BufferedReader/BufferedWriter
     * */

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
