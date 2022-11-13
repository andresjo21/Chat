package chatProtocol;

import java.util.List;

public interface IService {
    public User login(User u) throws Exception;
    public void logout(User u) throws Exception; 
    public void post(Message m) throws Exception;
    public void register(User user) throws Exception;
    User checkContact(String text) throws Exception;
    List<Message> getMessages(String receiver) throws Exception;
}
