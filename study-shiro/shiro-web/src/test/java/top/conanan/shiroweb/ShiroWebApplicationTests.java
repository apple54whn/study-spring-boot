package top.conanan.shiroweb;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.conanan.shiroweb.domain.SysUser;
import top.conanan.shiroweb.mapper.SysUserMapper;

@SpringBootTest
class ShiroWebApplicationTests {

    @Autowired
    private SysUserMapper sysUserMapper;


    @Test
    void contextLoads() {
        SysUser sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, "root"));
        System.out.println(sysUser);
    }

}
