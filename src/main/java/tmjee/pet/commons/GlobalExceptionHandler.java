package tmjee.pet.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<EndPointResponse<Void>> handleException(Exception e) {
        log.error(e.toString(), e);
        return new ResponseEntity<>(
                new EndPointResponse<Void>(
                        false,
                        List.of(e.toString()),
                        null),
                HttpStatus.OK);
    }
}
