package com.enjoy.service;

import com.enjoy.entity.Provinces;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 缓存穿透
 */
//@Service("provincesService")
public class ProvincesServiceImpl4 extends ProvincesServiceImpl implements ProvincesService{
    private BloomFilter<String> bf =null; //等效成一个set集合

    @PostConstruct //对象创建后，自动调用本方法
    public void init(){//在bean初始化完成后，实例化bloomFilter,并加载数据
        List<Provinces> provinces = this.list();

        //当成一个SET----- 占内存，比hashset占得小很多
        bf = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), provinces.size());// 32个
        for (Provinces p : provinces) {
            bf.put(p.getProvinceid());
        }
    }

    @Cacheable(value = "province")
    public Provinces detail(String provinceid) {
        //先判断布隆过滤器中是否存在该值，值存在才允许访问缓存和数据库
        if(!bf.mightContain(provinceid)){
            System.out.println("非法访问--------"+System.currentTimeMillis());
            return null;
        }
        System.out.println("数据库中得到数据--------"+System.currentTimeMillis());
        Provinces provinces = super.detail(provinceid);

        return provinces;
    }

    @CachePut(value = "province",key = "#entity.provinceid")
    public Provinces update(Provinces entity) {
        super.update(entity);
        return entity;
    }

    @CacheEvict(value = "province",key = "#entity.provinceid")
    public Provinces add(Provinces entity) {
        super.add(entity);
        return entity;
    }

    @Override
    @CacheEvict("province")
    public void delete(String provinceid) {
        super.delete(provinceid);
    }


}