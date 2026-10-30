package com.thekirankumar.youtubeauto.utils;

import android.webkit.WebResourceResponse;

import java.io.ByteArrayInputStream;
import java.util.Locale;

/**
 * Lightweight request filter for common advertising and tracking endpoints.
 *
 * This intentionally avoids blocking googlevideo.com because YouTube serves
 * both normal video and adverts from that CDN. In-stream YouTube adverts are
 * handled by the page-side enhancement layer in VideoWebView.
 */
public final class YouTubeAdBlocker {

    private static final String[] BLOCKED_HOST_PARTS = new String[]{
            "doubleclick.net",
            "googlesyndication.com",
            "googleadservices.com",
            "adservice.google.",
            "google-analytics.com",
            "googletagmanager.com"
    };

    private static final String[] BLOCKED_URL_PARTS = new String[]{
            "/pagead/",
            "/api/stats/ads",
            "/ptracking",
            "/get_midroll_info",
            "ad_break",
            "adformat="
    };

    private YouTubeAdBlocker() {
    }

    public static boolean shouldBlock(String url) {
        if (url == null) {
            return false;
        }

        String lower = url.toLowerCase(Locale.US);

        for (String blocked : BLOCKED_HOST_PARTS) {
            if (lower.contains(blocked)) {
                return true;
            }
        }

        if (lower.contains("youtube.com") || lower.contains("youtube-nocookie.com")) {
            for (String blocked : BLOCKED_URL_PARTS) {
                if (lower.contains(blocked)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static WebResourceResponse emptyResponse() {
        return new WebResourceResponse(
                "text/plain",
                "UTF-8",
                new ByteArrayInputStream(new byte[0])
        );
    }
}
