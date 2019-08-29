package com.xiupeilian.carpart.service;

import com.xiupeilian.carpart.model.City;

import java.util.List;

/**
 * @Description: 省市县
 * @Author: Tu Xu
 * @CreateDate: 2019/8/26 10:44
 * @Version: 1.0
 **/
public interface CityService {
    List<City> findCitiesByParentId(Integer parentId);
    public void deleteCityById(int id);
}
