package hzt.filter;

import jakarta.servlet.AsyncContext;
import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.SessionCookieConfig;
import jakarta.servlet.SessionTrackingMode;
import jakarta.servlet.descriptor.JspConfigDescriptor;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpUpgradeHandler;
import jakarta.servlet.http.Part;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

class ResponseTimerFilterTest {

    private static void executeFilterChain(ServletRequest request, ServletResponse response) {
        System.out.println("Filter chain executed called via method reference");
    }

    private static void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
        System.out.println("Executing do filter via method reference...");
        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new IllegalStateException();
        }
        System.out.println("Do filter executed via method reference");
    }

    @Test
    void testDoFilter() throws ServletException, IOException {
        Filter lambdaFilter = ResponseTimerFilterTest::doFilter;
        ResponseTimerFilter filter = new ResponseTimerFilter();
        final var request = getHttpServletRequest();
        final var response = getServletResponse();

        lambdaFilter.doFilter(request, response, ResponseTimerFilterTest::executeFilterChain);
        Assertions.assertThrows(NullPointerException.class, () ->
                filter.doFilter(request, response, ResponseTimerFilterTest::executeFilterChain));
    }

    private ServletResponse getServletResponse() {
        return new ServletResponse() {
            @Override
            public String getCharacterEncoding() {
                return null;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public ServletOutputStream getOutputStream(){
                return null;
            }

            @Override
            public PrintWriter getWriter() {
                return null;
            }

            @Override
            public void setCharacterEncoding(String s) {

            }

            @Override
            public void setContentLength(int i) {

            }

            @Override
            public void setContentLengthLong(long l) {

            }

            @Override
            public void setContentType(String s) {

            }

            @Override
            public void setBufferSize(int i) {

            }

            @Override
            public int getBufferSize() {
                return 0;
            }

            @Override
            public void flushBuffer() {

            }

            @Override
            public void resetBuffer() {

            }

            @Override
            public boolean isCommitted() {
                return false;
            }

            @Override
            public void reset() {

            }

            @Override
            public void setLocale(Locale locale) {

            }

            @Override
            public Locale getLocale() {
                return null;
            }
        };
    }

    private HttpServletRequest getHttpServletRequest() {
        return new HttpServletRequest() {
            @Override
            public String getAuthType() {
                return null;
            }

            @Override
            public Cookie[] getCookies() {
                return new Cookie[0];
            }

            @Override
            public long getDateHeader(String s) {
                return 0;
            }

            @Override
            public String getHeader(String s) {
                return null;
            }

            @Override
            public Enumeration<String> getHeaders(String s) {
                return null;
            }

            @Override
            public Enumeration<String> getHeaderNames() {
                return null;
            }

            @Override
            public int getIntHeader(String s) {
                return 0;
            }

            @Override
            public String getMethod() {
                return null;
            }

            @Override
            public String getPathInfo() {
                return null;
            }

            @Override
            public String getPathTranslated() {
                return null;
            }

            @Override
            public String getContextPath() {
                return null;
            }

            @Override
            public String getQueryString() {
                return null;
            }

            @Override
            public String getRemoteUser() {
                return null;
            }

            @Override
            public boolean isUserInRole(String s) {
                return false;
            }

            @Override
            public Principal getUserPrincipal() {
                return null;
            }

            @Override
            public String getRequestedSessionId() {
                return null;
            }

            @Override
            public String getRequestURI() {
                return null;
            }

            @Override
            public StringBuffer getRequestURL() {
                return null;
            }

            @Override
            public String getServletPath() {
                return null;
            }

            @Override
            public HttpSession getSession(boolean b) {
                return null;
            }

            @Override
            public HttpSession getSession() {
                return null;
            }

            @Override
            public String changeSessionId() {
                return null;
            }

            @Override
            public boolean isRequestedSessionIdValid() {
                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromCookie() {
                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromURL() {
                return false;
            }

            @Override
            public boolean isRequestedSessionIdFromUrl() {
                return false;
            }

            @Override
            public boolean authenticate(HttpServletResponse httpServletResponse) {
                return false;
            }

            @Override
            public void login(String s, String s1)  {

            }

            @Override
            public void logout() {

            }

            @Override
            public Collection<Part> getParts() {
                return null;
            }

            @Override
            public Part getPart(String s) {
                return null;
            }

            @Override
            public <T extends HttpUpgradeHandler> T upgrade(Class<T> aClass) {
                return null;
            }

            @Override
            public Object getAttribute(String s) {
                return null;
            }

            @Override
            public Enumeration<String> getAttributeNames() {
                return null;
            }

            @Override
            public String getCharacterEncoding() {
                return null;
            }

            @Override
            public void setCharacterEncoding(String s) {

            }

            @Override
            public int getContentLength() {
                return 0;
            }

            @Override
            public long getContentLengthLong() {
                return 0;
            }

            @Override
            public String getContentType() {
                return null;
            }

            @Override
            public ServletInputStream getInputStream() {
                return null;
            }

            @Override
            public String getParameter(String s) {
                return null;
            }

            @Override
            public Enumeration<String> getParameterNames() {
                return null;
            }

            @Override
            public String[] getParameterValues(String s) {
                return new String[0];
            }

            @Override
            public Map<String, String[]> getParameterMap() {
                return null;
            }

            @Override
            public String getProtocol() {
                return null;
            }

            @Override
            public String getScheme() {
                return null;
            }

            @Override
            public String getServerName() {
                return null;
            }

            @Override
            public int getServerPort() {
                return 0;
            }

            @Override
            public BufferedReader getReader() {
                return null;
            }

            @Override
            public String getRemoteAddr() {
                return null;
            }

            @Override
            public String getRemoteHost() {
                return null;
            }

            @Override
            public void setAttribute(String s, Object o) {

            }

            @Override
            public void removeAttribute(String s) {

            }

            @Override
            public Locale getLocale() {
                return null;
            }

            @Override
            public Enumeration<Locale> getLocales() {
                return null;
            }

            @Override
            public boolean isSecure() {
                return false;
            }

            @Override
            public RequestDispatcher getRequestDispatcher(String s) {
                return null;
            }

            @Override
            public String getRealPath(String s) {
                return null;
            }

            @Override
            public int getRemotePort() {
                return 0;
            }

            @Override
            public String getLocalName() {
                return null;
            }

            @Override
            public String getLocalAddr() {
                return null;
            }

            @Override
            public int getLocalPort() {
                return 0;
            }

            @Override
            public ServletContext getServletContext() {
                return new ServletContext() {
                    @Override
                    public String getContextPath() {
                        return null;
                    }

                    @Override
                    public ServletContext getContext(String s) {
                        return null;
                    }

                    @Override
                    public int getMajorVersion() {
                        return 0;
                    }

                    @Override
                    public int getMinorVersion() {
                        return 0;
                    }

                    @Override
                    public int getEffectiveMajorVersion() {
                        return 0;
                    }

                    @Override
                    public int getEffectiveMinorVersion() {
                        return 0;
                    }

                    @Override
                    public String getMimeType(String s) {
                        return null;
                    }

                    @Override
                    public Set<String> getResourcePaths(String s) {
                        return null;
                    }

                    @Override
                    public URL getResource(String s) {
                        return null;
                    }

                    @Override
                    public InputStream getResourceAsStream(String s) {
                        return null;
                    }

                    @Override
                    public RequestDispatcher getRequestDispatcher(String s) {
                        return null;
                    }

                    @Override
                    public RequestDispatcher getNamedDispatcher(String s) {
                        return null;
                    }

                    @Override
                    public Servlet getServlet(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<Servlet> getServlets() {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getServletNames() {
                        return null;
                    }

                    @Override
                    public void log(String s) {

                    }

                    @Override
                    public void log(Exception e, String s) {

                    }

                    @Override
                    public void log(String s, Throwable throwable) {

                    }

                    @Override
                    public String getRealPath(String s) {
                        return null;
                    }

                    @Override
                    public String getServerInfo() {
                        return null;
                    }

                    @Override
                    public String getInitParameter(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getInitParameterNames() {
                        return null;
                    }

                    @Override
                    public boolean setInitParameter(String s, String s1) {
                        return false;
                    }

                    @Override
                    public Object getAttribute(String s) {
                        return null;
                    }

                    @Override
                    public Enumeration<String> getAttributeNames() {
                        return null;
                    }

                    @Override
                    public void setAttribute(String s, Object o) {

                    }

                    @Override
                    public void removeAttribute(String s) {

                    }

                    @Override
                    public String getServletContextName() {
                        return null;
                    }

                    @Override
                    public ServletRegistration.Dynamic addServlet(String s, String s1) {
                        return null;
                    }

                    @Override
                    public ServletRegistration.Dynamic addServlet(String s, Servlet servlet) {
                        return null;
                    }

                    @Override
                    public ServletRegistration.Dynamic addServlet(String s, Class<? extends Servlet> aClass) {
                        return null;
                    }

                    @Override
                    public ServletRegistration.Dynamic addJspFile(String s, String s1) {
                        return null;
                    }

                    @Override
                    public <T extends Servlet> T createServlet(Class<T> aClass) {
                        return null;
                    }

                    @Override
                    public ServletRegistration getServletRegistration(String s) {
                        return null;
                    }

                    @Override
                    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
                        return null;
                    }

                    @Override
                    public FilterRegistration.Dynamic addFilter(String s, String s1) {
                        return null;
                    }

                    @Override
                    public FilterRegistration.Dynamic addFilter(String s, Filter filter) {
                        return null;
                    }

                    @Override
                    public FilterRegistration.Dynamic addFilter(String s, Class<? extends Filter> aClass) {
                        return null;
                    }

                    @Override
                    public <T extends Filter> T createFilter(Class<T> aClass) {
                        return null;
                    }

                    @Override
                    public FilterRegistration getFilterRegistration(String s) {
                        return null;
                    }

                    @Override
                    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
                        return null;
                    }

                    @Override
                    public SessionCookieConfig getSessionCookieConfig() {
                        return null;
                    }

                    @Override
                    public void setSessionTrackingModes(Set<SessionTrackingMode> set) {

                    }

                    @Override
                    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
                        return null;
                    }

                    @Override
                    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
                        return null;
                    }

                    @Override
                    public void addListener(String s) {

                    }

                    @Override
                    public <T extends EventListener> void addListener(T t) {

                    }

                    @Override
                    public void addListener(Class<? extends EventListener> aClass) {

                    }

                    @Override
                    public <T extends EventListener> T createListener(Class<T> aClass) {
                        return null;
                    }

                    @Override
                    public JspConfigDescriptor getJspConfigDescriptor() {
                        return null;
                    }

                    @Override
                    public ClassLoader getClassLoader() {
                        return null;
                    }

                    @Override
                    public void declareRoles(String... strings) {

                    }

                    @Override
                    public String getVirtualServerName() {
                        return null;
                    }

                    @Override
                    public int getSessionTimeout() {
                        return 0;
                    }

                    @Override
                    public void setSessionTimeout(int i) {

                    }

                    @Override
                    public String getRequestCharacterEncoding() {
                        return null;
                    }

                    @Override
                    public void setRequestCharacterEncoding(String s) {

                    }

                    @Override
                    public String getResponseCharacterEncoding() {
                        return null;
                    }

                    @Override
                    public void setResponseCharacterEncoding(String s) {

                    }
                };
            }

            @Override
            public AsyncContext startAsync() throws IllegalStateException {
                return null;
            }

            @Override
            public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
                return null;
            }

            @Override
            public boolean isAsyncStarted() {
                return false;
            }

            @Override
            public boolean isAsyncSupported() {
                return false;
            }

            @Override
            public AsyncContext getAsyncContext() {
                return null;
            }

            @Override
            public DispatcherType getDispatcherType() {
                return null;
            }
        };
    }
}
