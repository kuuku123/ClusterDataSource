package connection.validation;

import javax.sql.PooledConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectionValidation {


    public boolean checkLogicalConnection(Connection connection , PooledConnection physicalConnection) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select 1 from dual");
            preparedStatement.execute();
            System.out.println("============== validation success ====================");
            return true;
        } catch (Exception e) {
            System.out.println("e = " + e);
            System.out.println("============== validation failed ====================");
            return false;
        }
        finally {
            try {
                connection.close();
                physicalConnection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
