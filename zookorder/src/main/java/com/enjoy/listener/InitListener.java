package com.enjoy.listener;

import com.enjoy.utils.LoadBalance;
import org.I0Itec.zkclient.ZkClient;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;

public class InitListener implements ServletContextListener {

    private  static final String BASE_SERVICES = "/services";
    private static final String  SERVICE_NAME="/products";
    private   ZkClient zkClien;
    public  void contextInitialized(ServletContextEvent sce) {
        zkClien = new ZkClient("127.0.0.1:2181");
        List<String> children =  zkClien.getChildren(BASE_SERVICES+SERVICE_NAME);
        if(children != null && children.size() > 0){
            updateServiceList(BASE_SERVICES+SERVICE_NAME,children);
        }

        zkClien.subscribeChildChanges(BASE_SERVICES+SERVICE_NAME, (parentPath,currentChilds)->{
            updateServiceList(parentPath,currentChilds);
        });
    }

    public  void contextDestroyed(ServletContextEvent sce) {
        zkClien.close();
    }


    private void updateServiceList(String parentPath,List<String> children) {
        try{
            List<String> newServerList = new ArrayList<String>();
            for(String subNode:children) {
                byte[] data = zkClien.readData(parentPath+ "/" + subNode);
                String host = new String(data, "utf-8");
                newServerList.add(host);
            }
            LoadBalance.SERVICE_LIST = newServerList;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
