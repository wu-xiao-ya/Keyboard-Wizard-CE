package committee.nova.mkw.bridge.binding;

public interface BindingMutationBridge<B, K, M> {
    void setModifierAndKey(B binding, M modifier, K key);

    void resetToDefault(B binding);

    void refreshMappings();
}
