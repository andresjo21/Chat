package chatClient.presentation;

import chatClient.logic.ServiceProxy;
import chatProtocol.Message;
import chatProtocol.User;

import java.util.ArrayList;
import java.util.List;

public class Controller {
    View view;
    Model model;
    
    ServiceProxy localService;
    
    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        localService = (ServiceProxy)ServiceProxy.instance();
        localService.setController(this);
        view.setController(this);
        view.setModel(model);
    }

    public void login(User u) throws Exception{
        User logged=ServiceProxy.instance().login(u);
        model.setCurrentUser(logged);
        model.commit(Model.USER);
        model.commit(Model.CHAT);
    }

    public void post(String text, User receiver) throws Exception{
        Message message = new Message();
        message.setMessage(text);
        message.setSender(model.getCurrentUser());
        message.setReceiver(receiver);
        ServiceProxy.instance().post(message);
        model.getMessages().add(message);
        model.commit(Model.CHAT);
    }

    public void logout(){
        try {
            ServiceProxy.instance().logout(model.getCurrentUser());
        } catch (Exception ex) {
        }
        model.setMessages(new ArrayList<>());
        model.setCurrentUser(null);
        model.commit(Model.USER+Model.CHAT);
    }
        
    public void deliver(Message message){
        model.messages.add(message);
        model.commit(Model.CHAT);       
    }

    public void register(User user) {
        try {
            ServiceProxy.instance().register(user);
        } catch (Exception e) {}
    }

    public void addContact(String text) {
        try {
            ServiceProxy.instance().checkContact(text);
        } catch (Exception e) {}
    }

    public void addContactResponse(User contact) {
        model.contacts.add(contact);
        model.setAuxContacts();
        model.commit(Model.CONTACT);
    }

    // Metodos usados para guardar en XML
    public List<Message> getMessages() {
       return model.getMessages();
    }
    public List<User> getContacts() {
        return model.getContacts();
    }
    public void setContacts(List<User> contacts) {
        model.setContacts(contacts);
    }
    public void setMessages(List<Message> messages) {
        model.setMessages(messages);
    }
    // fin de metodos usados para XML

    public void updateContacts(User contact){
            model.updateContactStatus(contact);
            model.commit(3);
            model.commit(Model.USER);
            model.commit(Model.CONTACT);
            model.commit(Model.CHAT);
        }
}
