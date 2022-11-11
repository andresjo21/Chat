package chatServer.data;

import chatProtocol.User;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private List<User> users;

    public Data() {
        users = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
