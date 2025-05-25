package project_game.loupGarou.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ControllerWereWolf {
    
@GetMapping("/")
public String getMethodName(String param) {
    return "test";
}



}
