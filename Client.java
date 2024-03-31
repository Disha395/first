import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private static String username;

    public Client(Socket socket, String username){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(IOException e){
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }
    public void sendMessage(){
        try{
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String messageToSend =scanner.nextLine();
                bufferedWriter.write(username + ": " +messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch(IOException e){
            closeEverything(socket, bufferedWriter, bufferedReader);
        }
    }
    public void listenForMessages(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromGroupChat;

                while(socket.isConnected()){
                    try{
                        msgFromGroupChat =bufferedReader.readLine();
                        System.out.println(msgFromGroupChat);
                    }catch(IOException e){
                        closeEverything(socket, bufferedWriter, bufferedReader);
                    }
                }
            }
        }).start();
        {

        }
    }
    public  void closeEverything(Socket socket, BufferedWriter bufferedWriter, BufferedReader bufferedReader) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter your Username for Group Chat: ");
         username = sc.nextLine();
        Socket socket = new Socket("localhost", 1234);
        Client client = new Client(socket, username);
        client.listenForMessages();
        client.sendMessage();
    }
    }

