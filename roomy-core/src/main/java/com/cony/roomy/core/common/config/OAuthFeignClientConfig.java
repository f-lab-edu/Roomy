package com.cony.roomy.core.common.config;

import com.cony.roomy.core.common.exception.ErrorType;
import com.cony.roomy.core.common.exception.RoomyException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Slf4j
@Configuration
public class OAuthFeignClientConfig {

    //TODO 헤더를 처리하는 빈

    @Bean
    public ErrorDecoder errorDecoder() {
        return this::decode;
    }

    private Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.resolve(response.status());
        if (status == null) {
            return new RoomyException(ErrorType.UNKNOWN_ERROR, Map.of("response", response), log::error);
        }

        return switch (status) {
            case UNAUTHORIZED ->
                    new RoomyException(ErrorType.OAUTH_UNAUTHORIZED, Map.of("response", response), log::error);
            case BAD_REQUEST ->
                    new RoomyException(ErrorType.OAUTH_BAD_REQUEST, Map.of("response", response), log::error);
            case NOT_FOUND -> new RoomyException(ErrorType.OAUTH_NOT_FOUND, Map.of("response", response), log::error);
            default ->
                    new RoomyException(ErrorType.OAUTH_INTERNAL_SERVER_ERROR, Map.of("response", response), log::error);
        };
    }
}
