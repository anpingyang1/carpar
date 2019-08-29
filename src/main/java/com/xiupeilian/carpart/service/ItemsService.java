package com.xiupeilian.carpart.service;

import com.xiupeilian.carpart.model.Items;

import java.util.List;

/**
 * @Description:
 * @Author: Tu Xu
 * @CreateDate: 2019/8/28 9:33
 * @Version: 1.0
 **/
public interface ItemsService {
    List<Items> findItemsByQueryVo(Items items);
    List<Items> findItemsByMy(Items items);
}
