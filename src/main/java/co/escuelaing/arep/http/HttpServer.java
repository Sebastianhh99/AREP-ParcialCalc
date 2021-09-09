package main.java.co.escuelaing.arep.http;

public class HttpServer {
    private static HttpServer _instance = new HttpServer();

    private static HashMap<String,String> paths;

    private HttpServer(){
        paths=new HashMap<>();
        paths.put("/parcial", "application/json");
    }

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
        //System.out.println(type);
        return "HTTP/1.1 200 OK\r\n"
        + "Content-Type: "+"application/json"+"\r\n"
        + "\r\n"+CalcService.calc(path.replace("/", ""), number);
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
                String[] requestString = inputLine.split(" ");
                String[] res = requestString[1].split("\\?");
                method= requestString[0];
                path = res[0];
                version = requestString[2];
                param = res[1].split("=")[1];
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
