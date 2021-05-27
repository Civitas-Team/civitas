package usjt.project.civitas.civitas.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class ConfirmacaoDeInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_postostagem")
	@JsonIgnoreProperties("confirmacaoDeInfo")
	private Postagem idPostagem;
	
	@ManyToOne
	@JoinColumn(name = "idPessoa")
	private Pessoa idPessoa;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Postagem getIdPostagem() {
		return idPostagem;
	}

	public void setIdPostagem(Postagem idPostagem) {
		this.idPostagem = idPostagem;
	}

	public Pessoa getIdPessoa() {
		return idPessoa;
	}

	public void setIdPessoa(Pessoa idPessoa) {
		this.idPessoa = idPessoa;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
