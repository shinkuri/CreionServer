import net.LoginReceptor;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;

import java.net.SocketException;

public class Main {

    public static void main(String[] args) {

        Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.INFO);

        try {
            final LoginReceptor loginReceptor = new LoginReceptor(666, 10000);
            loginReceptor.start();
        } catch (SocketException socketException) {
            socketException.printStackTrace();
        }
    }
}
