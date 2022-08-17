package com.example.demo2.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();
    private static final Logger logger = LoggerFactory.getLogger(CustomErrorDecoder.class);

    @Override
    public Exception decode(String invoqueur, Response reponse) {
        if(reponse.status() == 403 ) {
            String msg = "Access to this resource requires authorities that couldn't be verified";
            logger.error(msg);
            return new ForbiddenException(
                    msg
            );
        }
        return defaultErrorDecoder.decode(invoqueur, reponse);
    }
}
