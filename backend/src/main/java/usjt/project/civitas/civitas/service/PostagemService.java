package usjt.project.civitas.civitas.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import usjt.project.civitas.civitas.entity.Pessoa;
import usjt.project.civitas.civitas.entity.Postagem;
import usjt.project.civitas.civitas.repository.PostagemRepository;
import usjt.project.civitas.civitas.validation.IDNullException;
import usjt.project.civitas.civitas.validation.InvalidTokenException;
import usjt.project.civitas.civitas.validation.NotFoundPersonException;
import usjt.project.civitas.civitas.validation.NotFoundPostException;

@Service
public class PostagemService {
	
	@Autowired
	private TemaService temaService;
	
	@Autowired
	private PostagemRepository postagemRepo;
	
	@Autowired
	private PessoaService pessoaService;
	
    public Postagem save(Postagem postagem) throws IllegalArgumentException, NotFoundPersonException {
        if (postagem == null) {
            throw new IllegalArgumentException("Objeto Post não é um objeto valido, pois tem seu valor nulo.");
        }
        if (postagem.getPessoa() == null || postagem.getPessoa().getId() == null) {
            throw new NotFoundPersonException();
        }
        postagem.setData(new Date());
        postagem.setTema(temaService.getById(postagem.getTemaId()));
        return postagemRepo.save(postagem);
    }
    
    //TODO: criar lógica de postagens próximas aqui
	public List<Postagem> getPosts(String userID){
		return postagemRepo.findAll();
	}
	
    public void delete(Long postId, Long userId) throws NotFoundPostException, NotFoundPersonException, InvalidTokenException {
        if (postId == null || !postagemRepo.existsById(postId)) {
            throw new NotFoundPostException();
        }
        try {
	        Pessoa pessoa;
			pessoa = pessoaService.getByID(userId);
	        Postagem postagem = getById(postId);
	        if (pessoa != null && pessoa.equals(postagem.getPessoa())) {
	            postagemRepo.deleteById(postId);
	        } else {
	            throw new InvalidTokenException();
	        }
		} catch (NotFoundPersonException | IDNullException e) {
			e.printStackTrace();
		}
    }
    
    public Postagem getById(Long id) throws NotFoundPostException {
        if (id != null && postagemRepo.existsById(id)) {
            try {
                return postagemRepo.findById(id).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        throw new NotFoundPostException();
    }
}
