package usjt.project.civitas.civitas.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.omg.CORBA.SetOverrideTypeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import usjt.project.civitas.civitas.entity.Imagem;
import usjt.project.civitas.civitas.entity.Login;
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
	public ResponseEntity<?> update(@RequestHeader String authorization, @RequestBody Pessoa pessoa, HttpServletRequest request) throws Exception {
		if(!pessoa.getToken().equals(authorization)) {
			throw new Exception("Token não pertence ao usuário a ser atualizado");
		}
		
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
	public RedirectView confirmarEmail(@RequestParam("user") Long id) {
		try {
			service.ConfirmarEmail(id);
		} catch(Exception e) {
			e.printStackTrace();
		}
		return new RedirectView("http://localhost:4200");
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Login login, HttpServletRequest request) {
		try {
			return ResponseEntity.ok(service.logar(login.getEmail(), login.getSenha()));
		} catch(Exception e) {
			return ResponseEntityHelper.createResponse(e, HttpStatus.FORBIDDEN, request);
		}
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader Long id, @RequestHeader String authorization, HttpServletRequest request) {
		try {
			return ResponseEntity.ok(service.excluirToken(id, authorization));
		} catch(Exception e) {
			return ResponseEntityHelper.createResponse(e, HttpStatus.FORBIDDEN, request);
		}
		
	}
	
	//TODO: apenas para testes, remover essa parada depois
	@GetMapping("/getAll")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok(service.getAll());
	}
	
	@PostMapping(value = "cadImagemPessoa", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> persistImage(HttpServletRequest request, @RequestParam("imagem") MultipartFile imagem) {
		try {

			byte[] imagemBytesEncoded = Base64.encodeBase64(imagem.getBytes());

			Imagem retorno = new Imagem();
			retorno.setImagemBase64(new String(imagemBytesEncoded));

			return (ResponseEntity<?>) ResponseEntity.ok(retorno);
		} catch (Exception e) {
			return ResponseEntityHelper.createResponse(e, HttpStatus.BAD_REQUEST, request);
		}
	}
}
