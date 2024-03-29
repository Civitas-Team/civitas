package usjt.project.civitas.civitas.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import usjt.project.civitas.civitas.entity.ConfirmacaoDeInfo;
import usjt.project.civitas.civitas.entity.Imagem;
import usjt.project.civitas.civitas.entity.Pessoa;
import usjt.project.civitas.civitas.entity.Postagem;
import usjt.project.civitas.civitas.entity.SearchResult;
import usjt.project.civitas.civitas.helper.ConstantsHelper;
import usjt.project.civitas.civitas.helper.PaginationHelper;
import usjt.project.civitas.civitas.helper.ResponseEntityHelper;
import usjt.project.civitas.civitas.service.ConfirmacaoDeInfoService;
import usjt.project.civitas.civitas.service.PessoaService;
import usjt.project.civitas.civitas.service.PostagemService;
import usjt.project.civitas.civitas.validation.EntityIDValidation;
import usjt.project.civitas.civitas.validation.InvalidTokenException;
import usjt.project.civitas.civitas.validation.NotFoundPersonException;
import usjt.project.civitas.civitas.validation.NotFoundPostException;

@RestController
@RequestMapping("/postagem")
public class PostagemController {

	@Autowired
	private PessoaService pessoaService;

	@Autowired
	private PostagemService postagemService;
	
	@Autowired
	private ConfirmacaoDeInfoService confirmacaoDeInfoService;

	@PostMapping(consumes = ConstantsHelper.APPLICATION_JSON, produces = ConstantsHelper.APPLICATION_JSON)
	public ResponseEntity<?> create(@RequestHeader String authorization, @RequestBody Postagem postagem,
			HttpServletRequest request) {
		try {
			Pessoa pessoa = pessoaService.getByToken(authorization);
			postagem.setPessoa(pessoa);

			return ResponseEntity.ok(postagemService.save(postagem));
		} catch (Exception e) {
			return ResponseEntityHelper.createResponse(e, HttpStatus.BAD_REQUEST, request);
		}
	}

	@GetMapping("/getPosts")
	public ResponseEntity<?> getTimeline(HttpServletRequest request,
			@RequestParam(required = false, value = "currentPage") String currentPageParam,
			@RequestParam(required = false, value = "itensPerPage") String itensPerPageParam,
			@RequestHeader String authorization) {
		Pessoa pessoaLogada;
		try {
			pessoaLogada = pessoaService.getByToken(authorization);
		} catch (Exception e) {
			return ResponseEntityHelper.createResponse(e, HttpStatus.FORBIDDEN, request);
		}

		List<Postagem> posts = postagemService.getPosts(pessoaLogada.getId());

		int totalOfResults = posts.size();
		int itensPerPage = Integer.parseInt(itensPerPageParam);
		int totalPages = 1;

		if (totalOfResults > itensPerPage) {
			totalPages = (totalOfResults + (itensPerPage - 1)) / itensPerPage;
		}

		int currentPage = 1;
		if (currentPageParam != null) {
			currentPage = Integer.parseInt(currentPageParam);
		}

		posts = PaginationHelper.getPage(posts, currentPage, itensPerPage);
		
		for(Postagem post: posts) {
			for(ConfirmacaoDeInfo confirmacaoDeInfo : post.getConfirmacaoDeInfo()) {
				if(confirmacaoDeInfo.getIdPessoa().getId() == pessoaLogada.getId()) post.setConfirmadaPeloUsuarioLogado(true);
			}
		}

		SearchResult searchResult = new SearchResult(posts, totalPages, totalOfResults, currentPage);
		
		return ResponseEntity.ok(searchResult);
	}

	@DeleteMapping(value = "/{id}", produces = ConstantsHelper.APPLICATION_JSON)
	public ResponseEntity<?> delete(@RequestHeader String authorization, @PathVariable("id") String idParam,
			HttpServletRequest request) {
		try {
			EntityIDValidation.validateID(idParam);
			Pessoa pessoa = pessoaService.getByToken(authorization);
			Long postId = Long.parseLong(idParam);
			try {
				postagemService.delete(postId, pessoa.getId());
			} catch (NotFoundPostException | NotFoundPersonException | InvalidTokenException e) {
				if (e.getClass() == InvalidTokenException.class) {
					return ResponseEntityHelper.createResponse(e, HttpStatus.FORBIDDEN, request);
				}
				return ResponseEntityHelper.createResponse(e, HttpStatus.NOT_FOUND, request);
			}
			final String MSG_DELETE_OK = "Post excluído com sucesso.";
			ApiMessage message = ApiMessage.buildMessage(MSG_DELETE_OK, request);
			return ResponseEntity.ok(message);
		} catch (Exception e) {
			return ResponseEntityHelper.createResponse(e, HttpStatus.BAD_REQUEST, request);
		}

	}

	@PostMapping(value = "cadImagemPostagem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
	
	@PostMapping("confirmarInfo/{id}")
	public ResponseEntity<?> postLike(@PathVariable("id") Long id, @RequestHeader String authorization, HttpServletRequest request) {
		
		Postagem postagem = new Postagem();
		postagem.setId(id);
		try {
		Pessoa pessoa = pessoaService.getByToken(authorization);
			if(confirmacaoDeInfoService.obterConfirmacaoDeInfo(postagem, pessoa)) {
				try {
					confirmacaoDeInfoService.incluirConfirmacaoDeInfo(pessoa, postagem);;
					return ResponseEntity.ok("Confirmação incluída com sucesso");
				} catch (NotFoundPostException e) {
					return ResponseEntityHelper.createResponse(e, HttpStatus.NOT_FOUND, request);
				}
			}
		 } catch (Exception e) {
            return ResponseEntityHelper.createResponse(e, HttpStatus.BAD_REQUEST, request);
		 }
		return ResponseEntity.ok("Confirmação retirada com sucesso");
	}
}
