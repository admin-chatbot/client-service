package com.voicebot.commondcenter.clientservice.utils;

import com.voicebot.commondcenter.clientservice.dto.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    public static ResponseEntity<?>  build500(Exception exception) {
        return ResponseEntity.internalServerError().body( ResponseBody.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(exception.getMessage())
                .exception(exception)
                .build());
    }

    public static ResponseEntity<?>  build400(String message) {
        return ResponseEntity.badRequest().body( ResponseBody.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .build());
    }

    public static <I> ResponseEntity<?> ok(String message, I i) {
        return ResponseEntity.ok(ResponseBody.builder()
                .message(message)
                .code(HttpStatus.OK.value())
                .data(i)
                .build());
    }



    public static ResponseEntity<?>  build404(String message) {
        return  ResponseEntity.notFound().build() ;
    }
}
