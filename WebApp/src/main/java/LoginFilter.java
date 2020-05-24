package main.java;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Servlet Filter implementation class LoginFilter
 */
@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")
public class LoginFilter implements Filter
{
    private final ArrayList<String> allowedURIs = new ArrayList<>();

    /**
     * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
     */
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
    {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        System.out.println("LoginFilter: " + httpRequest.getRequestURI());

        if (this.isUrlAllowedWithoutLogin(httpRequest.getRequestURI()))
        {
            chain.doFilter(request, response);
            return;
        }

        if (httpRequest.getSession().getAttribute("user") == null)
        {
            httpResponse.sendRedirect("login.html");
            //return;
        }
        else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy()
    {
        //ignore
    }

    private boolean isUrlAllowedWithoutLogin(String requestURI)
    {
        return allowedURIs.stream().anyMatch(requestURI.toLowerCase()::endsWith);
    }

    public void init(FilterConfig fConfig)
    {
        allowedURIs.add("login.html");
        allowedURIs.add("js/login.js");
        allowedURIs.add("api/login");
        //allowedURIs.add("employeelogin.html");
        //allowedURIs.add("js/employeelogin.js");
        //allowedURIs.add("api/employeelogin");
    }
}

