package tmjee.pet;

import org.hsqldb.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Properties;

@SpringBootApplication
public class PetApp {

    private static final Logger log = LoggerFactory.getLogger(PetApp.class);

    private static final Logger hsqldbLog = LoggerFactory.getLogger("tmjee.pet.hsqldb");

    public static void main(String[] args) throws Exception {
        Properties p = new Properties();
        p.load(PetApp.class.getResourceAsStream("/application.properties"));
        log.info("internal HSQLDB properties read");
        final Server server = new Server();
        server.setDatabaseName(0, p.getProperty("petapp.config.hsqldb.dbName"));
        server.setDatabasePath(0, p.getProperty("petapp.config.hsqldb.dbPath"));
        server.setPort(Integer.parseInt(p.getProperty("petapp.config.hsqldb.dbPort")));
        server.setLogWriter(slf4jPrintWriter());
        server.setErrWriter(slf4jPrintWriter());
        server.start();
        hsqldbLog.info("Internal HSQLDB started");                                                                                                                                                                                                                                    Runtime.getRuntime().addShutdownHook(new Thread(()->{                                                                                    server.stop();                                                                                                                         hsqldbLog.info("Internal HSQLDB stopped");                                                                                           }));                                                                                                                                                                                                                                                                                                                                                                                                                 log.info("starting OAuth2Application");

        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            server.stop();
            hsqldbLog.info("Internal HSQLDB stopped");
        }));

        log.info("starting PetApp");
        SpringApplication.run(PetApp.class, args);
        log.info("PetApp started");
    }

    private static PrintWriter slf4jPrintWriter() {
        PrintWriter printWriter = new PrintWriter(new ByteArrayOutputStream()) {
            @Override
            public void println(final String x) {
                hsqldbLog.info(x);
            }
        };
        return printWriter;
    }
}
