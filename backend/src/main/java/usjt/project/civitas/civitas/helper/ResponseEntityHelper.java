package usjt.project.civitas.civitas.helper;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import usjt.project.civitas.civitas.controller.ApiMessage;

public class ResponseEntityHelper {

    public static ResponseEntity<?> createResponse(Exception e, HttpStatus status, HttpServletRequest request) {
        ApiMessage message = ApiMessage.buildMessage(e.getMessage(), request);
        return ResponseEntity.status(status).body(message);
    }
}
