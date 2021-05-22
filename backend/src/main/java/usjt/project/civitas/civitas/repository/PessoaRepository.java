package usjt.project.civitas.civitas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import usjt.project.civitas.civitas.entity.Pessoa;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
	public Pessoa findByEmail(String email);
	public Pessoa findByCpf(String cpf);
	public Pessoa findByToken(String token);
}
