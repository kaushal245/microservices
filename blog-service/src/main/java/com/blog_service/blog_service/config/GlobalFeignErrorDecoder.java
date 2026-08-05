package com.blog_service.blog_service.config;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.stereotype.Component;

import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import tools.jackson.databind.ObjectMapper;

@Component
public class GlobalFeignErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {

        try {

            if (response.body() != null) {

                String body = Util.toString(
                        response.body().asReader(StandardCharsets.UTF_8));

                Map<String, Object> errorMap =
                        objectMapper.readValue(body, Map.class);

                String message =
                        errorMap.getOrDefault(
                                "message",
                                "Something went wrong")
                                .toString();

                switch (response.status()) {

                    case 400:
                        return new BadRequestException(message);

                    

                    default:
                        return new RuntimeException(message);
                }
            }

        } catch (Exception e) {
            return defaultDecoder.decode(methodKey, response);
        }

        return defaultDecoder.decode(methodKey, response);
    }
}