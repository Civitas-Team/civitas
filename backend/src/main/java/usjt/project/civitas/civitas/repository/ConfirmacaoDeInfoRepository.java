package usjt.project.civitas.civitas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import usjt.project.civitas.civitas.entity.ConfirmacaoDeInfo;
import usjt.project.civitas.civitas.entity.Pessoa;
import usjt.project.civitas.civitas.entity.Postagem;

@Repository
public interface ConfirmacaoDeInfoRepository extends JpaRepository<ConfirmacaoDeInfo, Long>{
	
	@Query("Select c from ConfirmacaoDeInfo c where c.idPostagem = :idPostagem and c.idPessoa = :idPessoa")
	public ConfirmacaoDeInfo findByConfirmacaoDeInfo(@Param("idPostagem") Postagem idPostagem, @Param("idPessoa") Pessoa idPessoa);
}
