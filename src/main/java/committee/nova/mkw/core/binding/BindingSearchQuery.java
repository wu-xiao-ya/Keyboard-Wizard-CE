package committee.nova.mkw.core.binding;

import java.util.List;

public record BindingSearchQuery(boolean keyFilter, String keyNameFilter, List<String> textTerms) {
    public boolean hasKeyNameFilter() {
        return keyNameFilter != null && !keyNameFilter.isEmpty();
    }

    public boolean hasTextTerms() {
        return !textTerms.isEmpty();
    }
}
