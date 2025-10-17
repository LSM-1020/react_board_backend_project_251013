package com.LSM.home.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
public class WebController {
 
	@RequestMapping(value = {"/{path:^(?!api$|static|index\\.html$).*$}/**" })
	    public String forward() {
	        return "forward:/index.html";
	    
	}
	   //api요청->스프링 부트 요청 적용
	   //api요청을 제외한 react라우터 요청으로 변경->리액트의 "/"로 시작하는 요청으로 변경
}
