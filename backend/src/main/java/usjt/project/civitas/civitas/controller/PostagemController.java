package usjt.project.civitas.civitas.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import usjt.project.civitas.civitas.entity.Pessoa;
import usjt.project.civitas.civitas.entity.Postagem;
import usjt.project.civitas.civitas.helper.ConstantsHelper;
import usjt.project.civitas.civitas.helper.ResponseEntityHelper;
import usjt.project.civitas.civitas.service.PessoaService;
import usjt.project.civitas.civitas.service.PostagemService;

@RestController
//todo definir path
public class PostagemController {
	
	@Autowired
	private PessoaService pessoaService;
	
	@Autowired
	private PostagemService postagemService;
	
    @PostMapping(consumes = ConstantsHelper.APPLICATION_JSON, produces = ConstantsHelper.APPLICATION_JSON)
    public ResponseEntity<?> create(@RequestHeader Long userID, @RequestBody Postagem postagem,
            HttpServletRequest request) {
        try {
            Pessoa pessoa = pessoaService.getByID(userID);
            postagem.setPessoa(pessoa);
            return ResponseEntity.ok(postagemService.save(postagem));
        } catch (Exception e) {
            return ResponseEntityHelper.createResponse(e, HttpStatus.BAD_REQUEST, request);
        }
    }
    
}
