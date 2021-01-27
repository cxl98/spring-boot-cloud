package com.cxl.cloud.server;

import com.cxl.cloud.common.util.MD5Utils;
import com.cxl.cloud.common.util.ReturnT;
<<<<<<< HEAD
//import com.cxl.cloud.dao.DynamicQuery;
import com.cxl.cloud.dao.UserDao;
import com.cxl.cloud.entry.User;
=======
import com.cxl.cloud.dao.DynamicQuery;
import com.cxl.cloud.entry.SysUser;
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
<<<<<<< HEAD
    private UserDao userDao;
=======
    private DynamicQuery dynamicQuery;
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2

    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "login")
    public ReturnT login(String username,String password){
<<<<<<< HEAD
        System.out.println(username+password);
//        String sql="SELECT * FROM sys_user WHERE username=?";
//        User user=dynamicQuery.nativeQuerySingleResult(User.class,sql,new Object[]{username});
        User user = userDao.selectUserByUsername(username);
        if (null!=user){
            System.out.println("xxxx");
            if (MD5Utils.getSalivaryMD5(password,user.getPassword())){
                request.getSession().setAttribute("user",user);
                System.out.println(ReturnT.success());
=======
        String sql="SELECT * FROM sys_user WHERE username=?";
        SysUser user=dynamicQuery.nativeQuerySingleResult(SysUser.class,sql,new Object[]{username});
        if (null!=user){
            if (MD5Utils.getSalivaryMD5(password,user.getPassword())){
                request.getSession().setAttribute("user",user);
>>>>>>> 9e674c949047e857516405bf511d169fd491bbf2
                return ReturnT.success();
            }else{
                return  ReturnT.fail("密码不正确");
            }
        }else{
            return ReturnT.fail("账号不存在");
        }
    }
}
