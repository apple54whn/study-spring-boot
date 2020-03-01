package top.conanan.shiroquickstart;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.env.BasicIniEnvironment;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class ShiroQuickstartApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroQuickstartApplication.class, args);


        /*
         创建配置了Shiro SecurityManager的最简单方法, realms, users, roles 和 permissions 使用了简单的 INI 配置
         官方给的示例中使用 IniSecurityManagerFactory 构造来获取 SecurityManager 的方式已经过时，推荐使用 Shiro 的 Environment 替代
         */
        BasicIniEnvironment basicIniEnvironment = new BasicIniEnvironment("classpath:shiro.ini");
        SecurityManager securityManager = basicIniEnvironment.getSecurityManager();

        /*
         对于这个简单的快速入门示例，使用SecurityManager可作为JVM单例访问。大多数应用程序不会这样做而是依赖于它们的容器配置或 webapps 的web.xml。
         */
        SecurityUtils.setSecurityManager(securityManager);


        /*
         获取当前执行的用户（安全领域公认的命名 Subject）
         独立应用程序中的 getSubject() 调用可能基于特定于应用程序位置的用户数据返回 Subject，而在服务器环境(例如web应用程序)中，它基于与当前线程或传入请求相关的用户数据获取 Subject。
         */
        Subject currentUser = SecurityUtils.getSubject();

        /*
         使用 Session 做一些事情(不需要web或EJB容器!!)
         该 Session 是一个特定于 Shiro 的实例，它提供了常规 HttpSession 所提供的大部分内容，但也提供了一些额外的好处，还有一个很大的区别: 它不需要HTTP环境!
         如果在 web 应用程序中部署，默认情况下会话将基于 HttpSession。但是，在非 web 环境中，比如这个简单的快速入门，Shiro默认情况下会自动使用它的企业会话管理。
         这意味着您可以在任何层的应用程序中使用相同的API，而不管部署环境如何。这打开了一个全新的应用程序世界，因为任何需要会话的应用程序都不需要强制使用HttpSession或EJB有状态会话bean。
         而且，任何客户机技术现在都可以共享会话数据。
         */
        Session session = currentUser.getSession();
        session.setAttribute("hello", "conanan");
        String value = (String) session.getAttribute("hello");
        if (value.equals("conanan")) {
            log.info("获取到正确的值! [" + value + "]");
        }

        /*
         登录当前用户，可以检查角色和权限
         我们只能对已知用户进行这些检查。上面的 Subject 实例表示当前用户，但是谁是当前用户呢?嗯，他们是匿名的——也就是说，直到他们至少登录一次。让我们这样做:
         */
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken("lonestarr", "vespa",true);
            // token.setRememberMe(true); //也可以在构造中第三个参数添加
            try {
                // 登录失败需捕获异常
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                log.info("该用户不存在：" + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {
                log.info(token.getPrincipal() + "账号的密码错误!");
            } catch (LockedAccountException lae) {
                log.info("账号" + token.getPrincipal() + " 已被锁.  " +
                        "请联系管理员解锁.");
            }
            // 在这里捕获更多的异常(可能是特定于您的应用程序的自定义异常?)
            // 可参考https://shiro.apache.org/static/1.5.1/apidocs/org/apache/shiro/authc/AuthenticationException.html
            catch (AuthenticationException ae) {
                //unexpected condition?  error?
            }
        }

        /*
         用户登陆后可以做如下事情：告诉我们当前用户是谁，输出他的 identifying principal（主要身份，在这个示例中是 username）
         */
        log.info("用户 [" + currentUser.getPrincipal() + "] 登陆成功.");

        /*
         测试是否有这个角色
         */
        if (currentUser.hasRole("schwartz")) {
            log.info("May the Schwartz be with you!");
        } else {
            log.info("Hello, mere mortal.");
        }

        /*
         我们还可以看到他们是否有权限对某种类型的实体采取行动:
         */
        if (currentUser.isPermitted("lightsaber:wield")) {
            log.info("You may use a lightsaber ring.  Use it wisely.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }

        /*
         此外，我们可以执行一个非常强大的实例级权限检查——查看用户是否有能力访问某个类型的特定实例:
         */
        if (currentUser.isPermitted("winnebago:drive:eagle5")) {
            log.info("You are permitted to 'drive' the winnebago with license plate (id) 'eagle5'.  " + "Here are the keys - have fun!");
        } else {
            log.info("Sorry, you aren't allowed to drive the 'eagle5' winnebago!");
        }

        /*
         退出。删除所有标识信息并使其会话无效。
         因为 remember 标示通常使用cookie表示，定向到登陆页面，设置同名cookie且maxAge为0
         */
        currentUser.logout();

        System.exit(0);
    }
}
