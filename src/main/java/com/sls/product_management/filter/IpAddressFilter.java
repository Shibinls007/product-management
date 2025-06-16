package com.sls.product_management.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class IpAddressFilter extends OncePerRequestFilter {

    private final Set<String> allowedIps;

    public IpAddressFilter(@Value("${app.security.allowed-ips}") String[] allowedIps) {
        this.allowedIps = new HashSet<>(Arrays.asList(allowedIps));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String remoteIp = request.getRemoteAddr();

        if (allowedIps.contains(remoteIp)) {
            log.info("Allowed access for IP: {}", remoteIp);
            filterChain.doFilter(request, response);
        } else {
            log.warn("Forbidden access for IP: {}", remoteIp);
            response.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied");
        }
    }
}