package usjt.project.civitas.civitas.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import usjt.project.civitas.civitas.entity.ConfirmacaoDeInfo;
import usjt.project.civitas.civitas.entity.Pessoa;
import usjt.project.civitas.civitas.entity.Postagem;
import usjt.project.civitas.civitas.repository.ConfirmacaoDeInfoRepository;
import usjt.project.civitas.civitas.validation.NotFoundPostException;

@Service
public class ConfirmacaoDeInfoService {
	
	@Autowired
	private PostagemService postagemService;
	
	@Autowired
	private ConfirmacaoDeInfoRepository confirmacaoDeInfoRepository;
	
	public void incluirConfirmacaoDeInfo(Pessoa pessoa, Postagem postagem) throws NotFoundPostException {
		
		ConfirmacaoDeInfo confirmacaoDeInfo = new ConfirmacaoDeInfo();
		Postagem postagemBD = postagemService.getById(postagem.getId());
		if(postagemBD != null) {
			confirmacaoDeInfo.setIdPessoa(pessoa);
			confirmacaoDeInfo.setIdPostagem(postagem);
			confirmacaoDeInfoRepository.save(confirmacaoDeInfo);
		} else {
			throw new NotFoundPostException();
		}
	}
	
	public boolean obterConfirmacaoDeInfo(Postagem postagem, Pessoa pessoa) {
		ConfirmacaoDeInfo confirmacaoDeInfo = confirmacaoDeInfoRepository.findByConfirmacaoDeInfo(postagem, pessoa); 
		if (confirmacaoDeInfo != null) {
			confirmacaoDeInfoRepository.deleteById(confirmacaoDeInfo.getId());
			return false;
		}
		return true;
	}
}
