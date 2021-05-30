package usjt.project.civitas.civitas.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name= "postagem")
public class Postagem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date data;
	@Column
	private String corpo;
	@Lob
	@Column( length = 100000 )
	private String imagem;
	@Column
	private String localizacao;
	@Column
	private String cidade;
    @ManyToOne(fetch = FetchType.EAGER)
    private Pessoa pessoa;
	@OneToOne(/*fetch = FetchType.EAGER*/)
    @JoinColumn(referencedColumnName = "id")
	private Tema tema;
	@Transient
	private Long temaId;
	@Transient
	private String distanciaDaPessoaLogada;
	@Transient
	private boolean confirmadaPeloUsuarioLogado;
    @OneToMany(mappedBy = "idPostagem")
    @JsonIgnoreProperties("idPostagem")
    private Set<ConfirmacaoDeInfo> confirmacaoDeInfo;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDate() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getCorpo() {
		return corpo;
	}
	public void setCorpo(String corpo) {
		this.corpo = corpo;
	}
	public Date getData() {
		return data;
	}
	public String getLocalizacao() {
		return localizacao;
	}
	public void setLocalizacao(String localizacao) {
		this.localizacao = localizacao;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Pessoa getPessoa() {
		return pessoa;
	}
	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
	public Tema getTema() {
		return tema;
	}
	public void setTema(Tema tema) {
		this.tema = tema;
	}
	public Long getTemaId() {
		return temaId;
	}
	public void setTemaId(Long temaId) {
		this.temaId = temaId;
	}
	public String getCidade() {
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getDistanciaDaPessoaLogada() {
		return distanciaDaPessoaLogada;
	}
	public void setDistanciaDaPessoaLogada(String distanciaDaPessoaLogada) {
		this.distanciaDaPessoaLogada = distanciaDaPessoaLogada;
	}
	
	public Set<ConfirmacaoDeInfo> getConfirmacaoDeInfo() {
		return confirmacaoDeInfo;
	}
	public void setConfirmacaoDeInfo(Set<ConfirmacaoDeInfo> confirmacaoDeInfo) {
		this.confirmacaoDeInfo = confirmacaoDeInfo;
	}
	public boolean isConfirmadaPeloUsuarioLogado() {
		return confirmadaPeloUsuarioLogado;
	}
	public void setConfirmadaPeloUsuarioLogado(boolean confirmadaPeloUsuarioLogado) {
		this.confirmadaPeloUsuarioLogado = confirmadaPeloUsuarioLogado;
	}
	public String getImagem() {
		return imagem;
	}
	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cidade == null) ? 0 : cidade.hashCode());
		result = prime * result + ((confirmacaoDeInfo == null) ? 0 : confirmacaoDeInfo.hashCode());
		result = prime * result + (confirmadaPeloUsuarioLogado ? 1231 : 1237);
		result = prime * result + ((corpo == null) ? 0 : corpo.hashCode());
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((distanciaDaPessoaLogada == null) ? 0 : distanciaDaPessoaLogada.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((localizacao == null) ? 0 : localizacao.hashCode());
		result = prime * result + ((pessoa == null) ? 0 : pessoa.hashCode());
		result = prime * result + ((tema == null) ? 0 : tema.hashCode());
		result = prime * result + ((temaId == null) ? 0 : temaId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Postagem other = (Postagem) obj;
		if (cidade == null) {
			if (other.cidade != null)
				return false;
		} else if (!cidade.equals(other.cidade))
			return false;
		if (confirmacaoDeInfo == null) {
			if (other.confirmacaoDeInfo != null)
				return false;
		} else if (!confirmacaoDeInfo.equals(other.confirmacaoDeInfo))
			return false;
		if (confirmadaPeloUsuarioLogado != other.confirmadaPeloUsuarioLogado)
			return false;
		if (corpo == null) {
			if (other.corpo != null)
				return false;
		} else if (!corpo.equals(other.corpo))
			return false;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (distanciaDaPessoaLogada == null) {
			if (other.distanciaDaPessoaLogada != null)
				return false;
		} else if (!distanciaDaPessoaLogada.equals(other.distanciaDaPessoaLogada))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (localizacao == null) {
			if (other.localizacao != null)
				return false;
		} else if (!localizacao.equals(other.localizacao))
			return false;
		if (pessoa == null) {
			if (other.pessoa != null)
				return false;
		} else if (!pessoa.equals(other.pessoa))
			return false;
		if (tema == null) {
			if (other.tema != null)
				return false;
		} else if (!tema.equals(other.tema))
			return false;
		if (temaId == null) {
			if (other.temaId != null)
				return false;
		} else if (!temaId.equals(other.temaId))
			return false;
		return true;
	}
}
