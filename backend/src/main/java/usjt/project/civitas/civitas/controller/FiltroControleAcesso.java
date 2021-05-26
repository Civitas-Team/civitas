package usjt.project.civitas.civitas.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import usjt.project.civitas.civitas.service.PessoaService;

@Component
@Order(1)
public class FiltroControleAcesso implements Filter {

	@Autowired
	private PessoaService pessoaService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletResponse res = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;

//		res.setHeader("Access-Control-Allow-Origin", "*");
//		res.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, DELETE, OPTIONS");
//		res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		
		res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader("Access-Control-Allow-Credentials", "true");
		res.setHeader("Access-Control-Allow-Methods",
                "ACL, CANCELUPLOAD, CHECKIN, CHECKOUT, COPY, DELETE, GET, HEAD, LOCK, MKCALENDAR, MKCOL, MOVE, OPTIONS, POST, PROPFIND, PROPPATCH, PUT, REPORT, SEARCH, UNCHECKOUT, UNLOCK, UPDATE, VERSION-CONTROL");
		res.setHeader("Access-Control-Max-Age", "3600");
		res.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Key, Authorization");

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
        	res.setStatus(HttpServletResponse.SC_OK);
        } else {
            chain.doFilter(req, res);
        }

	/*	if (byPass(req)) {
			chain.doFilter(request, response);
		} else {
			final String authorization = req.getHeader("authorization");
			try {
				
				if(StringUtils.isNotEmpty(authorization)) {
					pessoaService.getByToken(authorization);
					chain.doFilter(request, response);
				} else {
					sendForbidden(req, res, "Authorization é nulo");
				}
				
			} catch(Exception e) {
				sendForbidden(req, res, e.getMessage());
			}
		}*/
		
	}
	
	private void sendForbidden(HttpServletRequest req, HttpServletResponse res, String messageParam) throws IOException {
		res.setStatus(403);
		res.setContentType("application/json;charset=UTF-8");
		PrintWriter outPrintWriter = res.getWriter();
		ApiMessage message = ApiMessage.buildMessage("Acesso negado! " + messageParam , req);
		outPrintWriter.print(new Gson().toJson(message));
		outPrintWriter.flush();
	}

	//Função para válidar se a chamada é permitida sem o token.
	private boolean byPass(HttpServletRequest req) {
		final String[] freeAcessPoints = { "/login", "/insert", "/logout"};
		for (String freeAcessPoint : freeAcessPoints) {
			if (req.getRequestURI().contains(req.getContextPath() + freeAcessPoint)) {
				return true;
			}
		}
		return false;
	}

}
