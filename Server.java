/*
Jonnathen Ravelo, Ayesha Saleem
CIS427 Project1
*/

import java.net.*;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.io.*;
import java.util.*;

public class Server {

    private static final int SERVER_PORT = 6833;
    private static ArrayList<Record> list;
    private static final String dataFile = "data.txt";
    private static int maxRecordId;
    private static FileReader fReader;
    private static ServerSocket server = null;//
    private static String line;
    private static BufferedReader reader;
    private static PrintStream sender;
    private static Socket client=  null;


    public static void main(String cmds[]){
        Write("heyo! We started!");
        if(Init()){
        
            Run();
        } else {
            System.exit(1);
        }
    }//end of main()

    private static boolean Init(){
        Write("Initializing components!");
        //instantiate objects for use;
        try{    
            //read the file into memory
            fReader = new FileReader(dataFile);
            list = new ArrayList<Record>();
            ReadDataFromFile();
            server = new ServerSocket(SERVER_PORT);
            return true; 
            
        } catch (IOException ioe)
        {
            System.err.println("Unable to build server-socket: \n" + ioe);
            return false;
        }
    }//end of Init()

    private static int FindMaxRecordId(){
        //if the list is not empty
        if(list != null && !list.isEmpty()){
            int max = 0;
            for(Record r : list){
                if(r._recordId > max){
                    max = r._recordId;
                }
            }
            return max;
        }
        else {
            return 1000;
        }

    }//end of FindMaxRecodId()

    private static void ReadDataFromFile(){
        Write("Attempting to retrieve records from file...");
        //get each line from the file
        if(Files.exists(dataFile, LinkOption.NOFOLLOW_LINKS)){
            Write("data file found!");
            try {
                BufferedReader fileReader = Files.newBufferedReader(dataFile);
                String ln = null;
                while((ln = fileReader.readLine()) != null){
                    //parse each line for tokenss
                    String tkns[] = ln.split("\\s+");
                    //store in list
                    Record nr = new Record(Integer.valueOf(tkns[0]));
                    nr._firstname = tkns[1];
                    nr._lastname = tkns[2];
                    nr._phone = tkns[3];
                    list.add(nr);
                }
            } catch (IOException ioe){
                Write("Error reading from data file!");
            } catch (NumberFormatException nfe){
                Write("unable to get record id from file for " + ln);
            }
        } else {
            Write("Data file not found!");
        }
        
        //find the max record ID
        maxRecordId = FindMaxRecordId();
    }//end of ReadDataFromFile()

    private static void WriteDataToFile(){
        //open the File connection
        //overwrite the file
        //close the file connection
    }//end of WriteDataToFile() 

    private static void Run(){
        
        while(true){
            try {
                Write("Waiting for a connection from client(s)...");
                client = server.accept();
                reader = new BufferedReader(
                    new InputStreamReader(client.getInputStream())
                );
                sender = new PrintStream(client.getOutputStream());
                Write("Connected to client! Waiting for commands!");
                while((line = reader.readLine()) != null){
                    //parse string
                    String cmds[] = line.split("\\s+");
                    switch(cmds[0]){
                        case "add":  
                            Add(cmds[1], cmds[2], cmds[3]);
                            break;
                        case "delete": 
                            Delete(cmds[1]);
                            break;
                        case "list":
                            break;
                        case "shutdown": 
                            Respond("Shutting down the server");
                            ShutDown(); 
                            break;
                        default: 
                            break;
                    }
                }
            } catch (IOException ioe) {
                System.err.println("IO Exception encountered reading/sending input from the client.");
            } catch (Exception e) {
                System.err.println("An unexpected error has occurred.");
            } finally {
                ShutDown();
            }
        }
        
    }//end of Run()

    private static void ShutDown() {
        //output list to file
        WriteDataToFile();
        //close connections
        try{
            sender.close();
            reader.close();
            server.close();               
        } catch (Exception e) {
            System.err.println("Ayyy you fucked up!");
        }
    }//end of ShutDown()

    private static void Add(String fname, String lname, String phone){
        maxRecordId++;
        Record r = new Record(maxRecordId);
        r._firstname = fname;
        r._lastname = lname;
        r._phone = phone;
        //sends a response to the Client
        Respond("200 OK");
    }//end of Add()

    private static void Delete(String id) {
        //find the record in the list
        //remove the record from the list
        //send a response to client
    }//end of Delete()

    private static void List() {
        //send each record back the client as a response
    }//end of List()

    private static class Record {
        public String _firstname, _lastname, _phone;
        private final int _recordId;

        public Record(int id) {
            _recordId = id;
        }
        public int getRecordId(){
            return this._recordId;
        }
        public String ToString(){
            return String.format("%d\t%s %s\t%s", getRecordId(), _firstname, _lastname, _phone);
        }
    }//end of Record class

    private static void Write(String msg){
        System.out.println(msg);
    }//end of Write()

    private static void Respond(String msg) {
        sender.println(line);
    }//end of Respond()
}