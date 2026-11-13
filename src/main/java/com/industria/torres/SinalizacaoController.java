package com.industria.torres;
import com.industria.torres.Dtos.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api")
public class SinalizacaoController {
  private final SinalizacaoService service;
  public SinalizacaoController(SinalizacaoService s){ service=s; }
  @GetMapping("/estado") public Estado estado(){ return service.estado(); }
  @PostMapping("/sinalizacoes") @ResponseStatus(HttpStatus.CREATED)
  public void registrar(@Valid @RequestBody NovaSinalizacao n){ service.registrar(n); }
  @PostMapping("/sinalizacoes/{id}/atendimentos") @ResponseStatus(HttpStatus.CREATED)
  public void atender(@PathVariable Long id,@Valid @RequestBody NovoAtendimento a){ service.atender(id,a); }
}
