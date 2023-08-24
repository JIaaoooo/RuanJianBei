//package com.example.ruanjian.filter;
//
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@WebFilter
//public class CorsFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        req.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
//        HttpServletResponse response = (HttpServletResponse) res;
//        response.setHeader("Access-Control-Allow-Origin", ((HttpServletRequest) req).getHeader("Origin"));
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
//        response.setHeader("Access-Control-Max-Age", "3600");
//        response.setHeader("Access-Control-Allow-Headers", "Content-Disposition,Origin, X-Requested-With, Content-Type, Accept,Authorization,id_token");
//        response.setHeader("Access-Control-Allow-Credentials","true");
//        response.setHeader("Content-Security-Policy", "default-src 'self' 'unsafe-inline'; script-src 'self'; frame-ancestors 'self'; object-src 'none'");
//        response.setHeader("X-Content-Type-Options", "nosniff");
//        response.setHeader("X-XSS-Protection", "1; mode=block");
//        chain.doFilter(req, res);
//    }
//}
