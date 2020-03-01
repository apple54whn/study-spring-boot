package top.conanan.shiroweb;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@SpringBootApplication
@MapperScan("top.conanan.shiroweb.mapper")
@Controller
@Slf4j
public class ShiroWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiroWebApplication.class, args);
    }

    /**
     * 首页
     */
    @GetMapping({"", "index"})
    public String index(Model model) {
        return "index";
    }

    /**
     * 登录页
     */
    @GetMapping("login")
    public String loginUrl() {
        return "login";
    }

    /**
     * 401未授权
     */
    @GetMapping("401")
    public String unauthorizedUrl() {
        return "401";
    }


    /**
     * 随便看看
     */
    @GetMapping("look")
    public String look() {
        return "look";
    }

}
