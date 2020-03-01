package top.conanan.shiroweb.service.impl;

import top.conanan.shiroweb.domain.SysUser;
import top.conanan.shiroweb.mapper.SysUserMapper;
import top.conanan.shiroweb.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author conanan
 * @since 2020-03-01
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

}
