package usjt.project.civitas.civitas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import usjt.project.civitas.civitas.entity.Tema;

@Repository
public interface TemaRepository extends JpaRepository<Tema, Long> {}