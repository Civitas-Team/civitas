package usjt.project.civitas.civitas.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import usjt.project.civitas.civitas.entity.Postagem;
import usjt.project.civitas.civitas.entity.Tema;
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
        if(postagem.getTema() != null && postagem.getTema().getId() != null) {
        	postagem.setTema(temaService.getById(postagem.getTema().getId()));
        }
        return postagemRepo.save(postagem);
    }
    
	public List<Postagem> getAll(){
		return postagemRepo.findAll();
	}
}
