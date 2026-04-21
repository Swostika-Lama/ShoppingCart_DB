package service;

import dao.TranslationDAO;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class LocalizationService {

    private final TranslationDAO translationDAO;

    // cache: key_languageId → translation
    private final Map<String, String> cache = new HashMap<>();

    // English language ID (fallback)
    private static final int DEFAULT_LANGUAGE_ID = 1;

    public LocalizationService(Connection conn) {
        this.translationDAO = new TranslationDAO(conn);
    }

    public String get(String key, int languageId) {

        String cacheKey = key + "_" + languageId;

        // 1. Check cache first
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        // 2. Try requested language (e.g., Arabic / Japanese)
        String text = translationDAO.findTranslation(key, languageId);

        // 3. Fallback to English ONLY if needed
        if (text == null || text.isEmpty()) {
            text = translationDAO.findTranslation(key, DEFAULT_LANGUAGE_ID);
        }

        // 4. Final fallback (shows missing key clearly)
        if (text == null || text.isEmpty()) {
            text = "[" + key + "]";
        }

        // 5. Cache result
        cache.put(cacheKey, text);

        return text;
    }

    // Optional: clear cache when switching language
    public void clearCache() {
        cache.clear();
    }
}