package chatServer.data;

import java.sql.*;
import java.util.Properties;

public class Database {
    private static Database theInstance;

    public static Database instance() {
        if (theInstance == null) {
            theInstance = new Database();
        }
        return theInstance;
    }

    private static final String PROPERTIES_FILE_NAME = "/database.properties";

    Connection cnx;

    public Database() {
        getConnection();
    }

    public void getConnection() {
        try {
            Properties prop = new Properties();
            prop.load(getClass().getResourceAsStream(PROPERTIES_FILE_NAME));
            String driver = prop.getProperty("database_driver");
            String server = prop.getProperty("database_server");
            String port = prop.getProperty("database_port");
            String user = prop.getProperty("database_user");
            String password = prop.getProperty("database_password");
            String database = prop.getProperty("database_name");

            String URL_conexion = "jdbc:mysql://" + server + ":" + port + "/" +
                    database + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            Class.forName(driver).newInstance();
            cnx = DriverManager.getConnection(URL_conexion,user,password);
        } catch (Exception e) {
            System.err.println("FALLÓ CONEXION A BASE DE DATOS");
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }

    public PreparedStatement prepareStatement(String statement) throws Exception {
        try {
            return cnx.prepareStatement(statement);
        } catch (SQLException e) {
            throw new Exception("ERROR DE BASE DE DATOS");
        }
    }

    public int executeUpdate(PreparedStatement statement) throws Exception {
        try {
            statement.executeUpdate();
            return statement.getUpdateCount();
        } catch (SQLIntegrityConstraintViolationException ex) {
            throw new Exception("REGISTRO DUPLICADO");
        } catch (Exception ex) {
            throw new Exception("ERROR DE BASE DE DATOS");
        }

    }

    public ResultSet executeQuery(PreparedStatement statement) throws Exception {
        try {
            return statement.executeQuery();
        } catch (SQLException e) {
            throw new Exception("ERROR DE BASE DE DATOS");
        }
    }
}
