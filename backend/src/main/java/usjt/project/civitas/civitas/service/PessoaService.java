package usjt.project.civitas.civitas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import usjt.project.civitas.civitas.entity.Pessoa;
import usjt.project.civitas.civitas.entity.exception.IDNullException;
import usjt.project.civitas.civitas.entity.exception.NotFoundPersonException;
import usjt.project.civitas.civitas.repository.PessoaRepository;


// TODO: encryptar a senha, fazer o envio de email e mudar o paradigma para orientação à exception

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository repo;
	
	public String inserir(Pessoa pessoa) {
		if (repo.findByCpf(pessoa.getCpf()) != null) {
			return "CPF";
		} else if (repo.findByEmail(pessoa.getEmail()) != null) {
			return "Email";
		} else {
			repo.save(pessoa);
			return "ok";
		}
	}
	
	public Pessoa getByID(Long id) throws NotFoundPersonException, IDNullException {
		if (id != null) {
			Optional<Pessoa> pessoa = repo.findById(id);
			if (pessoa == null || !pessoa.isPresent()) {
				throw new NotFoundPersonException();
			}
			return pessoa.get();
		} else {
			throw new IDNullException();
		}
	}
	
	//TODO: apenas para testes, remover essa parada depois
	public List<Pessoa> getAll(){
		return repo.findAll();
	}
}
