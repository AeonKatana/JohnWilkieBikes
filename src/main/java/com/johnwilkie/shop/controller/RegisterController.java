package com.johnwilkie.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegisterController {
  @RequestMapping({"/register-complete"})
  private String registercomplete() {
    return "registercomplete";
  }
}
