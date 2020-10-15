package com.enjoy.service;

import com.enjoy.dao.CitiesDao;
import com.enjoy.dao.ProvincesDao;
import com.enjoy.entity.Provinces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("provincesService")
public class ProvincesServiceImpl2 implements ProvincesService{
    private static final Logger logger = LoggerFactory.getLogger(ProvincesServiceImpl2.class);
    @Autowired
    private ProvincesDao provincesDao;
    @Autowired
    private CitiesDao citiesDao;

    public List<Provinces> list(){
        return provincesDao.list();
    }

    @Cacheable(value = "province")
    public Provinces detail(String provinceid) {
        System.out.println("数据库中得到数据--------"+System.currentTimeMillis());
        Provinces provinces = provincesDao.detail(provinceid);
        if (null != provinces){
            provinces.setCities(citiesDao.list(provinceid));
        }

        return provinces;
    }

    @CachePut(value = "province",key = "#entity.provinceid")
    public Provinces update(Provinces entity) {
        provincesDao.update(entity);
        return entity;
    }

    @CacheEvict(value = "province",key = "#entity.provinceid")
    public Provinces add(Provinces entity) {
        provincesDao.insert(entity);
        return entity;
    }

    @Override
    @CacheEvict("province")
    public void delete(String provinceid) {
        provincesDao.delete(provinceid);
    }


}