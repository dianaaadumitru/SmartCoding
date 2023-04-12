package com.example.backend.utils;

public class WebSocketUtility {

    private static final String GENERAL_PAYLOAD = "{\n" +
            "  \"buffers\": [],\n" +
            "  \"channel\": \"shell\",\n" +
            "  \"content\": {\n" +
            "    \"silent\": false,\n" +
            "    \"store_history\": true,\n" +
            "    \"user_expressions\": {},\n" +
            "    \"allow_stdin\": true,\n" +
            "    \"stop_on_error\": true,\n" +
            "    \"code\": \"%s\"\n" +
            "  },\n" +
            "  \"header\": {\n" +
            "    \"date\": \"\",\n" +
            "    \"msg_id\": \"\",\n" +
            "    \"msg_type\": \"execute_request\",\n" +
            "    \"username\": \"\",\n" +
            "    \"version\": \"5.2\"\n" +
            "  },\n" +
            "  \"metadata\": {\n" +
            "    \"deletedCells\": [],\n" +
            "    \"recordTiming\": false\n" +
            "  },\n" +
            "  \"parent_header\": {}\n" +
            "}";

    public static String getRequestMessagePayload(String code) {
        return String.format(GENERAL_PAYLOAD, code);
    }
}