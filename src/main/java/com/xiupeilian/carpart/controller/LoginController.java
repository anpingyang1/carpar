package com.xiupeilian.carpart.controller;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.xiupeilian.carpart.constant.SysConstant;
import com.xiupeilian.carpart.mapper.CityMapper;
import com.xiupeilian.carpart.model.*;
import com.xiupeilian.carpart.service.BrandService;
import com.xiupeilian.carpart.service.CityService;
import com.xiupeilian.carpart.service.UserService;
import com.xiupeilian.carpart.task.MailTask;
import com.xiupeilian.carpart.util.SHA1Util;
import com.xiupeilian.carpart.vo.LoginVo;
import com.xiupeilian.carpart.vo.RegisterVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpRequest;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.aliyuncs.http.MethodType;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 登录注册
 * @Author: Tu Xu
 * @CreateDate: 2019/8/21 13:56
 * @Version: 1.0
 **/
@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private RedisTemplate jedis;
    @Autowired
    private UserService userService;
    @Autowired
    private JavaMailSenderImpl mailSender;
    @Autowired
    private ThreadPoolTaskExecutor executor;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CityService cityService;

    /**
     * @Description: 去往登录页面
     * @Author:      Administrator
     * @Param:       []
     * @Return       java.lang.String
      **/
    @RequestMapping("/toLogin")
    public String toLogin(){
        return "login/login";
    }

    @RequestMapping("/login")
    public void login(LoginVo vo, HttpServletRequest request, HttpServletResponse response) throws Exception{
        //判断验证码是否正确
        String code=(String)request.getSession().getAttribute(SysConstant.VALIDATE_CODE);
        if(vo.getValidate().toUpperCase().equals(code.toUpperCase())){
            //验证码正确
            Subject subject= SecurityUtils.getSubject();
            UsernamePasswordToken token=new UsernamePasswordToken(vo.getLoginName(),vo.getPassword());
            try {
                subject.login(token);
            }catch (Exception e){
                //用户名密码错误
                response.getWriter().write(e.getMessage());
                return ;
            }
            //获取存入的用户信息
            SysUser user=(SysUser)SecurityUtils.getSubject().getPrincipal();
            //存spring-session
            request.getSession().setAttribute("user",user);
            response.getWriter().write("3");
        }else{
            //验证码错误
            response.getWriter().write("1");
        }
    }
    //没有权限
    @RequestMapping("/noauth")
    public String noauth(){
        return "exception/noauth";
    }

    @RequestMapping("/forgetPassword")
    public String forgetPassword(){
        return "login/forgetPassword";
    }
    @RequestMapping("/getPassword")
    public void getPassword(HttpServletResponse response,LoginVo vo) throws IOException {
       SysUser user= userService.findUserByLoginNameAndEmail(vo);
       if (null==user){
           response.getWriter().write("1");
       }else{
            //生成新密码
           String password=new Random().nextInt(899999)+100000+"";
            //修改数据库
           user.setPassword(SHA1Util.encode(password));
           userService.updateUser(user);
            //发送邮件到用户邮箱
           SimpleMailMessage message=new SimpleMailMessage();
           message.setFrom("angelsinthedevil@sina.com");
           message.setTo(user.getEmail());
           message.setSubject("修配连汽配市场密码找回功能");
           message.setText("您的新密码是："+password);
           /** mailSender.send(message);
            创建一个任务，交给线程池就可以了.
            **/
           MailTask mailTask=new MailTask(mailSender,message);
           //让线程池去执行该任务就可以了
          executor.execute(mailTask);
           response.getWriter().write("2");
       }
    }

    /**
     * @Description: 注册页面
     * @Author:      Administrator
     * @Param:
     * @Return
     **/
    @RequestMapping("/toRegister")
    public String toRegister(HttpServletRequest request){
        //初始化数据  汽车品牌、配件种类、精品种类
        List<Brand> brandList=brandService.findBrandAll();
        List<Parts> partsList=brandService.findPartsAll();
        List<Prime> primelist=brandService.findPrimeAll();
        request.setAttribute("brandList",brandList);
        request.setAttribute("partsList",partsList);
        request.setAttribute("primeList",primelist);
        return "login/register";
    }
    /**
     * @Description: 用户账号唯一性校验
     * @Author:      Administrator
     * @Param:
     * @Return
     **/
    @RequestMapping("/checkLoginName")
    public void checkLoginName(String loginName, HttpServletResponse response) throws Exception{
        SysUser user= userService.findUserByLoginName(loginName);
        if(null==user){
            response.getWriter().write("1");
        }else{
            response.getWriter().write("2");
        }
    }
    @RequestMapping("/checkPhone")
    public void checkPhone(String telnum,HttpServletResponse response) throws IOException {
        //根据手机号去数据库查询
        SysUser user=userService.findUserByPhone(telnum);
        if(null==user){
            response.getWriter().write("1");
        }else{
            response.getWriter().write("2");
        }
    }


    @RequestMapping("/checkEmail")
    public void checkEmail(String email,HttpServletResponse response) throws IOException {
        //根据手机号去数据库查询
        SysUser user=userService.findUserByEmail(email);
        if(null==user){
            response.getWriter().write("1");
        }else{
            response.getWriter().write("2");
        }
    }

    @RequestMapping("/checkCompanyname")
    public void checkCompanyname(String companyname,HttpServletResponse response) throws IOException {
        //校验企业名称是否注册过
        Company company=userService.findCompanyByName(companyname);
        if(null==company){
            response.getWriter().write("1");
        }else{
            response.getWriter().write("2");
        }
    }

    /**
     * @Description: 省市县三级联动，根据父id查询所有的子节点
     * @Author:      Administrator
     * @Param:
     * @Return
     **/
    @RequestMapping("/getCity")
    public @ResponseBody
    List<City> getCity(Integer parentId){
        parentId=parentId==null?SysConstant.CITY_CHINA_ID:parentId;
        List<City> cityList=cityService.findCitiesByParentId(parentId);
        return cityList;
    }

    @RequestMapping("/register")
    public String register(RegisterVo vo){
        //参数
        //先插入企业表，再插入用户表,需要在一个事务进行控制
        userService.addRegsiter(vo);
        return "redirect:toLogin";
    }
    @RequestMapping("/smsControllter")
    public void smsControllter(String phone){
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAIs21zxaENzEJo", "EpsvrXjGXOwSh0WQTQ8TKpAzKJCu4W");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        String code=SHA1Util.random()+"";
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "\u4fee\u914d\u8fde");
        request.putQueryParameter("TemplateCode", "SMS_172888747");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            jedis.boundValueOps(phone).set(code);
            jedis.expire(phone,5, TimeUnit.MINUTES);
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping("/checkCode")
    public void checkCode(HttpServletResponse response,String phone,String code) throws IOException {
        String telCode= jedis.boundValueOps(phone).get()+"";
        if (null==telCode){
            response.getWriter().write("1");
        }
        if (!telCode.equals(code)){
            response.getWriter().write("2");
        }
    }

    @RequestMapping("/test")
    public void test(int id){
        cityService.deleteCityById(id);
    }
}
