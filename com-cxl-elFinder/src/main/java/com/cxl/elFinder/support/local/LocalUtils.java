package com.cxl.elFinder.support.local;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


public class LocalUtils {
    private static final ConcurrentMap<String, List<Locale>> LanguagesByCountry = new ConcurrentHashMap<>();

    private static final ConcurrentMap<String, List<Locale>> CountriesByLanguage = new ConcurrentHashMap<>();

    public LocalUtils() {
        super();
    }

    public static Locale toLocale(final String str) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty()) {
            return new Locale("", "");
        }
        if (str.contains("#")) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final int len = str.length();
        if (len < 2) {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
        final char ch0 = str.charAt(0);
        if (ch0 == '_') {
            if (len < 3) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            final char ch1 = str.charAt(1);
            final char ch2 = str.charAt(2);
            if (!Character.isUpperCase(ch1) || !Character.isUpperCase(ch2)) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            if (len == 3) {
                return new Locale("", str.substring(1, 3));
            }
            if (len < 5) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            if (str.charAt(3) != '_') {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            }
            return new Locale("", str.substring(1, 3));
        }

        final String[] split = str.split("_", -1);
        final int occurrences = split.length - 1;
        switch (occurrences) {
            case 0:
                if (isAllLowerCase(str) && (len == 2 || len == 3)) {
                    return new Locale(str);
                }
                throw new IllegalArgumentException("Invalid locale format: " + str);
            case 1:
                if (isAllLowerCase(split[0]) && (split[0].length() == 3 || split[0].length() == 2) && split[1].length() == 2 && isAllLowerCase(split[1])) {
                    return new Locale(split[0], split[1]);
                }
                throw new IllegalArgumentException("Invalid locale format: " + str);
            case 2:
                if (isAllLowerCase(split[0])&&(split[0].length()==2||split[0].length()==3)&&(split[1].length()==0||(split[1].length()==2&&isAllUpperCase(split[1])))&&split[2].length()>0) {
                    return new Locale(split[0],split[1],split[2]);
                }
                default:
                    throw new IllegalArgumentException("Invalid locale format: " + str);
        }
    }

    public static List<Locale> localeLookupList(final Locale locale){
        return localeLookupList(locale,locale);
    }

    private static List<Locale> localeLookupList(Locale locale,final Locale defaultLocale) {
        final List<Locale> list=new ArrayList<>(4);
        if (locale != null) {
            list.add(locale);
            if (locale.getVariant().length()>0) {
                list.add(new Locale(locale.getLanguage(),locale.getCountry()));
            }
            if (locale.getCountry().length()>0) {
                list.add(new Locale(locale.getLanguage(),""));
            }
            if (!list.contains(defaultLocale)) {
                list.add(defaultLocale);
            }
        }
        return Collections.unmodifiableList(list);
    }

    public static List<Locale> availableLocaleList(){
        return SyncAvoid.AVAILABLE_LOCALE_LIST;
    }

    public static Set<Locale> availableLocaleSet(){
        return SyncAvoid.AVAILABLE_LOCALE_SET;
    }

    public static boolean isAvailableLocale(final Locale locale){
        return availableLocaleList().contains(locale);
    }
    public static List<Locale> languaesByCountry(final String countryCode){
        if (countryCode==null) {
            return Collections.emptyList();
        }
        List<Locale> langs=LanguagesByCountry.get(countryCode);
        if (langs==null) {
            langs=new ArrayList<>();
            final List<Locale> locales=availableLocaleList();
            for (final Locale locale : locales) {
                if (countryCode.equals(locale.getCountry()) && locale.getVariant().isEmpty()) {
                    langs.add(locale);
                }
            }
            langs=Collections.unmodifiableList(langs);
            LanguagesByCountry.putIfAbsent(countryCode,langs);
            langs=LanguagesByCountry.get(countryCode);
        }
        return langs;
    }
    public static List<Locale> countriesByLanguage(final String languageCode){
        if (languageCode==null) {
            return Collections.emptyList();
        }
        List<Locale> countries=CountriesByLanguage.get(languageCode);
        if (countries==null) {
            countries=new ArrayList<>();
            final List<Locale> locales=availableLocaleList();
            for (final Locale locale: locales) {
                if (languageCode.equals(locale.getLanguage())&&locale.getCountry().length()!=0&&locale.getVariant().isEmpty()) {
                    countries.add(locale);
                }
            }
            countries=Collections.unmodifiableList(countries);
            CountriesByLanguage.putIfAbsent(languageCode,countries);
            countries=CountriesByLanguage.get(languageCode);
        }
        return countries;
    }
    private static boolean isAllLowerCase(final CharSequence cs) {
        if (cs == null || isEmpty(cs)) {
            return false;
        }
        final int size = cs.length();
        for (int i = 0; i < size; i++) {
            if (!Character.isLowerCase(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    private static boolean isAllUpperCase(final CharSequence cs){
        if (cs==null||isEmpty(cs)) {
            return false;
        }
        final int size=cs.length();
        for (int i = 0; i <size ; i++) {
            if (!Character.isLowerCase(cs.charAt(i))){
                return false;
            }
        }
        return true;
    }
    private static boolean isEmpty(final CharSequence charSequence){
        return charSequence==null||charSequence.length()==0;
    }

    private static class SyncAvoid {
        private static final List<Locale> AVAILABLE_LOCALE_LIST;

        private static final Set<Locale> AVAILABLE_LOCALE_SET;
        static {
            final List<Locale> list=new ArrayList<>(Arrays.asList(Locale.getAvailableLocales()));  // extra safe
            AVAILABLE_LOCALE_LIST=Collections.unmodifiableList(list);
            AVAILABLE_LOCALE_SET=Collections.unmodifiableSet(new HashSet<>(list));
        }
    }
}
