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

    public void save() {
        Class<?> clazz = this.getClass();
        if (clazz.isAnnotationPresent(Entity.class)) {
            String className = clazz.getSimpleName();

            Entity entityAnnotation = clazz.getAnnotation(Entity.class);
            String entityTableName = entityAnnotation.tableName();

            if (entityTableName.trim().isEmpty()) {
                entityTableName = className;
            }
            String tableName = entityTableName;

            Field[] fields = clazz.getDeclaredFields();
            List<String> columnsProcessed = new ArrayList<>();
            List<Field> fieldsToInsert = new ArrayList<>();

            Field idField = null;
            String idColumn = null;
            Object idValue = null;

            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(Column.class)) {
                    idField = field;
                    Column col = field.getAnnotation(Column.class);
                    String name = col.name();
                    if (name.trim().isEmpty()) {
                        idColumn = field.getName();
                    } else {
                        idColumn = name;
                    }
                    try {
                        field.setAccessible(true);
                        idValue = field.get(this);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    continue;
                }

                if (field.isAnnotationPresent(Column.class)) {
                    try {
                        Column columnAnnotation = field.getAnnotation(Column.class);
                        String columnName = columnAnnotation.name();

                        if (columnName.trim().isEmpty()) {
                            columnName = field.getName();
                        }

                        columnsProcessed.add(columnName);
                        fieldsToInsert.add(field);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }

            if (this.verifyExistence()) {
                String updateColumns = "";
                for (int i = 0; i < columnsProcessed.size(); i++) {
                    updateColumns += columnsProcessed.get(i) + " = ?";
                    if (i < columnsProcessed.size() - 1) {
                        updateColumns += ", ";
                    }
                }

                String sql = "UPDATE " + tableName + " SET " + updateColumns + " WHERE " + idColumn + " = ?";

                try (Connection conn = DriverManager.getConnection(this.urlDB, this.userDB, this.passDB);
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    int i = 0;
                    for (; i < fieldsToInsert.size(); i++) {
                        Field field = fieldsToInsert.get(i);
                        field.setAccessible(true);
                        Object value = field.get(this);
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

            String columnsString = String.join(", ", columnsProcessed);
            String interrogationPointers = "";
            for (int i = 0; i < columnsProcessed.size(); i++) {
                interrogationPointers += "?, ";
            }
            interrogationPointers = interrogationPointers.substring(0, interrogationPointers.length() - 2);

            String sql = "INSERT INTO " + tableName + " (" + columnsString + ") VALUES (" + interrogationPointers + ")";

            try (Connection conn = DriverManager.getConnection(this.urlDB, this.userDB, this.passDB);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                for (int i = 0; i < fieldsToInsert.size(); i++) {
                    Field field = fieldsToInsert.get(i);
                    field.setAccessible(true);
                    Object value = field.get(this);
                    stmt.setObject(i + 1, value);
                }

                System.out.println(stmt.toString());
                stmt.executeUpdate();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } else {
            System.out.println("Class is not an Entity");
        }
    }


    public static <T extends FrameworkClass> List<T> loadAll(Class<T> clazz, String urlDB, String userDB, String passDB) {
        List<T> results = new ArrayList<>();

        if (!clazz.isAnnotationPresent(Entity.class)) {
            System.out.println("Class is not an Entity");
            return results;
        }

        Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        String tableName = entityAnnotation.tableName();
        if (tableName.trim().isEmpty()) {
            tableName = clazz.getSimpleName();
        }

        String sql = "SELECT * FROM " + tableName;

        try (Connection conn = DriverManager.getConnection(urlDB, userDB, passDB);
             PreparedStatement stmt = conn.prepareStatement(sql);
             var rs = stmt.executeQuery()) {

            Field[] fields = clazz.getDeclaredFields();

            while (rs.next()) {
                T instance = clazz.getDeclaredConstructor().newInstance();
                instance.setUrlDB(urlDB);
                instance.setUserDB(userDB);
                instance.setPassDB(passDB);

                for (Field field : fields) {
                    if (!field.isAnnotationPresent(Column.class)) {
                        continue;
                    }

                    Column columnAnnotation = field.getAnnotation(Column.class);
                    String columnName = columnAnnotation.name();
                    if (columnName.trim().isEmpty()) {
                        columnName = field.getName();
                    }

                    field.setAccessible(true);
                    Object value = rs.getObject(columnName);
                    field.set(instance, value);
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

        Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        String tableName = entityAnnotation.tableName().trim();
        if (tableName.isEmpty()) {
            tableName = clazz.getSimpleName();
        }

        Field[] fields = clazz.getDeclaredFields();
        Object idValue = null;
        String idColumnName = null;

        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(Column.class)) {
                try {
                    field.setAccessible(true);
                    idValue = field.get(this);

                    Column columnAnnotation = field.getAnnotation(Column.class);
                    String columnName = columnAnnotation.name();
                    if (columnName.trim().isEmpty()) {
                        idColumnName = field.getName();
                    } else {
                        idColumnName = columnName;
                    }

                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return false;
                }
            }
        }

        if (idValue == null || idColumnName == null) {
            System.out.println("ID not defined.");
            return false;
        }

        return checkExistsInDB(idValue, tableName, idColumnName);
    }


    public boolean findById() {
        Class<?> clazz = this.getClass();
        if (clazz.isAnnotationPresent(Entity.class)) {
            String className = clazz.getSimpleName();

            Entity entityAnnotation = clazz.getAnnotation(Entity.class);
            String entityTableName = entityAnnotation.tableName();

            if (entityTableName.trim().isEmpty()) {
                entityTableName = className;
            }
            String tableName = entityTableName;

            Field[] fields = clazz.getDeclaredFields();
            Object idValue = null;
            String idColumnName = null;
            Field idField = null;

            for (Field field : fields) {
                if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(Column.class)) {
                    try {
                        idField = field;
                        field.setAccessible(true);
                        idValue = field.get(this);

                        Column columnAnnotation = field.getAnnotation(Column.class);
                        String columnName = columnAnnotation.name();

                        if (columnName.trim().isEmpty()) {
                            idColumnName = field.getName();
                        } else {
                            idColumnName = columnName;
                        }

                        break;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        return false;
                    }
                }
            }

            if (idField == null || idValue == null) {
                System.out.println("ID not defined.");
                return false;
            }

            String sql = "SELECT * FROM " + tableName + " WHERE " + idColumnName + " = ?";

            try (Connection conn = DriverManager.getConnection(this.urlDB, this.userDB, this.passDB);
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setObject(1, idValue);
                var rs = stmt.executeQuery();

                if (rs.next()) {
                    for (Field field : fields) {
                        if (!field.isAnnotationPresent(Column.class)){
                            continue;
                        }

                        Column columnAnnotation = field.getAnnotation(Column.class);
                        String columnName = columnAnnotation.name();

                        if (columnName.trim().isEmpty()) {
                            columnName = field.getName();
                        }

                        field.setAccessible(true);
                        Object value = rs.getObject(columnName);
                        field.set(this, value);
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

        } else {
            System.out.println("Class is not an Entity");
            return false;
        }
    }

    public void find(String fieldName, Object value) {
        Class<?> clazz = this.getClass();
        if (!clazz.isAnnotationPresent(Entity.class)) {
            System.out.println("Class is not an Entity");
            return;
        }

        String tableName;
        Entity entityAnnotation = clazz.getAnnotation(Entity.class);
        String entityTableName = entityAnnotation.tableName();
        if (entityTableName.trim().isEmpty()) {
            tableName = clazz.getSimpleName();
        } else {
            tableName = entityTableName;
        }

        Field[] fields = clazz.getDeclaredFields();
        String columnName = null;
        Field targetField = null;

        for (Field field : fields) {
            if (field.getName().equals(fieldName) && field.isAnnotationPresent(Column.class)) {
                Column col = field.getAnnotation(Column.class);
                String annotatedName = col.name();
                if (annotatedName.trim().isEmpty()) {
                    columnName = field.getName();
                } else {
                    columnName = annotatedName;
                }
                targetField = field;
                break;
            }
        }

        if (columnName == null || targetField == null) {
            System.out.println("Field not found or not annotated with @Column: " + fieldName);
            return;
        }

        String sql = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";

        try (Connection conn = DriverManager.getConnection(this.urlDB, this.userDB, this.passDB);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, value);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                for (Field field : fields) {
                    if (!field.isAnnotationPresent(Column.class)) {
                        continue;
                    }

                    Column col = field.getAnnotation(Column.class);
                    String colName = col.name();
                    if (colName.trim().isEmpty()) {
                        colName = field.getName();
                    }

                    field.setAccessible(true);
                    Object fieldValue = rs.getObject(colName);
                    field.set(this, fieldValue);
                }
                return;
            } else {
                System.out.println("No record found with " + columnName + " = " + value);
                return;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
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
