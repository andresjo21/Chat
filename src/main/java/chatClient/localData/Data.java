package chatClient.localData;

import chatProtocol.Message;
import chatProtocol.User;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Data {
    private List<User> contactos;
    private List<Message> mensajes;

    public Data() {
        contactos = new ArrayList<>();
        mensajes = new ArrayList<>();
    }

    public List<User> getContactos() {
        return contactos;
    }

    public void setContactos(List<User> usuarios) {
        this.contactos = usuarios;
    }

    public List<Message> getMensajes() {
        return mensajes;
    }

    public void setMensajes(List<Message> mensajes) {
        this.mensajes = mensajes;
    }
}
