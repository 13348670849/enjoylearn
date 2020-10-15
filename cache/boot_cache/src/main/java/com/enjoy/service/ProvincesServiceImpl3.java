package com.enjoy.service;

import com.enjoy.entity.Provinces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 缓存雪崩
 */
//@Service("provincesService")
public class ProvincesServiceImpl3 extends ProvincesServiceImpl implements ProvincesService{
    private static final Logger logger = LoggerFactory.getLogger(ProvincesServiceImpl3.class);
    @Resource
    private CacheManager cm;
    private ConcurrentHashMap<String, Lock> locks = new ConcurrentHashMap<>();//线程安全的

    private static final String CACHE_NAME = "province";

    //
    public Provinces detail(String provinceid) {
        // 1.从缓存中取数据
        Cache.ValueWrapper valueWrapper = cm.getCache(CACHE_NAME).get(provinceid);
        if (valueWrapper != null) {
            logger.info("缓存中得到数据");
            return (Provinces) (valueWrapper.get());
        }

        //2.加锁排队，阻塞式锁
        doLock(provinceid);//32个省，最多只有32把锁，1000个线程
        try{//第二个线程进来了
            // 一次只有一个线程
             //双重校验，不加也没关系，无非是多刷几次库
            valueWrapper = cm.getCache(CACHE_NAME).get(provinceid);//第二个线程，能从缓存里拿到值？
            if (valueWrapper != null) {
                logger.info("缓存中得到数据");
                return (Provinces) (valueWrapper.get());//第二个线程，这里返回
            }

            Provinces provinces = super.detail(provinceid);
            // 3.从数据库查询的结果不为空，则把数据放入缓存中，方便下次查询
            if (null != provinces){
                cm.getCache(CACHE_NAME).put(provinceid, provinces);
            }
            return provinces;
        }catch(Exception e){
            return null;
        }finally{
            //4.解锁
            releaseLock(provinceid);
        }
    }

    private void releaseLock(String userCode) {
        ReentrantLock oldLock = (ReentrantLock) locks.get(userCode);
        if(oldLock !=null && oldLock.isHeldByCurrentThread()){
            oldLock.unlock();
        }
    }



    private void doLock(String lockcode) {

        //provinceid有不同的值，参数多样化
        //provinceid相同的，加一个锁，---- 不是同一个key，不能用同一个锁

        ReentrantLock newLock = new ReentrantLock();//创建一个锁

        Lock oldLock = locks.putIfAbsent(lockcode, newLock);//若已存在，则newLock直接丢弃
        if(oldLock == null){
            newLock.lock();
        }else{
            oldLock.lock();
        }
    }
}