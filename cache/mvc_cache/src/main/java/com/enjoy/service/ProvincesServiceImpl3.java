package com.enjoy.service;

import com.enjoy.dao.CitiesDao;
import com.enjoy.dao.ProvincesDao;
import com.enjoy.entity.Provinces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.concurrent.TimeUnit;

//@Service("provincesService")
public class ProvincesServiceImpl3 implements ProvincesService{
    private static final Logger logger = LoggerFactory.getLogger(ProvincesServiceImpl3.class);
    @Autowired
    private ProvincesDao provincesDao;
    @Autowired
    private CitiesDao citiesDao;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public List<Provinces> list(){
        return provincesDao.list();
    }

    @Override
    public Provinces detail(String provinceid) {
        Provinces provinces = null;

        provinces = (Provinces)redisTemplate.opsForValue().get(provinceid);
        if (null != provinces){
            System.out.println("缓存中得到数据");
            return provinces;
        }

        System.out.println("数据库中得到数据");
        provinces = provincesDao.detail(provinceid);
        if (null != provinces){
            provinces.setCities(citiesDao.list(provinceid));
            redisTemplate.opsForValue().set(provinceid,provinces);
            redisTemplate.expire(provinceid,20000, TimeUnit.MILLISECONDS);
        }

        return provinces;
    }

    @Override
    public Provinces update(Provinces entity) {
        redisTemplate.delete(entity.getProvinceid());
        provincesDao.update(entity);
        return entity;
    }

    @Override
    public Provinces add(Provinces entity) {
        redisTemplate.delete(entity.getProvinceid());
        provincesDao.insert(entity);
        return entity;
    }

    @Override
    public void delete(String provinceid) {
        redisTemplate.delete(provinceid);
        provincesDao.delete(provinceid);
    }


}