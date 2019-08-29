package com.xiupeilian.carpart.vo;

/**
 * @Description: 登录vo类
 * @Author: Tu Xu
 * @CreateDate: 2019/8/21 14:56
 * @Version: 1.0
 **/
public class LoginVo {

    private String loginName;
    private String  username;
    private String email;
    private String password;
    private String validate;
    private Integer companyId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
