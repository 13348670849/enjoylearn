package com.enjoy.service;

import com.enjoy.dao.CitiesDao;
import com.enjoy.entity.Cities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("citiesService")
public class CitiesServiceImpl implements CitiesService{
    @Autowired
    private CitiesDao citiesDao;

    public int update(final Cities entity){
        return citiesDao.updateByEntity(entity);
    }

    public int add(final Cities entity){
        return citiesDao.insert(entity);
    }

    public List<Cities> list(String provinceid){
        return citiesDao.list(provinceid);
    }



}