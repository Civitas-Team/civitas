package usjt.project.civitas.civitas.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import usjt.project.civitas.civitas.entity.Postagem;
import usjt.project.civitas.civitas.entity.exception.NotFoundPersonException;
import usjt.project.civitas.civitas.repository.PostagemRepository;

@Service
public class PostagemService {
	
	@Autowired
	private TemaService temaService;
	
	@Autowired
	private PostagemRepository postagemRepo;
	
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
    
    //TODO: apenas para testes, remover essa parada depois
	public List<Postagem> getAll(){
		return postagemRepo.findAll();
	}
}
