package com.cxl.cloud.repository;

import com.cxl.cloud.entry.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<SysUser,Integer> {
}
