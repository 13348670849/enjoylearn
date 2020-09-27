package com.zookpeer;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import sun.awt.geom.AreaOp;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * 只能出发子节点
 * 创建节点路径时，父节点不能创建临时节点
 * 每次想重新注册监听的话，必须重新注册watcher或者属性为true，watcher注册的监听事件是一次性的
 */
public class Zook implements Watcher {

    static CountDownLatch countDownLatch = new CountDownLatch(1);
    static Stat stat = new Stat();
    private static ZooKeeper zookeeper;
    public static void main(String[] args) {
        try {

            zookeeper = new ZooKeeper("127.0.0.1:2181", 5000,new Zook());
            try {
                countDownLatch.await();
                zookeeper.getData("/node9/liu", true, null);
                zookeeper.create("/node9/liu/erer5","123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
               Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (KeeperException e) {
                e.printStackTrace();
            }
            System.out.println(zookeeper.getState());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void process(WatchedEvent watchedEvent) {
        //如果当前的连接状态是连接成功的，那么通过计数器去控制
        System.out.println("进去");
        if(watchedEvent.getState()==Event.KeeperState.SyncConnected){
            if(Event.EventType.None==watchedEvent.getType()&&null==watchedEvent.getPath()){
                countDownLatch.countDown();
                System.out.println(watchedEvent.getState()+"-->"+watchedEvent.getType());
            }else if(watchedEvent.getType()== Event.EventType.NodeDataChanged){
                try {
                    System.out.println("数据变更触发路径："+watchedEvent.getPath()+"->改变后的值："+
                            new String(zookeeper.getData(watchedEvent.getPath(),true,stat)));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(watchedEvent.getType()== Event.EventType.NodeChildrenChanged){//子节点的数据变化会触发
                try {
                    System.out.println("子节点数据变更路径："+watchedEvent.getPath()+"->节点的值："+
                            zookeeper.getData(watchedEvent.getPath(),true,stat));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(watchedEvent.getType()== Event.EventType.NodeCreated){//创建子节点的时候会触发
                try {
                    System.out.println("节点创建路径："+watchedEvent.getPath()+"->节点的值："+
                            zookeeper.getData(watchedEvent.getPath(),true,stat));
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else if(watchedEvent.getType()== Event.EventType.NodeDeleted){//子节点删除会触发
                System.out.println("节点删除路径："+watchedEvent.getPath());
            }
        }

    }
}
