package chatServer.data;

import chatProtocol.User;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<User> users;

    public Data() {
        users = new ArrayList<>();
        users.add(new User("001","001","Juan"));
        users.add(new User("002","002","Maria"));
        users.add(new User("003","003","Pedro"));
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
