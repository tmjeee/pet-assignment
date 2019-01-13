package tmjee.pet.commons;

import java.util.Collections;
import java.util.List;

public class EndPointResponse<T> {
    public final boolean ok;
    public final List<String> messages;
    public final T payload;

    public EndPointResponse(boolean ok, List<String> messages, T payload) {
        this.ok = ok;
        this.messages = Collections.unmodifiableList(messages);
        this.payload = payload;
    }
}

