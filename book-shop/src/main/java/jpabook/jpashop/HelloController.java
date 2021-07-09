package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello")
    public String hello(Model model) {
        model.addAttribute("data","hello!!!");

        // 관례상 resource/templates/hello.html 로 리턴
        // Spring boot 의 thymeleaf가  viewName을 매핑
        return "hello";


    }
}
