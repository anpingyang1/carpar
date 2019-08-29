package com.xiupeilian.carpart.service;

import com.xiupeilian.carpart.model.Brand;
import com.xiupeilian.carpart.model.Parts;
import com.xiupeilian.carpart.model.Prime;

import java.util.List;

/**
 * @Description:
 * @Author: Tu Xu
 * @CreateDate: 2019/8/26 11:01
 * @Version: 1.0
 **/
public interface BrandService {
    List<Brand> findBrandAll();

    List<Parts> findPartsAll();

    List<Prime> findPrimeAll();
}
