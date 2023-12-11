package study.tipsyboy.tipsyboyMall.member.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public String main() {
        return "Issue Test 중입니다.";
    }
}
