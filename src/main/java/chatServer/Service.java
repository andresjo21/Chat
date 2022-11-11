package chatServer;

import chatProtocol.User;
import chatProtocol.IService;
import chatProtocol.Message;
import chatServer.data.Data;
import chatServer.data.UserDao;

public class Service implements IService{

    private Data data;
    
    public Service() {
        data =  new Data();
    }
    
    public void post(Message m){
        // if wants to save messages, ex. recivier no logged on
    }

    @Override
    public void register(User user) throws Exception {
        UserDao userDao = new UserDao();
        userDao.create(user);
    }

    @Override
    public User checkContact(String text) throws Exception {
        UserDao userDao = new UserDao();
        User contactUser = userDao.read(text);
        return contactUser;
    }

    public User login(User p) throws Exception {
        //for(User u:data.getUsers()) if(p.equals(u)) return u;
        //throw new Exception("User does not exist");

        //p.setNombre(p.getId()); return p;
        //verificar si el usuario existe y la contraseña es correcta en la base de datos
        UserDao userDao = new UserDao();
        User us = userDao.read(p.getId());
        if(us.getClave().equals(p.getClave())) return us;
        else throw new Exception("User does not exist");
    }

    public void logout(User p) throws Exception{
        //nothing to do
    }    
}
