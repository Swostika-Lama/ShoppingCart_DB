package service;

import dao.TranslationDAO;
import db.DBConnection;

import java.sql.Connection;
import java.util.Locale;

public class LocalizationService {

    private final TranslationDAO translationDAO;

    public LocalizationService() {
        Connection conn = DBConnection.getConnection();
        this.translationDAO = new TranslationDAO(conn);
    }

    public String get(String key, Locale locale) {
        String text = translationDAO.findTranslation(key, locale.getLanguage(), locale.getCountry());
        if (text != null) return text;

        // fallback to English from DB only (no resource bundles)
        return translationDAO.findTranslation(key, "en", "US");
    }
}
