package com.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

public class ZkClientOper {

    public static void main(String[] args) throws InterruptedException {
        ZkClient zkClient = new ZkClient("127.0.0.1:2181");

        //String s = zkClient.readData("/myzj2");

        zkClient.subscribeChildChanges("/myNode", new IZkChildListener(){

            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println("rrr");
                System.out.println(parentPath);
            }
        });
        zkClient.createPersistent("/myNode/we2","aa");
       //zkClient.delete("/myNode/we2");
       Thread.sleep(5000);
       // String s =new String(bytes);
      //  System.out.println(s);
    }
}
