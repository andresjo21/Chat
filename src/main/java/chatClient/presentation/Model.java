/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatClient.presentation;

import chatProtocol.Message;
import chatProtocol.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Model extends java.util.Observable {
    User currentUser;
    List<Message> messages;
    List<User> contacts;
    List<User> auxContacts;

    public Model() {
       currentUser = null;
       messages= new ArrayList<>();
       this.setContacts(new ArrayList<>());
       this.setAuxContacts();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public void addObserver(java.util.Observer o) {
        super.addObserver(o);
        this.commit(Model.USER+Model.CHAT);
    }
    
    public void commit(int properties){
        this.setChanged();
        this.notifyObservers(properties);        
    } 

    public List<User> getContacts(){
        return contacts;
    }

    public void setContacts(List<User> contacts) {
        this.contacts = contacts;
        setAuxContacts();
    }

    public List<User> getAuxContacts() {
        return auxContacts;
    }

    public void setAuxContacts() {
        this.auxContacts = contacts;
    }

    public void searchContact(String name){
        auxContacts = contacts.stream().filter(e->e.getNombre().contains(name)).
                sorted(Comparator.comparing(e -> e.getNombre())).
                collect(Collectors.toList());
        this.commit(Model.CONTACT);
    }

    public static int USER=1;
    public static int CHAT=2;
    public static int CONTACT=3;

    public void updateContactStatus(User contact){
        User result = contacts.stream().filter(e->e.getId().equals(contact.getId())).findFirst().orElse(null);
        if (result!=null) result.setOnline(contact.isOnline());
    }
}
