package org.doxa.apigateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
@Slf4j
public class GlobalErrorHandlerController {

    @Value("${spring.servlet.multipart.max-file-size}")
    private String MAX_UPLOAD_SIZE;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Upload file size should not exceed " + MAX_UPLOAD_SIZE);
        apiResponse.setStatus(HttpStatus.EXPECTATION_FAILED);
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(apiResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> serverError(Exception exc) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Service temporary unavailable. Try again later");
        log.debug("Exception {}" + exc.getMessage());
        exc.printStackTrace();
        apiResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(apiResponse);
    }
}
