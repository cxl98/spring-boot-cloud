package com.cxl.cloud;

//import com.cxl.cloud.dao.DynamicQuery;
import com.cxl.cloud.common.util.MD5Utils;
import com.cxl.cloud.dao.UserDao;
import com.cxl.cloud.entry.User;
import org.apache.commons.codec.cli.Digest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TestMsql {
    @Autowired
    private UserDao userDao;

    @Test
    public void TestUser() {
        String admin = MD5Utils.getSaltMD5("admin");
//        String name = "admin";
//        String sql = "SELECT * FROM sys_user WHERE username=?";
//        User user = dynamicQuery.nativeQuerySingleResult(User.class, sql, new Object[]{name});
        User user=new User();
        user.setUsername("admin");
        user.setPassword(DigestUtils.md5DigestAsHex("admin".getBytes(StandardCharsets.UTF_8)));
        user.setRoleCode("1");
        user.setGmtCreate(new Timestamp(System.currentTimeMillis()));
//        User user = userDao.selectUserByUsername("admin");
        int insert = userDao.insert(user);
        System.out.println(insert);
        assert insert != 0;
    }
}
