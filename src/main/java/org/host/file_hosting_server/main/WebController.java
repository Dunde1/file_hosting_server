package org.host.file_hosting_server.main;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String index() {return "index";}
    @GetMapping("/mit09")
    public String mit09() {return "mit09";}
}
