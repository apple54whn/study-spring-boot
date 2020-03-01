package top.conanan.shiroweb.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.conanan.shiroweb.domain.SysUser;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author conanan
 * @since 2020-03-01
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class SysUserController {


    /**
     * 登录
     * @param username
     * @param password
     * @param rememberMe
     * @param model
     * @return
     */
    @PostMapping("login")
    public String login(String username, String password, Boolean rememberMe, Model model) {

        Subject currentUser = SecurityUtils.getSubject();

        if (!currentUser.isAuthenticated()) {
            // 1. 收集 Subject 的 principals（主要信息，一般是唯一的，这里即 username） and credentials（证书，凭证，这里即 password）
            // 使用这个令牌来绑定我们在Java应用程序中以某种方式获得的用户名和密码
            UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
            // usernamePasswordToken.setRememberMe(true);// 也可以在构造中设置
            // 在shiro中非常重要的一点是，记住的Subject（记忆中的）不一定是经过认证的Subject（实际的）。
            // 所以敏感操作需要继续判断是否经过认证甚至查看权限。Subject有方法来确定。
            try {
                // 2. 将 principals 和 credentials 提交到身份验证系统，进行认证（会自动调用 doGetAuthenticationInfo 方法）
                currentUser.login(token);
                model.addAttribute("username",username);
                return "index";
                // 3. 允许访问、重试身份验证或阻塞访问
            } catch (UnknownAccountException uae) {
                log.info("该用户不存在：" + token.getPrincipal() + "（不推荐，只需返回账号或密码错误）");
                model.addAttribute("msg", "该用户不存在");
                return "login";
            } catch (IncorrectCredentialsException ice) {
                log.info(token.getPrincipal() + "账号的密码错误!（不推荐，只需返回账号或密码错误）");
                model.addAttribute("msg", "密码错误");
                return "login";
            } catch (LockedAccountException lae) {
                log.info("账号" + token.getPrincipal() + " 已被锁，请联系管理员解锁。");
                model.addAttribute("msg", "账号已被锁，请联系管理员解锁。");
                return "login";
            }
            // 在这里捕获更多的异常(可以是特定于您的应用程序的自定义异常)
            // 可参考该AuthenticationException接口，https://shiro.apache.org/static/1.5.1/apidocs/org/apache/shiro/authc/AuthenticationException.html
            catch (AuthenticationException ae) {
                //unexpected condition?  error?
                log.error("登录时发生未知错误：{}", ae);
                model.addAttribute("msg", "未知错误，请联系管理员。");
                return "login";
            }
        }
        return "index";
    }


    @GetMapping("logout")
    public String logout(){
        SecurityUtils.getSubject().logout();
        return "index";
    }


    /**
     * 查看用户信息
     */
    @GetMapping
    public String add() {
        return "user/index";
    }

    /**
     * 更改用户信息
     */
    @GetMapping("/update")
    public String update() {
        return "user/update";
    }
}
