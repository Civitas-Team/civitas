package usjt.project.civitas.civitas.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import usjt.project.civitas.civitas.entity.Tema;
import usjt.project.civitas.civitas.repository.TemaRepository;

@Service
public class TemaService {
	
	@Autowired
	private TemaRepository repo;
	
	public boolean setTemaList(List<String> temas) {
		for(String tema : temas) {
			repo.save(new Tema(tema));
		}
		return true;
	}
	
	public List<Tema> getAll(){
		return repo.findAll();
	}

	public Tema getById(Long id) {
		return repo.findById(id).get();
	}

	public List<Long> validarIds(String[] temas) {
		if (temas != null) {
			List<Long> ids = new ArrayList<>();
			for (String tema: temas) {
				Integer number = null;
				try {
					number = Integer.parseInt(tema);
					ids.add(number.longValue());
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			if (ids.size() > 0){
				return ids;
			}
		}
		return null;
	}
}
