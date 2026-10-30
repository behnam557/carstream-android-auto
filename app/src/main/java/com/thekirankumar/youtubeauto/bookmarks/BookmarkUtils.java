package com.thekirankumar.youtubeauto.bookmarks;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.TypedValue;

import com.thekirankumar.youtubeauto.webview.VideoWebView;

import java.io.ByteArrayOutputStream;

/**
 * Created by kiran.kumar on 24/01/18.
 */

public class BookmarkUtils {
    public static void addBookmark(final VideoWebView webView) {
        webView.scrollTo(0,0);
        webView.post(new Runnable() {
            @Override
            public void run() {
                final Bookmark bookmark = new Bookmark();
                bookmark.setTitle(webView.getTitle());
                bookmark.setUrl(webView.getUrl());
                Bitmap bitmap = Bitmap.createBitmap(webView.getMeasuredWidth(), webView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                float aspectRatio = (float) webView.getMeasuredWidth() / (float) webView.getMeasuredHeight();
                Canvas c = new Canvas(bitmap);
                webView.draw(c);

                Resources r = webView.getResources();
                float px = 3 * TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, BookmarksFragment.BOOKMARK_VIEW_SIZE_IN_DP, r.getDisplayMetrics());

                bitmap = Bitmap.createScaledBitmap(bitmap, (int) px, (int) (px / aspectRatio), false);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                bookmark.setThumbnail(byteArray);

                Bitmap favicon = webView.getFavicon();
                if(favicon!=null) {
                    ByteArrayOutputStream faviconStream = new ByteArrayOutputStream();
                    favicon.compress(Bitmap.CompressFormat.PNG, 80, faviconStream);
                    byte[] faviconBytes = faviconStream.toByteArray();
                    bookmark.setFavicon(faviconBytes);
                }

                bookmark.setCreatedAt(System.currentTimeMillis());
                BookmarkStore.add(webView.getContext(), bookmark);
            }
        });
    }

    public static void deleteBookmark(android.content.Context context, final Bookmark bookmark) {
        BookmarkStore.delete(context, bookmark);
    }
}

