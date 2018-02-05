/* Me
 * Client.java
 */

import java.io.*;
import java.net.*;

public class Client 
{
    public static final int SERVER_PORT = 6833;
    
    public static void main(String[] commands) 
    {
	Socket clientSocket = null;  
	PrintStream os = null;
	BufferedReader is = null;
	String userInput = null;
	String serverInput = null;
	BufferedReader stdInput = null;

	//Check the number of command line parameters
	if (commands.length < 1)
	{
	    System.out.println("Usage: client <Server IP Address>");
	    System.exit(1);
	}

	// Try to open a socket on SERVER_PORT
	// Try to open input and output streams
	try 
	{
	    clientSocket = new Socket(commands[0], SERVER_PORT);
	    os = new PrintStream(clientSocket.getOutputStream());
	    is = new BufferedReader (
		new InputStreamReader(clientSocket.getInputStream()));
	    stdInput = new BufferedReader(new InputStreamReader(System.in));
	} 
	catch (UnknownHostException e) 
	{
	    System.err.println("Don't know about host: hostname");
	} 
	catch (IOException e) 
	{
	    System.err.println("Couldn't get I/O for the connection to: hostname");
	}

	// If everything has been initialized then we want to write some data
	// to the socket we have opened a connection to on port 25

	if (clientSocket != null && os != null && is != null) 
	{
	    try 
	    {
		while ((userInput = stdInput.readLine())!= null)
		{
		    os.println(userInput);
		    serverInput = is.readLine();
		    System.out.println("Echo:" + serverInput);
<<<<<<< HEAD
                    if(userInput.contains("QUIT")){
                        os.close();
                        is.close();
                        clientSocket.close(); 
                    }//END QUIT
                  if(userInput.contains("ADD")){
                      
                  }// END ADD
                    
                }
=======
                    //code goes here hahahahaha
		}

>>>>>>> parent of 1e1bc43... redid shit
		// close the input and output stream
		// close the socket

		os.close();
		is.close();
		clientSocket.close();   
	    } 
	    catch (IOException e) 
	    {
		System.err.println("IOException:  " + e);
	    }
	}
    }           
}
