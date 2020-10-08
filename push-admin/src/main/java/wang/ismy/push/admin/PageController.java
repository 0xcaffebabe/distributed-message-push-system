package wang.ismy.push.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author MY
 * @date 2020/9/21 20:18
 */
@Controller
public class PageController {

    @RequestMapping("/")
    public String homePage() {
        return "forward:/dashboard.html";
    }
}
