package com.enjoy.service;

import com.enjoy.entity.Provinces;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 编码实现redis缓存
 */
@Service("provincesService")
public class ProvincesServiceImpl1 extends ProvincesServiceImpl implements ProvincesService{

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public Provinces detail(String provinceid) {
        Provinces provinces = null;

        //在redis查询
        provinces = (Provinces)redisTemplate.opsForValue().get(provinceid);
        if (null != provinces){
            //滑动过期
//            redisTemplate.expire(provinceid,20000, TimeUnit.MILLISECONDS);
            System.out.println("缓存中得到数据");
            return provinces;
        }

        provinces = super.detail(provinceid);
        if (null != provinces){
            redisTemplate.opsForValue().set(provinceid,provinces);//set缓存
            redisTemplate.expire(provinceid,20000, TimeUnit.MILLISECONDS);//设置过期
        }

        return provinces;
    }

    @Override
    public Provinces update(Provinces entity) {
        redisTemplate.delete(entity.getProvinceid());//直接删除缓存
        super.update(entity);
        return entity;
    }

    @Override
    public Provinces add(Provinces entity) {
        redisTemplate.delete(entity.getProvinceid());
        super.add(entity);
        return entity;
    }

    @Override
    public void delete(String provinceid) {
        redisTemplate.delete(provinceid);
        super.delete(provinceid);
    }


}