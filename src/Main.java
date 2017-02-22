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

public class TCP_Client 
{
    public static void main(String[] args) throws Exception
    {
        int portNum = 9009;
        String ipAdress = "10.106.177.15";
        TCPClient TCP_Client = new TCPClient(portNum, ipAdress);
        TCP_Client.communicate();
    }
    
}
