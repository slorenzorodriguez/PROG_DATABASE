
package sql;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Hector Pose Carames
 */
public class BD {
   public static void createNewDatabase(String fileName) {
 
        String url = "jdbc:sqlite:/home/menuven/Documentos/sqlite/" + fileName;
 
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Driver " + meta.getDriverName());
                System.out.println("Base de datos creada.");
            }
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
 

    public static void createNewTable() {
        // url = ruta de la base de datos
        String url = "jdbc:sqlite:C:\\Users\\Kinkalla\\Documents\\sqlite\\tests.db";
        
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS empresa (\n"
                + "	cif PRIMARY KEY,\n"
                + "	nome text \n"              
                + " );";
        
        
        String sql2 = "CREATE TABLE IF NOT EXISTS cliente (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	nome text NOT NULL,\n"
                + "	apellido text NOT NULL, \n"
                + "     ciudad text NOT NULL, \n"
                + "     cif text, \n"
                + "     FOREIGN KEY(cif) REFERENCES empresa(cif) \n"
                + " );";
        
        try (Connection conn = DriverManager.getConnection(url);
                Statement stmt = conn.createStatement()) {
            // creamos la tabla cargando nuestra sentencia en la variable sql
            stmt.execute(sql);
            stmt.execute(sql2);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
   
    Connection connect() {
        // url = ruta de nuestra base de datos
        String url = "jdbc:sqlite:C:\\Users\\Kinkalla\\Documents\\sqlite\\tests.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
 
    
    
    public void selectAll(){
        String sql = "SELECT id, nome, apellido, ciudad, cif FROM cliente";
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            // bucle para imprimir todos los datos de nuestra tabla
            while (rs.next()) {
                System.out.println(rs.getInt("id") +  "\t" + 
                                   rs.getString("nome") + "\t" +
                                   rs.getString("apellido") + "\t"+
                                   rs.getString("ciudad") + "\t"+
                                   rs.getString("cif"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void insert(String nome, String apellido, String ciudad, String cif) {
       
        // insert para añadir clientes a nuestra base de datos
        // el id se genera autómaticamente
        String sql = "INSERT INTO cliente(nome,apellido,ciudad,cif) VALUES(?,?,?,?)";
 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, nome);
            pstmt.setString(2, apellido);
            pstmt.setString(3, ciudad);
            pstmt.setString(4, cif);
            pstmt.executeUpdate();
            
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        
    }
    public void update(int id, String nome, String apellido, String ciudad, String cif) {
        String sql = "UPDATE cliente SET nome = ? , "
                + "apellido = ? "
                + "ciudad = ? "
                + "cif = ? "
                + "WHERE id = ?";
 
        try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            // set the corresponding param
            pstmt.setString(1, nome);
            pstmt.setString(2, apellido);
            pstmt.setString(3, ciudad);
            pstmt.setString(4, cif);
            pstmt.setInt(5, id);
            // update 
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    
    public int getId(){
        //como el id se genera automáticamente tenemos que recuperarlo 
        //para introducirlo en la tabla
        String sql = "SELECT max(id) from cliente";
        int rowID = 0;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            System.out.println(rs.getInt("max(id)"));
            rowID = rs.getInt("max(id)");
            
        } catch (SQLException ex) {
           Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
       }
     
       return rowID;
}
    
    public ArrayList<Object[]> tablas(){
       ArrayList<Object[]> tablas  = new ArrayList<>();
       String sql = "SELECT id, nome, apellido, ciudad, cif FROM cliente";
        
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
        while (rs.next()) {
                Object[] datos = new Object[5];
                datos[0] = Integer.toString(rs.getInt("id"));
                datos[1] = rs.getString("nome");
                datos[2] = rs.getString("apellido");
                datos[3] = rs.getString("ciudad");
                datos[4] = rs.getString("cif");
                tablas.add(datos);
        }        
   
    }  catch (SQLException ex) {
           Logger.getLogger(BD.class.getName()).log(Level.SEVERE, null, ex);
       }
     
        return tablas;
        }
    
    
    
}