package hzt;

import java.util.Locale;
import java.util.ResourceBundle;

import static java.lang.System.*;

public class LocaleSample {

    public static final String APPMESSAGES = "appmessages";
    public static final String GREETINGS = "greetings";

    public static void main(String... args) {
        // if no properties file for Canada French exists, it will fallback to appmessages.proprties without any extensions
        Locale.setDefault(Locale.CANADA_FRENCH);
        Locale customJapanLocale = new Locale("jp", "JP");
        out.println(ResourceBundle.getBundle(APPMESSAGES, customJapanLocale).getString(GREETINGS));
        out.println(ResourceBundle.getBundle(APPMESSAGES, Locale.FRANCE).getString(GREETINGS));
        out.println(ResourceBundle.getBundle(APPMESSAGES, Locale.GERMAN).getString(GREETINGS));
        out.println(ResourceBundle.getBundle(APPMESSAGES, Locale.CANADA_FRENCH).getString(GREETINGS));
    }
}
