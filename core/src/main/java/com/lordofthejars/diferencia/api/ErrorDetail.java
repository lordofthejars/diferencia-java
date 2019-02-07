package com.lordofthejars.diferencia.api;

import java.util.Map;

public class ErrorDetail {

    private String fullUri;
    private Map<String, String> originalHeaders;
    private String originalBody;
    private String bodyDiff;
    private String headersDiff;
    private String statusDiff;

    public ErrorDetail(String fullUri, Map<String, String> originalHeaders, String originalBody, String bodyDiff,
        String headersDiff, String statusDiff) {
        this.fullUri = fullUri;
        this.originalHeaders = originalHeaders;
        this.originalBody = originalBody;
        this.bodyDiff = bodyDiff;
        this.headersDiff = headersDiff;
        this.statusDiff = statusDiff;
    }

    public String getFullUri() {
        return fullUri;
    }

    public Map<String, String> getOriginalHeaders() {
        return originalHeaders;
    }

    public String getOriginalBody() {
        return originalBody;
    }

    public String getBodyDiff() {
        return bodyDiff;
    }

    public String getHeadersDiff() {
        return headersDiff;
    }

    public String getStatusDiff() {
        return statusDiff;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ErrorDetail{");
        sb.append("fullUri='").append(fullUri).append('\'');
        sb.append(", originalHeaders=").append(originalHeaders);
        sb.append(", originalBody='").append(originalBody).append('\'');
        sb.append(", bodyDiff='").append(bodyDiff).append('\'');
        sb.append(", headersDiff='").append(headersDiff).append('\'');
        sb.append(", statusDiff='").append(statusDiff).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
