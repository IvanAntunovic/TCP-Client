/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author a661517
 */

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import org.omg.CORBA.portable.UnknownException;
import static sun.java2d.cmm.ColorTransform.In;
import static sun.java2d.cmm.ColorTransform.Out;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Scanner;

public class TCPClient 
{
    Socket clientSocket;
    PrintWriter outToServer;
    BufferedReader inFromServer;
    
    private int portNumber;
    private String IPAddress;
    private String messageToBeSent;
    private String messageLength;
    private String ackByte;
    
    public TCPClient(int portNumber, String IPAddress)
    {
        Socket clientSocket = null;
        PrintWriter outToServer = null;
        BufferedReader inFromServer = null;
        
        this.portNumber = portNumber;
        this.IPAddress = IPAddress;
    }
    
    public void communicate() throws IOException
    {
        //SocketAddress serverAddress = new InetSocketAddress(IPAddress, portNumber);
        
        System.out.println("Attempting to connect to host ");
        
        this.connect();

        InputStreamReader consoleInReader = new InputStreamReader(System.in);
        BufferedReader inFromUser = new BufferedReader(consoleInReader);
        
        String userInput;
        String readPackage;
        
        while ( ( userInput = inFromUser.readLine() ) != null )
        {
            outToServer.print(this.getMessageToBeSent(userInput));
        
            //Write samo šalje bytove
            //outToServer.write(this.getMessageToBeSent(userInput));
            //flush šalje bytove
            
            outToServer.flush();
            
            readPackage = this.readPackage();
            
            if ( readPackage.equals("001") )
            {
                System.out.println("Delivery is sucessful!");
            }    
            
            System.out.println("Input: ");
        }
        
        outToServer.close();
        inFromServer.close();
        clientSocket.close();
    }
        
    public void connect()
    {
        try{
            clientSocket = new Socket(IPAddress, portNumber);
            outToServer = new PrintWriter(clientSocket.getOutputStream(), true);
            
            InputStreamReader inputStreamReader = new InputStreamReader(clientSocket.getInputStream());
            
            inFromServer = new BufferedReader(inputStreamReader);
            
        }catch(UnknownException e)
        {
            System.err.println("Unknown host: " + IPAddress);
            System.exit(1);
            
        }catch(IOException e)
        {
            System.err.println("Could not get connection from " + IPAddress);
            System.exit(1);
        }
        System.out.println("Connection established from " + IPAddress + "!");
        System.out.println("Enter the data to be sent to server: ");
    } 
    
    
    private String readPackage() throws IOException
    {
        StringBuilder serverInput = new StringBuilder();
        int dataLength = 0;
        boolean isHeaderParsed = false;
        
        while (isHeaderParsed == true && serverInput.length() == dataLength + 3)
        {
            serverInput.append( (char)inFromServer.read() );
            
            if ( !isHeaderParsed && serverInput.length() >= 3 )
            {
                String tempHeaderLength  = null;
                
                tempHeaderLength = serverInput.toString();
                tempHeaderLength = tempHeaderLength.substring(0, 2);
                
                dataLength = Integer.parseInt(tempHeaderLength);
                isHeaderParsed = true;
            }          
        }
        return ( serverInput.toString() );
    }
    
    public String getMessageLenght(String message)
    {
        return String.format("%02d", message.length()); 
    }
        
    
    public String addAckByte()
    {
        return ("0");
    }
    
    public String getMessageToBeSent(String message)
    {   
        StringBuilder messageToBeSent = new StringBuilder();
        
        messageToBeSent.append(this.getMessageLenght(message));
        messageToBeSent.append(this.addAckByte());
        messageToBeSent.append(message);
       
        return messageToBeSent.toString();
    }
}

    