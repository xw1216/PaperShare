package tech.outspace.papershare.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import tech.outspace.papershare.utils.network.HttpFormat;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OptionsRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getMethod().equals(HttpFormat.methodsList.get(2))) {
            response.setHeader(HttpFormat.allowMethodsHeader, "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            response.setHeader(HttpFormat.allowHeader, response.getHeader(HttpFormat.requestHeader));
            return;
        }
        filterChain.doFilter(request, response);
    }
}
