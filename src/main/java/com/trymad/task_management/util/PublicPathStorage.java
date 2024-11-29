package com.trymad.task_management.util;

import java.util.List;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class PublicPathStorage {

    private final List<RequestMatcher> publicPaths = List.of(
            new AntPathRequestMatcher("/api/doc/**"),
            new AntPathRequestMatcher("/api/v1/auth/**"));

    public boolean isPublicPath(HttpServletRequest request) {
        return publicPaths.stream().anyMatch(antPublic -> antPublic.matches(request));
    }

    public List<RequestMatcher> getPathsAsList() {
        return this.publicPaths;
    }

    public RequestMatcher[] getPathsAsArray() {
        return this.publicPaths.toArray(new RequestMatcher[this.publicPaths.size()]);
    }
}
