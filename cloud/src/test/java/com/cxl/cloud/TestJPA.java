package com.cxl.cloud;

import com.cxl.cloud.dao.DynamicQuery;
import com.cxl.cloud.entry.SysUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class TestJPA {
    @Autowired
    private DynamicQuery dynamicQuery;

    @Test
    public void TestJpa() {
        String name = "admin";
        String sql = "SELECT * FROM sys_user WHERE username=?";
        SysUser user = dynamicQuery.nativeQuerySingleResult(SysUser.class, sql, new Object[]{name});
        System.out.println(user);
        assert user != null;
    }
}
