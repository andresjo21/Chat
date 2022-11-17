package chatServer;

import chatProtocol.User;
import chatProtocol.Protocol;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import chatProtocol.IService;
import chatProtocol.Message;

public class Worker {
    Server srv;
    ObjectInputStream in;
    ObjectOutputStream out;
    IService service;
    User user;

    public Worker(Server srv, ObjectInputStream in, ObjectOutputStream out, User user, IService service) {
        this.srv=srv;
        this.in=in;
        this.out=out;
        this.user=user;
        this.service=service;
    }

    boolean continuar;    
    public void start(){
        try {
            System.out.println("Worker atendiendo peticiones...");
            Thread t = new Thread(new Runnable(){
                public void run(){
                    listen();
                }
            });
            continuar = true;
            t.start();
        } catch (Exception ex) {  
        }
    }
    
    public void stop(){
        continuar=false;
        System.out.println("Conexion cerrada...");
    }
    
    public void listen(){
        int method;
        while (continuar) {
            try {
                method = in.readInt();
                System.out.println("Operacion: "+method);
                switch(method){
                //case Protocol.LOGIN: done on accept
                case Protocol.LOGOUT:
                    try {

                        srv.remove(user);
                        service.logout(user);
                        srv.updateContacts(user);

                    } catch (Exception ex) {}
                    stop();
                    break;

                case Protocol.POST:
                    Message message=null;
                    try {
                        message = (Message)in.readObject();
                        message.setSender(user);
                        if(!srv.deliver(message))
                            service.post(message); // if wants to save messages, ex. recivier no logged on
                        System.out.println(user.getNombre()+": "+message.getMessage());
                    } catch (ClassNotFoundException ex) {} catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;

                case Protocol.CONTACT_RESPONSE:
                    //String userName = null;
                    try {
                        User contactUser = service.checkContact((String)in.readObject());
                        deliverContact(contactUser);
                    } catch (ClassNotFoundException ex) {} catch (Exception e) {
                        out.writeInt(Protocol.ERROR_CONTACT);
                        throw new RuntimeException(e);
                        //devolver el codigo de contact response, error no error
                    }
                    break;

                case Protocol.UPDATE_CONTACTS:
                    try {
                        User contact = (User)in.readObject();
                        srv.updateContacts(contact);
                    } catch (ClassNotFoundException ex) {} catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;

                }
                out.flush();
            } catch (IOException  ex) {
                System.out.println(ex);
                continuar = false;
            }                        
        }
    }
    
    public void deliver(Message message){
        try {
            out.writeInt(Protocol.DELIVER);
            out.writeObject(message);
            out.flush();
        } catch (IOException ex) {
        }
    }

    public void deliverContact(User contact){
        try {
            out.writeInt(Protocol.CONTACT_RESPONSE);
            out.writeInt(Protocol.ERROR_NO_ERROR);
            out.writeObject(contact);
            out.flush();
        } catch (IOException ex) {
        }
    }

    public void updateContact(User user) {
        try {
            out.writeInt(Protocol.UPDATE_CONTACTS);
            out.writeObject(service.checkContact(user.getId()));
            out.flush();
        } catch (Exception ex) {
        }
    }
}
