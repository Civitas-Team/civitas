package usjt.project.civitas.civitas.entity;

import javax.persistence.*;

@Entity
@Table(name= "postagem")
public class Postagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
}
