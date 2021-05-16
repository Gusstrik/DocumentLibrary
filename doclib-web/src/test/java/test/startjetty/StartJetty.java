package test.startjetty;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class StartJetty {

    public static void main(String[] args) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("user"));
        Server server = init("doclib-web/src/test/webapp");
        server.start();
        server.join();

    }

    public static Server init(String webDir) throws Exception {
        Server server = new Server(12135);
        WebAppContext wcon = new WebAppContext();
        wcon.setContextPath("/doclib-app");
        wcon.setDescriptor(webDir + "/web.xml");
        wcon.setResourceBase(webDir);
        wcon.setConfigurationDiscovered(true);

        wcon.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",".*/[^/]*jstl.*\\.jar$");
        //wcon.setParentLoaderPriority(true);

        server.setHandler(wcon);
        return server;
    }
}
