package usjt.project.civitas.civitas.service;

import java.text.DecimalFormat;
import java.util.Comparator;
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
	private PessoaService pessoaService;
	
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
    
	public List<Postagem> getPosts(Long userID){
		try {
			Pessoa pessoa = pessoaService.getByID(userID);
			String pessoaLocalizacao = pessoa.getLocalizacao();
			String dist1 = pessoaLocalizacao.replace(" ", "");
			String[] dist1splited = dist1.split(",");
			
			List<Postagem> postagensDaCidade = postagemRepo.findByCidade(pessoa.getCidade());
			for (Postagem postagem : postagensDaCidade) {
				
				String dist2 = postagem.getLocalizacao().replace(" ", "");
				String[] dist2splited = dist2.split(",");
				postagem.setDistanciaDaPessoaLogada(calculaDistancia(Double.parseDouble(dist1splited[0]), Double.parseDouble(dist1splited[1]), 
																	Double.parseDouble(dist2splited[0]), Double.parseDouble(dist2splited[1])));
			}
			postagensDaCidade.sort(Comparator.comparing(Postagem::getDistanciaDaPessoaLogada));
			return postagensDaCidade;
		} catch (NotFoundPersonException | IDNullException e) {
			throw new RuntimeException(e);
		}
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
			throw new RuntimeException(e);
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
    
	private static String calculaDistancia(double lat1, double lon1, double lat2, double lon2) {
		  double theta = lon1 - lon2;
		  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		  dist = Math.acos(dist);
		  dist = rad2deg(dist);
		  dist = dist * 60 * 1.1515;
		  dist = dist * 1.609344;
		  return new DecimalFormat("#,##0.00").format(dist) + " Km";
	}
	
	private static double deg2rad(double deg) {
		  return (deg * Math.PI / 180.0);
	}
	
	private static double rad2deg(double rad) {
		  return (rad * 180.0 / Math.PI);
	}
}
