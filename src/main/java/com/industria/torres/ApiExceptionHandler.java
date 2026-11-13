package com.industria.torres;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String,String>> validacao(MethodArgumentNotValidException e){
    var f=e.getBindingResult().getFieldError();
    return ResponseEntity.badRequest().body(Map.of("mensagem","Dados inválidos"+(f==null?"":": "+f.getField())));
  }
  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Map<String,String>> regra(ResponseStatusException e){
    return ResponseEntity.status(e.getStatusCode()).body(Map.of("mensagem",String.valueOf(e.getReason())));
  }
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Map<String,String>> integridade(DataIntegrityViolationException e){
    return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensagem","Torre inexistente ou dados inconsistentes"));
  }
}
