/**
 * @author Rob Logan - c3165020
 */
import java.io.*;
import java.net.*;

public class CodeClient {
    private static final String DEFAULTHOST = "localhost";
    private static final int DEFAULTPORT = 12345;
    /**
     * A client to communicate with a remote server.
     * @param args takes two valid arguments. The first being the server's host name string 
     *              the second being an int representing the port number of the server. 
     */
    public static void main(String[] args) throws IOException{
        String hostName;
        int portNumber;

        if (args.length!=2){
            hostName = DEFAULTHOST;
            portNumber = DEFAULTPORT;
        }else{
            hostName = args[0];
            portNumber = Integer.parseInt(args[1]);
        }
        System.out.println("Connecting to "+hostName+":"+portNumber);
        try (
            Socket clientSocket = new Socket(hostName, portNumber);//create the socket
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);//create the writer to send messages to the server
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));//create the reader to receive messages
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))//create the reader to take input from the user
        ) {
            String userInput = "ASCII";
            out.println(userInput);
            System.out.println("CLIENT: "+userInput);
            String response = in.readLine();
            if (!response.equals("ASCII: OK")){
                System.out.println("Invalid response from server. Closing connection.");
                clientSocket.close();
                System.exit(1);
            }
            System.out.println("SERVER: "+response);
            System.out.print("Enter command to send to server [AC, CA, BYE, END or something to convert]: ");
            userInput = stdIn.readLine();
            while (userInput!= null) {
                out.println(userInput);
                response = in.readLine();
                if(response.equals("END: OK")||response.equals("BYE: OK")){
                    System.out.println("CLIENT: "+userInput);
                    System.out.println("SERVER: "+response);
                    break;
                }else{
                    System.out.println("CLIENT: "+userInput);
                    System.out.println("SERVER: "+response);
                    System.out.println("SERVER: "+in.readLine());
                    System.out.print("Enter command to send to server [AC, CA, BYE, END or something to convert]: ");
                    userInput = stdIn.readLine();
                }
            }
            clientSocket.close();
            System.exit(1);
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
    }
    
}
