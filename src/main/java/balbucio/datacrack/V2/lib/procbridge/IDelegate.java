package balbucio.datacrack.V2.lib.procbridge;

import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface IDelegate {
    /**
     * An interface that defines how server handles requests.
     *
     * @param method the requested method
     * @param payload the requested payload, must be a JSON value
     * @return the result, must be a JSON value
     */
    @Nullable
    Object handleRequest(@Nullable String method, @Nullable Object payload);
}
