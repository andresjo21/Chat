package chatServer.data;

import chatProtocol.Message;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MessageDao {
    Database db;

    public MessageDao() {
        db = Database.instance();
    }

    public void create(Message m) throws Exception {
        String sql = "insert into " +
                "Messages " +
                "(receiver, sender, message) " +
                "values(?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m.getReceiver().getId());
        stm.setString(2, m.getSender().getId());
        stm.setString(3, m.getMessage());

        db.executeUpdate(stm);
    }

    /*public User read(String receiver) throws Exception {
        String sql = "select " +
                "* " +
                "from  Messages m " +
                " inner join Usuario u on m.receiver=u.nombreUsuario " +
                "where m.receiver=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, receiver);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs, "m");
        } else {
            throw new Exception("No tiene mensajes");
        }
    }*/

    /*public void update(User e) throws Exception {
        String sql = "update " +
                "Usuario " +
                "set nombreUsuario=?, pass=?, nombre=?  " +
                "where nombreUsuario=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getId());
        stm.setString(2, e.getClave());
        stm.setString(3, e.getNombre());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("USUARIO NO EXISTE");
        }
    }*/

    public List<Message> findByReferencia(String receiver) throws Exception {
        UserDao userDao = new UserDao();
        List<Message> resultado = new ArrayList<Message>();
        String sql = "select * " +
                "from " +
                "Messages m " +
                " inner join Usuario u on m.receiver=u.nombreUsuario " +
                " inner join Usuario u2 on m.receiver=u2.nombreUsuario " +
                "where m.receiver like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + receiver + "%");
        ResultSet rs = db.executeQuery(stm);
        Message m;
        while (rs.next()) {
            m = from(rs, "m");
            m.setReceiver(userDao.from(rs, "u"));
            m.setSender(userDao.from(rs, "u2"));
            resultado.add(m);
        }
        return resultado;
    }

    public void delete(String m) throws Exception {
        String sql = "delete " +
                "from Messages " +
                "where receiver=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, m);
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("NO TIENE MENSAJES");
        }
    }

    public Message from(ResultSet rs, String alias) throws Exception {
        Message m = new Message();
        m.setMessage(rs.getString(alias + ".message"));
        return m;
    }
}
