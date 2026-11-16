package committee.nova.mkw.core.binding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class BindingSearchQuery {
    private final boolean keyFilter;
    private final String keyNameFilter;
    private final List<String> textTerms;
    private final List<String> normalizedTerms;

    BindingSearchQuery(boolean keyFilter, String keyNameFilter, List<String> textTerms) {
        this.keyFilter = keyFilter;
        this.keyNameFilter = keyNameFilter;
        this.textTerms = Collections.unmodifiableList(new ArrayList<>(textTerms));
        this.normalizedTerms = new ArrayList<>(textTerms.size());
        for (String term : textTerms) {
            this.normalizedTerms.add(term.toLowerCase());
        }
    }

    public boolean keyFilter() {
        return keyFilter;
    }

    public String keyNameFilter() {
        return keyNameFilter;
    }

    public List<String> textTerms() {
        return textTerms;
    }

    public boolean hasKeyNameFilter() {
        return keyNameFilter != null;
    }

    public boolean hasTextTerms() {
        return !textTerms.isEmpty();
    }

    public boolean matchesName(String localizedName) {
        String normalizedName = localizedName.toLowerCase();
        for (String term : normalizedTerms) {
            if (!normalizedName.contains(term)) {
                return false;
            }
        }
        return true;
    }
}
