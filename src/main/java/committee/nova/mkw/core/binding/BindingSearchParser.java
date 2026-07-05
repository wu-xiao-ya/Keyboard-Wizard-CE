package committee.nova.mkw.core.binding;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BindingSearchParser {
    private static final Pattern KEY_NAME_PATTERN = Pattern.compile("<.*?>");

    private BindingSearchParser() {
    }

    public static BindingSearchQuery parse(String rawFilterText, String keyFilterPrefix) {
        String filterText = rawFilterText == null ? "" : rawFilterText;
        boolean keyFilter = filterText.startsWith(keyFilterPrefix);
        if (keyFilter) {
            filterText = filterText.substring(keyFilterPrefix.length());
        }

        String keyNameFilter = null;
        Matcher keyNameMatcher = KEY_NAME_PATTERN.matcher(filterText);
        if (keyNameMatcher.find()) {
            String keyNameWithBrackets = keyNameMatcher.group();
            keyNameFilter = keyNameWithBrackets.substring(1, keyNameWithBrackets.length() - 1);
            filterText = filterText.replace(keyNameWithBrackets, "");
        }

        List<String> textTerms = Arrays.stream(filterText.split("\\s+"))
                .map(String::trim)
                .filter(term -> !term.isEmpty())
                .toList();
        return new BindingSearchQuery(keyFilter, keyNameFilter, textTerms);
    }
}
