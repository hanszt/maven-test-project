package hzt.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * http://www.java2s.com/Tutorial/Java/0400__Servlet/Simplefilterformeasuringservletresponsetimes.htm
 * https://www.learn-it-with-examples.com/development/java/web-tier/using-java-filter-for-logging-example.html
 */
public class ResponseTimerFilter implements Filter {

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        long startTime = System.nanoTime();
        filterChain.doFilter(servletRequest, servletResponse);
        long elapsedNanos = System.nanoTime() - startTime;
        String name = servletRequest instanceof HttpServletRequest request ? request.getRequestURI() : "servlet";
        filterConfig.getServletContext().log(name + " took " + elapsedNanos + " nano seconds");
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
