package com.thekirankumar.youtubeauto.bookmarks;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public final class BookmarkStore {
    private static final String PREFS = "carstream_bookmarks";
    private static final String KEY = "items";

    private BookmarkStore() {}

    public static synchronized ArrayList<Bookmark> load(Context context) {
        ArrayList<Bookmark> result = new ArrayList<>();
        if (context == null) return result;
        String raw = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).getString(KEY, "[]");
        try {
            JSONArray array = new JSONArray(raw);
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                Bookmark b = new Bookmark();
                b.setTitle(o.optString("title", ""));
                b.setUrl(o.optString("url", ""));
                b.setFaviconPath(o.optString("faviconPath", null));
                b.setFaviconResource(o.optInt("faviconResource", 0));
                b.setThumbnailResource(o.optInt("thumbnailResource", 0));
                b.setCreatedAt(o.optLong("createdAt", 0));
                b.setPreventDelete(o.optBoolean("preventDelete", false));
                String thumb = o.optString("thumbnail", "");
                if (!thumb.isEmpty()) b.setThumbnail(Base64.decode(thumb, Base64.DEFAULT));
                String fav = o.optString("favicon", "");
                if (!fav.isEmpty()) b.setFavicon(Base64.decode(fav, Base64.DEFAULT));
                result.add(b);
            }
        } catch (Exception ignored) {
        }
        return result;
    }

    public static synchronized void add(Context context, Bookmark bookmark) {
        ArrayList<Bookmark> items = load(context);
        items.add(bookmark);
        save(context, items);
    }

    public static synchronized void delete(Context context, Bookmark bookmark) {
        ArrayList<Bookmark> items = load(context);
        for (int i = items.size() - 1; i >= 0; i--) {
            Bookmark current = items.get(i);
            if (current.getCreatedAt() == bookmark.getCreatedAt()
                    && safe(current.getUrl()).equals(safe(bookmark.getUrl()))) {
                items.remove(i);
                break;
            }
        }
        save(context, items);
    }

    private static void save(Context context, ArrayList<Bookmark> items) {
        if (context == null) return;
        JSONArray array = new JSONArray();
        try {
            for (Bookmark b : items) {
                JSONObject o = new JSONObject();
                o.put("title", b.getTitle());
                o.put("url", b.getUrl());
                o.put("faviconPath", b.getFaviconPath());
                o.put("faviconResource", b.getFaviconResource());
                o.put("thumbnailResource", b.getThumbnailResource());
                o.put("createdAt", b.getCreatedAt());
                o.put("preventDelete", b.isPreventDelete());
                if (b.getThumbnail() != null) {
                    o.put("thumbnail", Base64.encodeToString(b.getThumbnail(), Base64.NO_WRAP));
                }
                if (b.getFavicon() != null) {
                    o.put("favicon", Base64.encodeToString(b.getFavicon(), Base64.NO_WRAP));
                }
                array.put(o);
            }
        } catch (Exception ignored) {
        }
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY, array.toString()).apply();
    }

    private static String safe(String value) {
        return value == null ? "" : value;
    }
}
