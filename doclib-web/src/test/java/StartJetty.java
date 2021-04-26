import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.Reader;
import java.sql.SQLOutput;

public class StartJetty {
    private static final Logger log;

    static {
        log = LoggerFactory.getLogger(StartJetty.class);
    }

    public static void main(String[] args) throws Exception {
        Server server = init("C:\\Users\\istre\\DocumentLibrary\\test\\src\\main\\webapp");
        server.start();
        server.join();

    }

    public static Server init(String webDir) throws Exception {
        Server server = new Server(12135);
        WebAppContext wcon = new WebAppContext();


        wcon.setContextPath("/test-app");
        wcon.setDescriptor(webDir + "/web.xml");
        wcon.setResourceBase(webDir);
        wcon.setConfigurationDiscovered(true);

        wcon.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",".*/[^/]*jstl.*\\.jar$");
        //wcon.setParentLoaderPriority(true);

        server.setHandler(wcon);
        return server;
    }
}
