package ru.ilia.rest.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Перекдирует запросы в utf-8
 *
 * Класс используется для отдачи статики (frontend) по пути /dist/*;
 * Доступа к REST сервису по пути /ws/*;
 * В иных случаях отдается index.html
 */
public class MainFilter implements Filter {

    FilterConfig config;

    public void setFilterConfig(FilterConfig config) {
        this.config = config;
    }

    public FilterConfig getFilterConfig() {
        return config;
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
        setFilterConfig(config);
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
