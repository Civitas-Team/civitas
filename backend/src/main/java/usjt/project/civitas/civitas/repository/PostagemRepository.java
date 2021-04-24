package usjt.project.civitas.civitas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import usjt.project.civitas.civitas.entity.Postagem;

@Repository
public interface PostagemRepository extends JpaRepository<Postagem, Long> {}
