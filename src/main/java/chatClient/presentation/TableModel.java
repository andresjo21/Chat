package chatClient.presentation;

import chatProtocol.User;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel implements javax.swing.table.TableModel{
    List<User> rows;
    int[] cols;

    public TableModel(int[] cols, List<User> rows){
        initColNames();
        this.cols=cols;
        this.rows=rows;
    }

    public int getColumnCount() {
        return cols.length;
    }

    public String getColumnName(int col){
        return colNames[cols[col]];
    }

    public Class<?> getColumnClass(int col){
        switch (cols[col]){
            case ESTADO:
                return Boolean.class;
            default: return super.getColumnClass(col);
        }
    }

    public int getRowCount() {
        return rows.size();
    }

    public Object getValueAt(int row, int col) {
        User contacto = rows.get(row);
        switch (cols[col]){
            case ID: return contacto.getId();
            case NOMBRE: return contacto.getNombre();
            case ESTADO: return contacto.isOnline();
            default: return "";
        }
    }
    public static final int ID=0;
    public static final int NOMBRE=1;
    public static final int ESTADO=2;

    String[] colNames = new String[3];
    private void initColNames(){
        colNames[ID]="Id";
        colNames[NOMBRE]= "Contactos";
        colNames[ESTADO]= "Online";
    }

}
