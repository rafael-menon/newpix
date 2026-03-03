package newpixserver.messages.operation;

import com.fasterxml.jackson.databind.JsonNode;

public interface Operation {
    Object execute(JsonNode payload) throws Exception;
}
