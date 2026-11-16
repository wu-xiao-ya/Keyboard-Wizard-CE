package committee.nova.mkw.bridge.binding;

import java.util.List;
import java.util.Map;

public interface BindingAccessBridge<B, K, M> {
    List<B> getAllBindings();

    List<String> getCategories();

    Map<K, Integer> getBindingCountsByKey();

    K getKey(B binding);

    M getModifier(B binding);

    boolean isUnbound(B binding);
}
