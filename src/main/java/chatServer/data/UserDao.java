package chatServer.data;

import chatProtocol.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    Database db;

    public UserDao() {
        db = Database.instance();
    }

    public void create(User e) throws Exception {
        String sql = "insert into " +
                "Usuario " +
                "(nombreUsuario, pass, nombre, estado) " +
                "values(?,?,?,?)";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, e.getId());
        stm.setString(2, e.getClave());
        stm.setString(3, e.getNombre());
        stm.setBoolean(4, e.isOnline());

        db.executeUpdate(stm);
    }

    public User read(String nombre) throws Exception {
        String sql = "select " +
                "* " +
                "from  Usuario u " +
                "where u.nombreUsuario=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, nombre);
        ResultSet rs = db.executeQuery(stm);
        if (rs.next()) {
            return from(rs, "u");
        } else {
            throw new Exception("USUARIO NO EXISTE");
        }
    }

    public void update(User e, boolean estado) throws Exception {
        String sql = "update " +
                "Usuario " +
                "set estado=?  " +
                "where nombreUsuario=?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setBoolean(1, estado);
        stm.setString(2, e.getId());
        int count = db.executeUpdate(stm);
        if (count == 0) {
            throw new Exception("USUARIO NO EXISTE");
        }
    }

    public List<User> findByReferencia(String nombre) throws Exception {
        List<User> resultado = new ArrayList<User>();
        String sql = "select * " +
                "from " +
                "Usuario u " +
                "where u.nombreUsuario like ?";
        PreparedStatement stm = db.prepareStatement(sql);
        stm.setString(1, "%" + nombre + "%");
        ResultSet rs = db.executeQuery(stm);
        while (rs.next()) {
            resultado.add(from(rs, "u"));
        }
        return resultado;
    }

    public User from(ResultSet rs, String alias) throws Exception {
        User e = new User();
        e.setId(rs.getString(alias + ".nombreUsuario"));
        e.setClave(rs.getString(alias + ".pass"));
        e.setNombre(rs.getString(alias + ".nombre"));
        e.setOnline(rs.getBoolean(alias + ".estado"));
        return e;
    }
}
