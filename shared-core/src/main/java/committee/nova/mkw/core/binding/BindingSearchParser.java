package committee.nova.mkw.core.binding;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class BindingSearchParser {
    private static final Pattern KEY_NAME_PATTERN = Pattern.compile("<.*?>");
    private static final Pattern LEGACY_KEY_NAME_PATTERN = Pattern.compile("<.*>");

    private BindingSearchParser() {
    }

    public static BindingSearchQuery parse(String rawFilterText, String keyFilterPrefix) {
        return parse(rawFilterText, keyFilterPrefix, KEY_NAME_PATTERN);
    }

    private static BindingSearchQuery parse(String rawFilterText, String keyFilterPrefix, Pattern keyNamePattern) {
        String filterText = rawFilterText == null ? "" : rawFilterText;
        boolean keyFilter = filterText.startsWith(keyFilterPrefix);
        if (keyFilter) {
            filterText = filterText.substring(keyFilterPrefix.length());
        }

        String keyNameFilter = null;
        Matcher keyNameMatcher = keyNamePattern.matcher(filterText);
        if (keyNameMatcher.find()) {
            String keyNameWithBrackets = keyNameMatcher.group();
            keyNameFilter = keyNameWithBrackets.substring(1, keyNameWithBrackets.length() - 1);
            filterText = filterText.replace(keyNameWithBrackets, "");
        }

        return new BindingSearchQuery(keyFilter, keyNameFilter, textTerms(filterText));
    }

    public static BindingSearchQuery parseLegacy(String rawFilterText, String keyFilterPrefix) {
        String filterText = rawFilterText == null ? "" : rawFilterText;
        if (filterText.startsWith(keyFilterPrefix)) {
            return parse(filterText, keyFilterPrefix, LEGACY_KEY_NAME_PATTERN);
        }
        if (filterText.startsWith("@")) {
            String[] parts = filterText.split("\\s+", 2);
            String keyName = parts[0].substring(1);
            String bindingName = parts.length == 2 ? parts[1] : "";
            return new BindingSearchQuery(false, keyName, textTerms(bindingName));
        }
        return new BindingSearchQuery(false, null, textTerms(filterText));
    }

    private static List<String> textTerms(String filterText) {
        return Arrays.stream(filterText.split("\\s+"))
                .map(String::trim)
                .filter(term -> !term.isEmpty())
                .collect(Collectors.toList());
    }
}
