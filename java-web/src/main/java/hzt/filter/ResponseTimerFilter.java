package hzt.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

/**
 * @see <a href="http://www.java2s.com/Tutorial/Java/0400__Servlet/Simplefilterformeasuringservletresponsetimes.htm">
 *     Simplefilterformeasuringservletresponsetimes</a>
 * @see <a href="https://www.learn-it-with-examples.com/development/java/web-tier/using-java-filter-for-logging-example.html">
 *     using-java-filter-for-logging-example</a>
 */
public class ResponseTimerFilter implements Filter {

    private FilterConfig filterConfig;

    public ResponseTimerFilter() {
        super();
    }

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
