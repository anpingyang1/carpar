package com.xiupeilian.carpart.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xiupeilian.carpart.model.SysUser;
import com.xiupeilian.carpart.service.UserService;
import com.xiupeilian.carpart.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Description: 员工管理模板
 * @Author: Tu Xu
 * @CreateDate: 2019/8/22 9:44
 * @Version: 1.0
 **/
@Controller
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private UserService userService;
    @RequestMapping("/staffList")
    public String staffList(LoginVo vo, Integer pageSize, Integer pageNum, HttpServletRequest request, HttpServletResponse response) throws IOException {
        pageSize=pageSize==null?10:pageSize;
        pageNum=pageNum==null?1:pageNum;
        PageHelper.startPage(pageNum,pageSize);
        SysUser user=(SysUser) request.getSession().getAttribute("user");
        vo.setCompanyId(user.getCompanyId());
        List<SysUser> list=userService.findByCompanyId(vo);
        PageInfo<SysUser> page=new PageInfo<>(list);
        request.setAttribute("vo",vo);
        request.setAttribute("page",page);
       return "index/staffList";
    }
}
