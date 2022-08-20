package lk.ijse.chat_room.Client;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import lk.ijse.chat_room.controller.ClientFormController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.util.ArrayList;

public class Client {

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;

    private VBox vBox;

    private BufferedImage bufferedImage;
    private Image fxImage;

    private VBox sendersVBox;

    public Client(Socket socket, String username, VBox vBox) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
            this.vBox = vBox;

        } catch (IOException e) {
            closeAll(this.socket, this.bufferedReader, this.bufferedWriter);
        }
    }

    // To Server
    public void sendMessage(String msgToSend, VBox vBox, String sendersName) {
        this.sendersVBox = vBox;
        new Thread(() -> {
            try {
                this.bufferedWriter.write(username);
                this.bufferedWriter.newLine(); // says this is the end of the message
                this.bufferedWriter.flush();

//                if (msgToSend.startsWith("/quit")) {
                if (msgToSend.contains("left")) {
//                    this.bufferedWriter.write(username+ " has left the meeting!");
                    this.bufferedWriter.write(msgToSend);

                } else if (msgToSend.contains(" has joined")) {
                    this.bufferedWriter.write(msgToSend);

                }
                /*else if (msgToSend.contains(".jpg") || msgToSend.contains(".png")) {
                    System.out.println("-------------------------");
                    System.out.println(msgToSend);

                    String url = msgToSend;
                    this.bufferedWriter.write(url);

                } */
                else {
//                    this.bufferedWriter.write(username);
//                    this.bufferedWriter.newLine(); // says this is the end of the message
                    this.bufferedWriter.flush();

                    this.bufferedWriter.write(username + " : " + msgToSend);
//                    this.bufferedWriter.write(" :   " + msgToSend);
                }

                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();

            } catch (IOException e) {
                closeAll(this.socket, this.bufferedReader, this.bufferedWriter);
            }
        }).start();

    }

    // listen/get messages from the server, and other clients
    public void listenForMessages(VBox vBox, String username) {
//        System.out.println("inside listenForMessage");
//        System.out.println("--------username-"+ username+"-");
        new Thread(() -> {
            String msgFromChat = null;
            String imgFromChat = null;
            while (socket.isConnected() && !username.equals("SERVER")) {
                try {
                    msgFromChat = bufferedReader.readLine();
//                    System.out.println("msgFromChat: "+msgFromChat);

                    if (msgFromChat.contains(".jpg") || msgFromChat.contains(".png")) {
//                        System.out.println("---------------photo msg-------------");
                        imgFromChat = msgFromChat;
//                        System.out.println("imgFromChat; "+imgFromChat);

                        String[] strings = imgFromChat.split(" : "); // shiny1 : /home/shinyT480/Downloads/Images/female_profile.jpg

                        if (strings.length == 2) {
                            String sendersName = strings[0].trim();
//                            System.out.println("sendersName: "+sendersName);

                            String filePath = strings[1];
                            bufferedImage = ImageIO.read(new File(filePath));
                            fxImage = SwingFXUtils.toFXImage(bufferedImage, null);

                            if (sendersName.equals("sender")) {
                                ClientFormController.displayImageOnRight(fxImage, vBox);
                            } else {
                                ClientFormController.displayImageOnLeft(sendersName, fxImage, vBox);
                            }
                        }

                    } else if (!msgFromChat.contains(".jpg") || !msgFromChat.contains(".png")) {
//                        System.out.println("---------------string msg-------------");
                        String[] strings = msgFromChat.split(":"); // shiny1 : Hii
                        String sendersName = strings[0].trim();

//                        System.out.println("strings[0]-"+sendersName+"-");

                        //  if string messages
                        if (strings.length == 2 || msgFromChat.contains(" has joined") || msgFromChat.contains("left") ) {
//                            ClientFormController.displayMsgOnLeft(msgFromChat, vBox);
//                            System.out.println("*"+username+"*===*"+sendersName+"*");

                            if (sendersName.equals("sender")) {
                                ClientFormController.displayMessageOnRight(msgFromChat.split(" : ")[1], vBox);
                            } else {
                                ClientFormController.displayMsgOnLeft(msgFromChat, vBox);
                            }
                        }
                    }

                } catch (IOException e) {
                    closeAll(socket, bufferedReader, bufferedWriter);
                    break; // to avoid executing error again and again
                }
            }
        }).start();
    }

    public void sendImage(File file, VBox vBox){
//        System.out.println("inside sendImage");
        new Thread(()->{
            URI uri = file.toURI();
//            System.out.println("uri : "+uri); // file:/home/shinyT480/Downloads/Images/female_profile.jpg
            String filePath = file.toURI().toString();
//            System.out.println("filePath: "+filePath); // file:/home/shinyT480/Downloads/Images/female_profile.jpg
            String url = filePath.split("file:")[1]; // /home/shinyT480/Downloads/Images/female_profile.jpg
//            System.out.println("url: "+url);

            try {
                this.bufferedWriter.write(username);
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();

                this.bufferedWriter.write(username + " : " + url);
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();

            } catch (IOException e) {
                closeAll(socket, bufferedReader, bufferedWriter);
            }

//            sendMessage(url, vBox);
            /*try {
                //read image
                this.bufferedImage = ImageIO.read(file);

                //write image
                boolean write = ImageIO.write(bufferedImage, "jpg", file);

//                System.out.println(3);
//                BufferedImage img = ImageIO.read(ImageIO.createImageInputStream(socket.getInputStream()));
//                System.out.println("img: "+img);
//
//                System.out.println(4);

//               ImageIO.write(bufferedImage , "png", file);
//               ImageIO.write(bufferedImage, "JPG", socket.getOutputStream());
//               ImageIO.write(bufferedImage , "jpg", new File(file.toURI().toString()));

            } catch (IOException e) {
                closeAll(socket, bufferedReader, bufferedWriter);
            }*/
        }).start();

    }

    /*public void listenForImages(VBox vBox) {
        new Thread(() -> {
            String imgFromChat = null;
            while (socket.isConnected()) {
                try {
                    imgFromChat = bufferedReader.readLine();
                    System.out.println("imgFromChat: "+imgFromChat);

//                    if (!imgFromChat.contains(".jpg") || !imgFromChat.contains(".png")) {
//                        listenForMessages(vBox);
//                        return;
//                    }

                    String[] strings = imgFromChat.split(":"); // shiny1: /home/shinyT480/Downloads/Images/female_profile.jpg

                    if (strings.length == 2) {
//                        ClientFormController.displayMsgOnLeft(imgFromChat, vBox);
                        String sendersName = strings[0];
                        System.out.println("sendersName: "+sendersName);
                        String filePath = strings[1];
//                        filePath = "file:"+filePath;

                        bufferedImage = ImageIO.read(new File(filePath));
                        fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
                        ClientFormController.displayImageOnLeft(username, fxImage, vBox);
                    }

//                    if (strings.length != 2) {
//                        System.out.println("imgFromChat: " + imgFromChat);
//                        bufferedImage = ImageIO.read(new File(imgFromChat));
//                        System.out.println("bufferedImage: " + bufferedImage);
//
//                        fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
//                        ClientFormController.displayImageOnLeft(fxImage, vBox);
//                    }

                } catch (IOException e) {
                    closeAll(socket, bufferedReader, bufferedWriter);
                    break; // to avoid executing error again and again
                }
            }
        }).start();
    }*/

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) bufferedReader.close();
            if (bufferedWriter != null) bufferedWriter.close();
            if (socket != null) socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

