package top.conanan.shiroweb.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.realm.text.PropertiesRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.hibernate.dialect.Dialect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.conanan.shiroweb.domain.SysUser;
import top.conanan.shiroweb.mapper.SysUserMapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@Configuration
@Slf4j
@AllArgsConstructor
public class ShiroConfig {

    private SysUserMapper sysUserMapper;

    @Bean
    public Realm userRealm() {
        return new AuthorizingRealm() {
            /**
             * 认证的步骤分为如下三步
             */
            @Override
            protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
                log.debug("=============================认证开始");
                System.out.println("=============================认证开始");

                UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

                SysUser sysUser = sysUserMapper.selectOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, token.getUsername()));
                if (sysUser == null) {
                    return null; //自动抛出 UnknownAccountException
                }

                return new SimpleAuthenticationInfo(sysUser, sysUser.getPassword(), "");
            }

            /**
             * 授权
             */
            @Override
            protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
                log.debug("=============================授权开始");
                System.out.println("=============================授权开始");
                SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
                // 这里需查询数据库
                Subject currentUser = SecurityUtils.getSubject();
                SysUser sysUser = (SysUser) currentUser.getPrincipal();// 根据认证时构造的 SimpleAuthenticationInfo 参数来获取
                simpleAuthorizationInfo.addStringPermission(sysUser.getPermission());// 可以添加多个
                return simpleAuthorizationInfo;
            }
        };
    }

    @Bean
    public DefaultShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();
        chainDefinition.addPathDefinition("/index", "anon");
        chainDefinition.addPathDefinition("/login", "anon");
        chainDefinition.addPathDefinition("/look", "anon");
        chainDefinition.addPathDefinition("/user", "authc");
        chainDefinition.addPathDefinition("/user/update", "authc,perms[user:update]");
        return chainDefinition;

    }


    @Bean
    public CacheManager cacheManager() {
        // Caching isn't needed in this example, but we will use the MemoryConstrainedCacheManager for this example.
        return new MemoryConstrainedCacheManager();
    }


    @Bean
    public ShiroDialect dialect(){
        return new ShiroDialect();
    }

}
