package usjt.project.civitas.civitas.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import usjt.project.civitas.civitas.entity.Pessoa;
import usjt.project.civitas.civitas.helper.ResponseEntityHelper;
import usjt.project.civitas.civitas.service.PessoaService;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {
	
	@Autowired
	private PessoaService service;
	
	@PostMapping("/cadastroUsuario")
	public ResponseEntity<?> insert(@RequestBody Pessoa person, HttpServletRequest request) {
		try {
			
			String insert = service.inserir(person);
	
			if (insert == "ok") {
				ApiMessage message = ApiMessage.buildMessage("Cadastro realizado com sucesso, verifique seu email para prosseguir!", request);
				System.out.println(person.getToken());
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
			} else {
				ApiMessage message = ApiMessage.buildMessage(insert + " já cadastrado!", request);
				return ResponseEntity.status(HttpStatus.ALREADY_REPORTED).body(message);
			}
			
		} catch(Exception e) {
			return ResponseEntityHelper.createResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
		}
	}
	
	@PostMapping("/update")
	public ResponseEntity<?> update(@RequestBody Pessoa pessoa, HttpServletRequest request) {
		try {
			Pessoa pessoaAtualizada = service.update(pessoa);
			
			if(pessoaAtualizada != null) {
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(pessoaAtualizada);
			} else {
				throw new Exception("Ocorreu algum erro durante a atualização do usuário");
			}
			
		} catch(Exception e) {
			return ResponseEntityHelper.createResponse(e, HttpStatus.FORBIDDEN, request);
		}
	}
	
	@PostMapping("/reenviarEmail")
	public ResponseEntity<?> reenviarEmail(@RequestHeader Long id, HttpServletRequest request) {
		try {
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(service.reenviarEmail(id));
		} catch(Exception e) {
			return ResponseEntityHelper.createResponse(e, HttpStatus.FORBIDDEN, request);
		}
	}
	
	@GetMapping("/confirmarEmail")
	public void confirmarEmail(@RequestParam("user") Long id) {
		try {
			service.ConfirmarEmail(id);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestHeader String email, @RequestHeader String senha, HttpServletRequest request) {
		try {
			return ResponseEntity.ok(service.logar(email, senha));
		} catch(Exception e) {
			return ResponseEntityHelper.createResponse(e, HttpStatus.FORBIDDEN, request);
		}
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader Long id, @RequestHeader String token, HttpServletRequest request) {
		try {
			return ResponseEntity.ok(service.excluirToken(id, token));
		} catch(Exception e) {
			return ResponseEntityHelper.createResponse(e, HttpStatus.FORBIDDEN, request);
		}
		
	}
	
	//TODO: apenas para testes, remover essa parada depois
	@GetMapping("/getAll")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok(service.getAll());
	}
}
