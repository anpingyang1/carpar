package com.xiupeilian.carpart.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiupeilian.carpart.model.Brand;
import com.xiupeilian.carpart.model.Items;
import com.xiupeilian.carpart.model.Parts;
import com.xiupeilian.carpart.model.SysUser;
import com.xiupeilian.carpart.service.BrandService;
import com.xiupeilian.carpart.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 公共货架
 * @Author: Tu Xu
 * @CreateDate: 2019/8/28 9:26
 * @Version: 1.0
 **/
@Controller
@RequestMapping("/public")
public class PublicItemsController {
    @Autowired
    private ItemsService itemsService;

    @Autowired
    private BrandService brandService;

    @RequestMapping("/publicItems")
    public String publicItems(Items items, Integer pageSize, Integer pageNo, HttpServletRequest request,String brandName){
        //第一次需要初始化分页条件
        pageSize=pageSize==null?8:pageSize;
        pageNo=pageNo==null?1:pageNo;
        //开始分页查询
        PageHelper.startPage(pageNo,pageSize);
        //查询满足条件的全部数据即可
        List<Items> itemsList=itemsService.findItemsByQueryVo(items);
        //封装，底层使用动态代理
        PageInfo<Items> page=new PageInfo<>(itemsList);
        //分页数据存request
        request.setAttribute("page",page);
        //搜索条件回填
        request.setAttribute("items",items);
        //页面当中有一些数据需要初始化，品牌、配件类别
        List<Brand> brandList=brandService.findBrandAll();
        List<Parts> partsList=brandService.findPartsAll();
        //品牌列表，配件类别
        request.setAttribute("brandList",brandList);
        request.setAttribute("partsList",partsList);
        request.setAttribute("brandName",brandName);
        return "public/publicItems";
    }

    @RequestMapping("/myItems")
    public String myItems(Items items, Integer pageSize, Integer pageNo, HttpServletRequest request,String brandName){
        //第一次需要初始化分页条件
        pageSize=pageSize==null?8:pageSize;
        pageNo=pageNo==null?1:pageNo;
        //开始分页查询
        PageHelper.startPage(pageNo,pageSize);

        SysUser user=(SysUser) request.getSession().getAttribute("user");
        items.setUserId(user.getId());
        //查询满足条件的全部数据即可
        List<Items> itemsList=itemsService.findItemsByMy(items);
        //封装，底层使用动态代理
        PageInfo<Items> page=new PageInfo<>(itemsList);
        //分页数据存request
        request.setAttribute("page",page);
        //搜索条件回填
        request.setAttribute("items",items);
        //页面当中有一些数据需要初始化，品牌、配件类别
        List<Brand> brandList=brandService.findBrandAll();
        List<Parts> partsList=brandService.findPartsAll();
        //品牌列表，配件类别
        request.setAttribute("brandList",brandList);
        request.setAttribute("partsList",partsList);
        request.setAttribute("brandName",brandName);
        return "public/publicItems";
    }
}
