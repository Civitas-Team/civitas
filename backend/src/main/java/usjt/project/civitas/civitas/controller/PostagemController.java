package usjt.project.civitas.civitas.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import usjt.project.civitas.civitas.entity.Pessoa;
import usjt.project.civitas.civitas.entity.Postagem;
import usjt.project.civitas.civitas.entity.SearchResult;
import usjt.project.civitas.civitas.helper.ConstantsHelper;
import usjt.project.civitas.civitas.helper.PaginationHelper;
import usjt.project.civitas.civitas.helper.ResponseEntityHelper;
import usjt.project.civitas.civitas.service.PessoaService;
import usjt.project.civitas.civitas.service.PostagemService;

@RestController
@RequestMapping("/postagem")
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
	
    @GetMapping("/getPosts")
    public SearchResult getTimeline(HttpServletRequest request, 
		@RequestParam(required = false, value = "currentPage") String currentPageParam,
		@RequestParam(required = false, value = "itensPerPage") String itensPerPageParam,
		@RequestHeader String userID) {
		
		List<Postagem> posts = postagemService.getPosts(userID);
        
        int totalOfResults = posts.size();
		
		int itensPerPage = Integer.parseInt(itensPerPageParam);
	
		int totalPages = 1;
		
		if (totalOfResults > itensPerPage) {
			totalPages = ( totalOfResults + (itensPerPage - 1) ) / itensPerPage;
		}

		int currentPage = 1;
		if (currentPageParam != null) {
			currentPage = Integer.parseInt(currentPageParam);
		}
		
		posts = PaginationHelper.getPage(posts, currentPage, itensPerPage);
		
		SearchResult searchResult = new SearchResult(posts, totalPages, totalOfResults, currentPage);
		
		return searchResult;
    }
    
	//TODO: apenas para testes, remover essa parada depois
	@GetMapping("/getAll")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok(postagemService.getAll());
	}
}
