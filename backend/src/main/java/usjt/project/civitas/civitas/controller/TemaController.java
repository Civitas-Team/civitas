package usjt.project.civitas.civitas.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import usjt.project.civitas.civitas.service.TemaService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/tema")
public class TemaController {
	
	@Autowired
	TemaService service;
	
	private ApiMessage message;
	
	@PostMapping
	public ResponseEntity<?> insert(@RequestBody List<String> themes, HttpServletRequest request){
		
		if(service.setTemaList(themes)) {
			return ResponseEntity.ok().build();
		}else {
			message = ApiMessage.buildMessage("Problema ao executar a operação!", request);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
		}
	}
	
	@GetMapping("/getAll")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok(service.getAll());
	}
}
