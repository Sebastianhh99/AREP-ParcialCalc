package co.escuelaing.arep.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import co.escuelaing.arep.service.CalcService;

public class HttpServer {
    private static HttpServer _instance = new HttpServer();

    private HttpServer(){}

    private static HttpServer getInstance(){
        return _instance;
    }

    static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000; //returns default port if heroku-port isn't set
    }

    public String makeResponse(String path,String number){
        return "HTTP/1.1 200 OK\r\n"
        + "Content-Type: "+"application/json"+"\r\n"
        + "Access-Control-Allow-Origin: *"+"\r\n"
        + "\r\n"+" "+CalcService.calc(path.replace("/", ""), number);
    }

    public void processResponse(Socket clientSocket) throws IOException{
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),true);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        String inputLine;
        String method="";
        String path="";
        String param="";
        String version="";
        List<String> headers = new ArrayList<String>();
        while ((inputLine = in.readLine()) != null) {
            if(method.isEmpty()){
                try{
                    String[] requestString = inputLine.split(" ");
                    String[] res = requestString[1].split("\\?");
                    method= requestString[0];
                    path = res[0];
                    version = requestString[2];
                    param = res[1].split("=")[1];
                }catch(Exception e){
                    path="error";
                    param="error";
                }
                //System.out.println("Request: "+ method + " "+path+" "+version);
            }else{
                //System.out.println("Header: "+inputLine);
                headers.add(inputLine);
            }
            //System.out.println("Received: " + inputLine);
            if (!in.ready()) {
                break;
            }
        }

        out.println(makeResponse(path,param));

        out.close();

        in.close();
    }

    public void startServer(String[] args) throws IOException{
        int port = getPort();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: "+port);
            System.exit(1);
        }
        Socket clientSocket = null;
        boolean running = true;
        while(running){
            try {
                System.out.println("Listo para recibir en puerto: "+port);
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }

            processResponse(clientSocket);
    
            clientSocket.close();    
        }
        serverSocket.close();
    }

    public static void main(String[] args) throws IOException{
        HttpServer.getInstance().startServer(args);
    }
}
