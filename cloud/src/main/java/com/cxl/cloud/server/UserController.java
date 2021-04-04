package com.cxl.cloud.server;

import com.cxl.cloud.common.util.ReturnT;
import com.cxl.cloud.dao.UserDao;
import com.cxl.cloud.entry.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "login")
    public ReturnT login(String username,String password){
        System.out.println(username+password);
        User user = userDao.selectUserByUsername(username);
        if (null!=user){
            System.out.println("xxxx");
            if (DigestUtils.md5Digest(user.getPassword().getBytes(StandardCharsets.UTF_8))==password.getBytes(StandardCharsets.UTF_8)){
                request.getSession().setAttribute("user",user);
                System.out.println(ReturnT.success());
                return ReturnT.success();
            }else{
                return  ReturnT.fail("密码不正确");
            }
        }else{
            return ReturnT.fail("账号不存在");
        }
    }
}
