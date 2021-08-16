package com.koeni.myRetail;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyRetailController {

    @GetMapping("/ping")
    public static String getPing() {
        return "Pong";
    }

}
