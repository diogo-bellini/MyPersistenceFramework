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

    private static String getTableName(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Entity.class)) {
            return null;
        }
        Entity entity = clazz.getAnnotation(Entity.class);
        String tableName = entity.tableName();
        if (tableName.trim().isEmpty()) {
            tableName = clazz.getSimpleName();
        }
        return tableName;
    }

    private static List<Field> getColumnFields(Class<?> clazz) {
        List<Field> result = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                result.add(field);
            }
        }
        return result;
    }

    private static Field getIdField(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(Column.class)) {
                return field;
            }
        }
        return null;
    }

    private static String getColumnName(Field field) {
        Column col = field.getAnnotation(Column.class);
        String name = col.name();
        if (name.trim().isEmpty()) {
            name = field.getName();
        }
        return name;
    }

    private static Object getValue(Field field, Object instance) {
        try {
            field.setAccessible(true);
            return field.get(instance);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static void setValue(Field field, Object instance, Object value) {
        try {
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    public void save() {
        Class<?> clazz = this.getClass();
        if (!clazz.isAnnotationPresent(Entity.class)) {
            System.out.println("Class is not an Entity");
            return;
        }

        String tableName = getTableName(clazz);
        Field idField = getIdField(clazz);

        if (idField == null) {
            System.out.println("ID field not found");
            return;
        }

        Object idValue = getValue(idField, this);
        String idColumn = getColumnName(idField);

        List<Field> allColumns = getColumnFields(clazz);
        List<Field> fieldsToInsert = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();

        for (Field field : allColumns) {
            if (!field.equals(idField)) {
                fieldsToInsert.add(field);
                columnNames.add(getColumnName(field));
            }
        }

        if (this.verifyExistence()) {
            String updateColumns = "";
            for (int i = 0; i < columnNames.size(); i++) {
                updateColumns += columnNames.get(i) + " = ?";
                if (i < columnNames.size() - 1) {
                    updateColumns += ", ";
                }
            }

            String sql = "UPDATE " + tableName + " SET " + updateColumns + " WHERE " + idColumn + " = ?";

            try (Connection conn = DriverManager.getConnection(this.urlDB, this.userDB, this.passDB);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                int i = 0;
                for (; i < fieldsToInsert.size(); i++) {
                    Object value = getValue(fieldsToInsert.get(i), this);
                    stmt.setObject(i + 1, value);
                }

                stmt.setObject(i + 1, idValue);
                System.out.println(stmt.toString());
                stmt.executeUpdate();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            return;
        }

        String columnsString = "";
        String placeholders = "";
        for (int i = 0; i < columnNames.size(); i++) {
            columnsString += columnNames.get(i);
            placeholders += "?";
            if (i < columnNames.size() - 1) {
                columnsString += ", ";
                placeholders += ", ";
            }
        }

        String sql = "INSERT INTO " + tableName + " (" + columnsString + ") VALUES (" + placeholders + ")";

        try (Connection conn = DriverManager.getConnection(this.urlDB, this.userDB, this.passDB);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (int i = 0; i < fieldsToInsert.size(); i++) {
                Object value = getValue(fieldsToInsert.get(i), this);
                stmt.setObject(i + 1, value);
            }

            System.out.println(stmt.toString());
            stmt.executeUpdate();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    public static <T extends FrameworkClass> List<T> loadAll(Class<T> clazz, String urlDB, String userDB, String passDB) {
        List<T> results = new ArrayList<>();

        if (!clazz.isAnnotationPresent(Entity.class)) {
            System.out.println("Class is not an Entity");
            return results;
        }

        String tableName = getTableName(clazz);
        String sql = "SELECT * FROM " + tableName;

        try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB);
             PreparedStatement stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {

            List<Field> columns = getColumnFields(clazz);

            while (rs.next()) {
                T instance = clazz.getDeclaredConstructor().newInstance();
                instance.setUrlDB(urlDB);
                instance.setUserDB(userDB);
                instance.setPassDB(passDB);

                for (Field field : columns) {
                    String colName = getColumnName(field);
                    Object value = rs.getObject(colName);
                    setValue(field, instance, value);
                }

                results.add(instance);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return results;
    }

    public boolean verifyExistence() {
        Class<?> clazz = this.getClass();

        if (!clazz.isAnnotationPresent(Entity.class)) {
            System.out.println("Class is not an Entity");
            return false;
        }

        String tableName = getTableName(clazz);
        Field idField = getIdField(clazz);

        if (idField == null) {
            System.out.println("ID field not found");
            return false;
        }

        Object idValue = getValue(idField, this);
        if (idValue == null) {
            System.out.println("ID not defined.");
            return false;
        }

        String idColumnName = getColumnName(idField);

        return checkExistsInDB(idValue, tableName, idColumnName);
    }

    private boolean checkExistsInDB(Object idValue, String tableName, String idColumnName) {
        String sql = "SELECT 1 FROM " + tableName + " WHERE " + idColumnName + " = ? LIMIT 1";

        try (Connection conn = DriverManager.getConnection(this.urlDB, this.userDB, this.passDB);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, idValue);
            var rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }




    public boolean findById() {
        Class<?> clazz = this.getClass();
        if (!clazz.isAnnotationPresent(Entity.class)) {
            System.out.println("Class is not an Entity");
            return false;
        }

        String tableName = getTableName(clazz);
        Field idField = getIdField(clazz);

        if (idField == null) {
            System.out.println("ID field not found");
            return false;
        }

        Object idValue = getValue(idField, this);
        if (idValue == null) {
            System.out.println("ID value is null");
            return false;
        }

        String idColumn = getColumnName(idField);
        String sql = "SELECT * FROM " + tableName + " WHERE " + idColumn + " = ?";

        try (Connection conn = DriverManager.getConnection(this.urlDB, this.userDB, this.passDB);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, idValue);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                List<Field> columns = getColumnFields(clazz);
                for (Field field : columns) {
                    String colName = getColumnName(field);
                    Object value = rs.getObject(colName);
                    setValue(field, this, value);
                }
                return true;
            } else {
                System.out.println("Instance not found.");
                return false;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    public void find(String fieldName, Object value) {
        Class<?> clazz = this.getClass();
        if (!clazz.isAnnotationPresent(Entity.class)) {
            System.out.println("Class is not an Entity");
            return;
        }

        List<Field> columns = getColumnFields(clazz);
        Field targetField = null;
        String columnName = "";

        for (Field field : columns) {
            if (field.getName().equals(fieldName)) {
                columnName = getColumnName(field);
                targetField = field;
                break;
            }
        }

        if (targetField == null) {
            System.out.println("Field not found or not annotated with @Column: " + fieldName);
            return;
        }

        String tableName = getTableName(clazz);
        String sql = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";

        try (Connection conn = DriverManager.getConnection(this.urlDB, this.userDB, this.passDB);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, value);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                for (Field field : columns) {
                    String colName = getColumnName(field);
                    Object fieldValue = rs.getObject(colName);
                    setValue(field, this, fieldValue);
                }
            } else {
                System.out.println("No record found with " + columnName + " = " + value);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
