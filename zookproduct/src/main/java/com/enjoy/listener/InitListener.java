package com.enjoy.listener;

import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.net.InetAddress;

public class InitListener implements ServletContextListener {
    @Value("${server.port}")
    private int port;
    private  static final String BASE_SERVICES = "/services";
    private static final String  SERVICE_NAME="/products";
    ZkClient zkClient;
    public  void contextInitialized(ServletContextEvent sce) {
        WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext()).getAutowireCapableBeanFactory().autowireBean(this);
        try {
            String hostAddress = InetAddress.getLocalHost().getHostAddress();
            String serverName = sce.getServletContext().getContextPath();
            System.out.println("Http://"+hostAddress+":"+port+serverName+"/");
            zkClient = new ZkClient("127.0.0.1:2181");
            if(!zkClient.exists(BASE_SERVICES+SERVICE_NAME)){
                zkClient.createPersistent(BASE_SERVICES+SERVICE_NAME);
            }
            zkClient.createEphemeralSequential(BASE_SERVICES+SERVICE_NAME+"/child", ("Http://"+hostAddress+":"+port+serverName+"/").getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void contextDestroyed(ServletContextEvent sce) {
        zkClient.close();
    }
}
