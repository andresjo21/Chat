package chatClient.logic;

import chatClient.presentation.Controller;
import chatProtocol.User;
import chatProtocol.Protocol;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.SwingUtilities;
import chatProtocol.IService;
import chatProtocol.Message;
import java.util.ArrayList;
import java.util.List;

public class ServiceProxy implements IService{
    private static IService theInstance;
    public static IService instance(){
        if (theInstance==null){ 
            theInstance=new ServiceProxy();
        }
        return theInstance;
    }

    ObjectInputStream in;
    ObjectOutputStream out;
    Controller controller;

    public ServiceProxy() {           
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    Socket skt;
    private void connect() throws Exception{
        skt = new Socket(Protocol.SERVER,Protocol.PORT);
        out = new ObjectOutputStream(skt.getOutputStream() );
        out.flush();
        in = new ObjectInputStream(skt.getInputStream());    
    }

    private void disconnect() throws Exception{
        skt.shutdownOutput();
        skt.close();
    }
    
    public User login(User u) throws Exception{
        connect();
        try {
            out.writeInt(Protocol.LOGIN);
            out.writeObject(u);
            out.flush();
            int response = in.readInt();
            if (response==Protocol.ERROR_NO_ERROR){
                User u1=(User) in.readObject();
                this.start();
                return u1;
            }
            else {
                disconnect();
                throw new Exception("No remote user");
            }            
        } catch (IOException | ClassNotFoundException ex) {
            return null;
        }
    }
    
    public void logout(User u) throws Exception{
        out.writeInt(Protocol.LOGOUT);
        out.writeObject(u);
        out.flush();
        this.stop();
        this.disconnect();
    }
    
    public void post(Message message){
        try {
            out.writeInt(Protocol.POST);
            out.writeObject(message);
            out.flush();
        } catch (IOException ex) {
            
        }   
    }

    public void register(User user) throws Exception {
        connect();
        out.writeInt(Protocol.REGISTER);
        out.writeObject(user);
        out.flush();
        if (in.readInt() == Protocol.ERROR_REGISTER) {
            disconnect();
            throw new Exception("User already exists");
        }
    }

    @Override
    public User checkContact(String text)throws Exception {
        out.writeInt(Protocol.CONTACT);
        out.writeObject(text);
        out.flush();
        User contact = null;
        return contact;
    }

    @Override
    public List<Message> getMessages(String receiver) throws Exception {
        return null;
    }

    @Override
    public void deleteMessages(String receiver) throws Exception {}

    // LISTENING FUNCTIONS
   boolean continuar = true;
   public void start(){
        System.out.println("Client worker atendiendo peticiones...");
        Thread t = new Thread(new Runnable(){
            public void run(){
                listen();
            }
        });
        continuar = true;
        t.start();
    }
    public void stop(){
        continuar=false;
    }
    
   public void listen(){
        int method;
        while (continuar) {
            try {
                method = in.readInt();
                System.out.println("DELIVERY");
                System.out.println("Operacion: "+method);
                switch(method){
                case Protocol.DELIVER:
                    try {
                        Message message=(Message)in.readObject();
                        deliver(message);
                    } catch (ClassNotFoundException ex) {}
                    break;
                case Protocol.CONTACT_RESPONSE:
                    try{
                        User contact=(User)in.readObject();
                        contactResponse(contact);
                    }catch (ClassNotFoundException ex){}
                    break;
                }
                out.flush();
            } catch (IOException  ex) {
                continuar = false;
            }                        
        }
    }
    
   private void deliver( final Message message ){
      SwingUtilities.invokeLater(new Runnable(){
            public void run(){
               controller.deliver(message);
            }
         }
      );
   }

    private void contactResponse( final User contact ){
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                    controller.addContactResponse(contact);
                }
            }
        );
    }
}
