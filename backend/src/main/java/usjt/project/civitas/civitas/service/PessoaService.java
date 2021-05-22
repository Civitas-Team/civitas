package usjt.project.civitas.civitas.service;

import java.security.Key;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import usjt.project.civitas.civitas.entity.Pessoa;
import usjt.project.civitas.civitas.repository.PessoaRepository;
import usjt.project.civitas.civitas.validation.IDNullException;
import usjt.project.civitas.civitas.validation.InvalidEntityFormatException;
import usjt.project.civitas.civitas.validation.InvalidPasswordException;
import usjt.project.civitas.civitas.validation.NotFoundPersonException;
import usjt.project.civitas.civitas.validation.NullTokenException;
import usjt.project.civitas.civitas.validation.UserLogoutException;


// TODO: encryptar a senha, fazer o envio de email e mudar o paradigma para orientação à exception

@Service
public class PessoaService {
	
	//Chave para ser usada na geração do token para o usuário
	private String token = "This is a token, and contains a secret";
	
	@Autowired
	private PessoaRepository repo;
	
	public String inserir(Pessoa pessoa) throws Exception {
		try {
			if (repo.findByCpf(pessoa.getCpf()) != null) {
				return "CPF";
			} else if (repo.findByEmail(pessoa.getEmail()) != null) {
				return "Email";
			} else {
				pessoa.setSenha(criptografar(pessoa.getSenha()));
				repo.save(pessoa);
				return "ok";
			}
		} catch(Exception e) {
			throw e;
		}
	}
	
	//Função para criptografar a senha do usuário
	//Recebe uma String a ser criptografada
	public String criptografar(String senha) throws Exception {
		try {
			MessageDigest algoritmo = MessageDigest.getInstance("SHA-256");
			byte digestMessage[] = algoritmo.digest(senha.getBytes("UTF-8"));
			StringBuilder hexPassword = new StringBuilder();
			for (byte aByte : digestMessage) {
				hexPassword.append(String.format("%02X", 0xFF & aByte));
			}
			return hexPassword.toString();
		} catch (Exception e) {
			throw e;
		}
	}
	
	//Função busca uma pessoa no banco usando o token
	//Recebe uma String token, que sera usada para busca no banco
	public Pessoa getByToken(String token) throws Exception {
		
		if(token == null || token.isEmpty()) {
			throw new NullTokenException();
		}
		
		Pessoa pessoa = repo.findByToken(token);
		
		if(pessoa != null) {
			return pessoa;
		} else {
			throw new NotFoundPersonException();
		}
		
	}
	
	//Função para gerar token para o usuário
	//Recebe uma Sting que será o valor a ser usado para gerar o token
	public String gerarToken(String subject) {
		
		//The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(token);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
		
      //Let's set the JWT Claims
        String token = Jwts.builder()
                .setIssuedAt(new Date())
                .setSubject(subject)
                .setIssuer("localhost:8080")
                .signWith(signatureAlgorithm, signingKey)
                .compact();
        
		return token;
	}
	
	//Função para comprar dois tokens, assim validando se o usuário é o mesmo
	//Recebe um Long que indica o usuário e uma String com o token a ser comparado
	public boolean compareToken(String token, Long id) throws Exception {
		
		try {
			
			if(id == null) {
				throw new IDNullException();
			}
			
			if(token == null || token.isEmpty()) {
				throw new NullTokenException();
			}
			
			Optional<Pessoa> pessoa = repo.findById(id);
		
			if(pessoa != null) {
				Pessoa usuario = pessoa.get();
				
				if(usuario.getToken() != null && !usuario.getToken().isEmpty()) {
					return usuario.getToken().compareTo(token) == 0;
				} else {
					throw new NullTokenException();
				}
				
			} else {
				throw new NotFoundPersonException();
			}
		
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//Função para excluir token do usuário
	//Recebe um Long que indica o id do usuário que deve ter o token excluido
	//Recebe um token, 
	public boolean excluirToken(Long id, String token) throws Exception {
		try {
			
			if(id == null) {
				throw new IDNullException();
			}
			
			Optional<Pessoa> pessoa = repo.findById(id);
			
			if(pessoa != null) {
				Pessoa usuario = pessoa.get();
				//Valida se token do usuário não é nulo
				if(usuario.getToken() != null && !usuario.getToken().isEmpty()) {
					//Valida se o token recebido é o mesmo do usuário a ter o token excluido
					if(usuario.getToken().compareTo(token) == 0) {
						usuario.setToken(null);
						repo.save(usuario);
						return true;
					} else {
						return false;
					}
				} else {
					throw new UserLogoutException();
				}
			} else {
				throw new NotFoundPersonException();
			}
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//Função responsável pelo logout do usuário
	//Recebe um Long informando o id do usuário a ter o token excluido
	//Recebe uma String informando o token do usuário, para válidar se pode excluir o token
	public boolean logout(Long id, String token) throws Exception  { 
		
		try {
			
			if(id == null) {
				throw new IDNullException();
			}
			
			if(token == null) {
				throw new NullTokenException();
			}
			
			return excluirToken(id, token);
		
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	//Função responsável pelo login
	//Recebe um objeto pessoa, que deve conter o email e a senha do usuario
	//Acha a pessoa pelo email e compara a senha
	//Retorna o objeto pessoa do banco, já com token gerado
	public Pessoa logar(Pessoa pessoa) throws Exception {
		
		if(pessoa == null) {
			throw new InvalidEntityFormatException();
		}
		
		pessoa.setSenha(criptografar(pessoa.getSenha()));
		
		Pessoa usuario;
		
		if(pessoa.getEmail() != null && !pessoa.getEmail().isEmpty()) {
			usuario = repo.findByEmail(pessoa.getEmail());
		} else {
			usuario = repo.findByCpf(pessoa.getCpf());
		}
		
		
		if(usuario != null) {
			
			if(usuario.getSenha().equals(pessoa.getSenha())) {
				usuario.setToken(gerarToken(usuario.getEmail()));
				pessoa = repo.save(usuario);
				return pessoa;
			} else {
				throw new InvalidPasswordException();
			}
			
		} else {
			throw new NotFoundPersonException();
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
