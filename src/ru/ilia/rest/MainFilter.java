package ru.ilia.rest;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by ILIA on 24.01.2017.
 */
public class MainFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        HttpServletRequest req = (HttpServletRequest) request;
        String path = req.getRequestURI().substring(req.getContextPath().length());
        System.out.println("path: "+path);
        if(path.startsWith("/dist/")){
            System.out.println("request js or css");
            chain.doFilter(request,response);
            return;
        }
        if(path.startsWith("/ws/")){
            System.out.println("request REST service");
            chain.doFilter(request,response);
            return;
        }
        System.out.println("else dispatch");
        request.getRequestDispatcher("/index.html").forward(request, response);
    }

    @Override
    public void destroy() {

    }
}
