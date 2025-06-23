package framework;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public abstract class FrameworkClass {
    String urlDB;
    String userDB;
    String passDB;

    public FrameworkClass(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void save(){
        Class<?> clazz = this.getClass();
        if (clazz.isAnnotationPresent(Entity.class)) {
            String className = clazz.getSimpleName();

            Entity entityAnnotation = clazz.getAnnotation(Entity.class);
            String entityTableName = entityAnnotation.tableName();

            if(entityTableName.trim().isEmpty()){
                entityTableName = className;
            }
            String tableName = entityTableName;

            Field[] fields = clazz.getDeclaredFields();
            List<String> columnsProcessed = new ArrayList<>();
            List<Field> fieldsToInsert = new ArrayList<>();

            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class)) {
                    continue;
                }
                if(field.isAnnotationPresent(Column.class)){
                    try{
                        String fieldName = field.getName();

                        Column columnAnnotation = field.getAnnotation(Column.class);
                        String columnName = columnAnnotation.name();

                        if(columnName.trim().isEmpty()){
                            columnName = fieldName;
                        }

                        columnsProcessed.add(columnName);
                        fieldsToInsert.add(field);
                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                }
            }
            String columnsString = String.join(", ", columnsProcessed);
            String interrogationPointers = "";
            for(int i = 0; i < columnsProcessed.size(); i++){
                interrogationPointers += "?, ";
            }
            interrogationPointers = interrogationPointers.substring(0, interrogationPointers.length() - 2);
            String sql = "INSERT INTO " + tableName + " (" + columnsString + ") VALUES ("+ interrogationPointers +")";
            System.out.println(sql);

            try(Connection conn = DriverManager.getConnection(this.urlDB, this.userDB, this.passDB);
                PreparedStatement stmt = conn.prepareStatement(sql)){

                for (int i = 0; i < fieldsToInsert.size(); i++) {
                    Field field = fieldsToInsert.get(i);
                    field.setAccessible(true);
                    Object value = field.get(this);
                    stmt.setObject(i + 1, value);
                }

                System.out.println(stmt.toString());
                stmt.executeUpdate();

            }catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
        else {
            System.out.println("Class is not an Entity");
        }
    }

    public void load(){
        Class<?> clazz = this.getClass();
        System.out.println("Class: " + clazz.getName());
    }

    public void verifyExistence(){
        Class<?> clazz = this.getClass();
        System.out.println("Class: " + clazz.getName());
    }

    public void find(){
        Class<?> clazz = this.getClass();
        System.out.println("Class: " + clazz.getName());
    }

    public void setUrlDB(String urlDB) {
        this.urlDB = urlDB;
    }

    public void setUserDB(String userDB) {
        this.userDB = userDB;
    }

    public void setPassDB(String passDB) {
        this.passDB = passDB;
    }
}
