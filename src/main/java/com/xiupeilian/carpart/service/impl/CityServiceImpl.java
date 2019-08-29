package com.xiupeilian.carpart.service.impl;

import com.xiupeilian.carpart.mapper.CityMapper;
import com.xiupeilian.carpart.model.City;
import com.xiupeilian.carpart.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author: Tu Xu
 * @CreateDate: 2019/8/26 10:44
 * @Version: 1.0
 **/
@Service
public class CityServiceImpl implements CityService {
    @Autowired
    private CityMapper cityMapper;



    /**
     * @Description: 指定该方法的返回值，会被spring-cache cachemanager进行缓存,需要指定使用哪一个缓存
     * @Author:      Administrator
     * @Param:       [parentId]
     * @Return       java.util.List<com.xiupeilian.carpart.model.City>
     **/
    @Override
    @Cacheable(value="redisCache")
    public List<City> findCitiesByParentId(Integer parentId) {
        return cityMapper.findCitiesByParentId( parentId);
    }

    /**
     * @Description: 需要定义规则，手动维护缓存(删除或清空)
     * @Author:      Administrator
     * @Param:       [id]
     * @Return       void
     **/
    @Override
    @CacheEvict(value="redisCache",allEntries = true)
    public void deleteCityById(int id) {
        cityMapper.deleteByPrimaryKey(id);
    }
}
