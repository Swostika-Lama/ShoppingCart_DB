package service;

import dao.TranslationDAO;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LocalizationService {

    private final TranslationDAO translationDAO;

    // Simple in-memory cache: key_locale → translated text
    private final Map<String, String> cache = new HashMap<>();

    public LocalizationService(Connection conn) {
        this.translationDAO = new TranslationDAO(conn);
    }

    public String get(String key, Locale locale) {
        String cacheKey = key + "_" + locale.toString();

        // 1. Check cache first
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        // 2. Try requested language
        String text = translationDAO.findTranslation(
                key,
                locale.getLanguage(),
                locale.getCountry()
        );

        // 3. Fallback to English
        if (text == null) {
            text = translationDAO.findTranslation(key, "en", "US");
        }

        // 4. Final fallback (debug-friendly)
        if (text == null) {
            text = "[" + key + "]";
        }

        // 5. Store in cache
        cache.put(cacheKey, text);

        return text;
    }
}
