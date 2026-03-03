package newpixclient.messages.operation;

import com.fasterxml.jackson.databind.JsonNode;
import newpixclient.type.Token;

import java.util.Map;

public interface Operation {
    Object send(Map<String, Object> itemMap, Token token) throws Exception;

    Object receive(JsonNode payload) throws Exception;
}
