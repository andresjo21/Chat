package chatProtocol;

public interface IService {
    public User login(User u) throws Exception;
    public void logout(User u) throws Exception; 
    public void post(Message m);
    public void register(User user) throws Exception;
    User checkContact(String text) throws Exception;
}
