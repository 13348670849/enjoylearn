package com.enjoy.service;

import com.enjoy.dao.CitiesDao;
import com.enjoy.dao.ProvincesDao;
import com.enjoy.entity.Provinces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;

import java.util.List;

/**
 * springcache优雅实现缓存
 */
//@Service("provincesService")
@CacheConfig(cacheNames="province") //通用配置
public class ProvincesServiceImpl2 implements ProvincesService{

    @Autowired
    private ProvincesDao provincesDao;
    @Autowired
    private CitiesDao citiesDao;

    public List<Provinces> list(){
        return provincesDao.list();
    }

    /**
     * 查询数据时，使用
     * @param provinceid
     * @return
     */
//    @Cacheable(value = "province",
//            key = "#root.targetClass.simpleName+':'+#root.methodName+':'+#provinceid")
    @Cacheable// value指定当前接口，要使用哪一个缓存器 --- 如果该缓存器不存在，则创建一个
    public Provinces detail(String provinceid) {//一个接口方法，对应一个缓存器
        Provinces provinces = null;

        System.out.println("数据库中得到数据--------"+System.currentTimeMillis());
        provinces = provincesDao.detail(provinceid);
        if (null != provinces){
            provinces.setCities(citiesDao.list(provinceid));
        }

        return provinces;
    }

    //新增一个方法---- 缓存的都是方法返回值
    @CachePut(key = "#entity.provinceid")
    public Provinces update(Provinces entity) {
        provincesDao.update(entity);
        return entity;
    }


    @Override
    @CacheEvict
    public void delete(String provinceid) {
        provincesDao.delete(provinceid);
    }



    //组合配置
    @Caching(put = {
            @CachePut(key = "#entity.provinceid"),
            @CachePut(key = "#entity.provinceid")}
    )
    public Provinces add(Provinces entity) {
        provincesDao.insert(entity);
        return entity;
    }

}