package usjt.project.civitas.civitas.service;

import java.security.Key;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import usjt.project.civitas.civitas.entity.Pessoa;
import usjt.project.civitas.civitas.repository.PessoaRepository;
import usjt.project.civitas.civitas.validation.IDNullException;
import usjt.project.civitas.civitas.validation.InvalidPasswordException;
import usjt.project.civitas.civitas.validation.InvalidTokenException;
import usjt.project.civitas.civitas.validation.NotFoundPersonException;
import usjt.project.civitas.civitas.validation.NullTokenException;
import usjt.project.civitas.civitas.validation.ObjectNullException;
import usjt.project.civitas.civitas.validation.UserLoginException;
import usjt.project.civitas.civitas.validation.UserLogoutException;


// TODO: encryptar a senha, fazer o envio de email e mudar o paradigma para orientação à exception

@Service
public class PessoaService {
	
	//Chave para ser usada na geração do token para o usuário
	private String token = "This is a token, and contains a secret";
	
	@Value("${server.port}")
	String serverPort;
	
	@Autowired
	private PessoaRepository repo;
	
	public String inserir(Pessoa pessoa) throws Exception {
		try {
			validarInsert(pessoa);
			if (repo.findByCpf(pessoa.getCpf()) != null) {
				return "CPF";
			} else if (repo.findByEmail(pessoa.getEmail()) != null) {
				return "Email";
			} else {
				pessoa.setSenha(criptografar(pessoa.getSenha()));
				pessoa = repo.save(pessoa);
				EnviarEmailConfirmacao(pessoa);
				return "ok";
			}
		} catch(Exception e) {
			throw e;
		}
	}
	
	//Função para atualizar uma pessoa
	//Recebe um objeto pessoa, que representa o objeto a subtituir o objeto que temos para aquele usuário
	public Pessoa update(Pessoa pessoa) throws Exception {
		try {
			validarUpdate(pessoa);
			return repo.save(pessoa);
		} catch(Exception e) {
			throw e;
		}
	}
	
	//Função para validar a operação de insert para o usuário
	//Recebe um objeto pessoa a ser válidado
	public void validarInsert(Pessoa pessoa) throws Exception {
		validar(pessoa);
	}
	
	//Função para validar a operação de update para o usuário
	//Recebe um objeto pessoa a ser válidado
	public void validarUpdate(Pessoa pessoa) throws Exception {
		validar(pessoa);
		if(pessoa.getId() == null) {
			throw new IDNullException();
		}
		
	}
	
	//Função para validar se o objeto pode realizar uma operação de insert ou update
	//Valida os atributos básicos do usuário
	public void validar(Pessoa pessoa) throws Exception {
		if(pessoa.getNome() == null || pessoa.getNome().isEmpty()) {
			throw new ObjectNullException("Nome do usuário");
		}
		if(pessoa.getCpf() == null || pessoa.getCpf().isEmpty()) {
			throw new ObjectNullException("Cpf do usuário");
		}
		if(pessoa.getEmail() == null || pessoa.getEmail().isEmpty()) {
			throw new ObjectNullException("Email do usuário");
		}
		if(pessoa.getSenha() == null || pessoa.getSenha().isEmpty()) {
			throw new ObjectNullException("Senha do usuário");		
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
                .setIssuer("localhost:" + serverPort)
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
					return usuario.getToken().equals(token);
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
					if(usuario.getToken().equals(token)) {
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
	public Pessoa logar(String email, String senha) throws Exception {
		
		Pessoa pessoa = new Pessoa();
		pessoa.setEmail(email);
		pessoa.setSenha(senha);
		
		pessoa.setSenha(criptografar(pessoa.getSenha()));
		
		Pessoa usuario;
		
		if(pessoa.getEmail() != null && !pessoa.getEmail().isEmpty()) {
			usuario = repo.findByEmail(pessoa.getEmail());
		} else {
			usuario = repo.findByCpf(pessoa.getCpf());
		}
		
		if(usuario != null) {
		
			if(usuario.getToken() != null && usuario.getToken().length() > 0) {
				throw new UserLoginException();
			}	
			
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
	
	//Função responsável pelo reenvio do e-mail
	//Recebe o id do usuário a receber o e-mail
	public Pessoa reenviarEmail(Long id, String token) throws Exception {
		try {
			
			if(id == null) {
				throw new IDNullException();
			}
			
			Optional<Pessoa> pessoa = repo.findById(id);
			
			if(pessoa != null) {
				Pessoa usuario = pessoa.get();
			
				if(compareToken(token, id)) {
				
					EnviarEmailConfirmacao(usuario);
					
					return usuario;
				
				} else {
					throw new InvalidTokenException();
				}
				
			} else {
				throw new NotFoundPersonException();
			}
			
		} catch(Exception e) {
			throw e;
		}
	}
	
	//Função responsável pela confirmar do e-mail
	//Recebe o id do usuário que confirmou o e-mail
	public void ConfirmarEmail(Long id) throws Exception {
		try {
			
			if(id == null) {
				throw new IDNullException();
			}
			
			Optional<Pessoa> pessoa = repo.findById(id);
			
			if(pessoa != null) {
				Pessoa usuario = pessoa.get();
				
				usuario.setEmailConfirmado(true);
				
				repo.save(usuario);
			} else {
				throw new NotFoundPersonException();
			}
			
		} catch(Exception e) {
			throw e;
		}
	}
	
	//Função responsável pelo envio do e-mail de confirmação do usuário
	//Recebe um objeto pessoa, que representa o usuário a receber o e-mail
	public void EnviarEmailConfirmacao(Pessoa pessoa) throws RuntimeException {
		/** Parâmetros de conexão com servidor Gmail */
		Properties props = new Properties();
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.socketFactory.port", "465");
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.port", "465");
	    
	    /** Parâmetros de validação de usuário e senha */
	    Session session = Session.getDefaultInstance(props,
	    	      new javax.mail.Authenticator() {
	    	           protected PasswordAuthentication getPasswordAuthentication()
	    	           {
	    	                 return new PasswordAuthentication("civitascontrole@gmail.com",
	    	                 "Civitas123456");
	    	           }
	    	      });
	    
//	    /** Ativa Debug para sessão */
//	    session.setDebug(true);
	    
	    try {

	    	Message message = new MimeMessage(session);
	        
	        //Remetente
	        message.setFrom(new InternetAddress("civitascontrole@gmail.com"));
	        
	        //Destinatário(s)
	        Address[] toUser = InternetAddress 
	                   .parse("civitascontrole@gmail.com, " + pessoa.getEmail());

	        message.setRecipients(Message.RecipientType.TO, toUser);
	        
	        //Assunto
	        message.setSubject("Bem vindo a Civitas!");
	        
	        //Conteudo
	        message.setText("Para confirmar seu email, clique no link a seguir! http://localhost:8080/pessoa/confirmarEmail?user=" + pessoa.getId());
	        
	        /**Método para enviar a mensagem criada*/
	        Transport.send(message);

	       } catch (MessagingException e) {
	          throw new RuntimeException(e);
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
