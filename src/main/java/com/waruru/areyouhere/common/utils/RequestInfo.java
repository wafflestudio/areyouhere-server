package com.waruru.areyouhere.common.utils;

import jakarta.servlet.http.HttpServletRequest;

public record RequestInfo(
        String requestURL,
        String method,
        String remoteAddress
) {

    public RequestInfo(String requestURL, String method, String remoteAddress) {
        this.requestURL = requestURL;
        this.method = method;
        this.remoteAddress = remoteAddress;
    }

    public RequestInfo(HttpServletRequest request) {
        this(String.valueOf(request.getRequestURL()),
                request.getMethod(),
                request.getRemoteAddr()
        );
    }
}
