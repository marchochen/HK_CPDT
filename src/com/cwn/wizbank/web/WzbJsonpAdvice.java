package com.cwn.wizbank.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

@ControllerAdvice
public class WzbJsonpAdvice extends AbstractJsonpResponseBodyAdvice{
	public WzbJsonpAdvice() {  
        super("callback", "jsonp"); //指定jsonpParameterNames  
    }  
}
