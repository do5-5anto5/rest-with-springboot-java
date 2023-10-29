package do55antos.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import do55antos.data_vo_v1.security.AccountCredentialsVO;
import do55antos.data_vo_v1.security.TokenVO;
import do55antos.repositories.UserRepository;
import do55antos.security.jwt.JwtTokenProvider;

@Service
public class AuthService {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenProvider tokenProvider;
	
	@Autowired
	private UserRepository repository;
	
	@SuppressWarnings("rawtypes")
	public ResponseEntity signin(AccountCredentialsVO data) {
		try{
			var username = data.getUsername();
			var password = data.getPassword();
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(username, password));
			
			var user = repository.findByUsername(username);
			
			var tokenResponse = new TokenVO();
			if (user != null) {
				tokenResponse = tokenProvider.createAccessToken(username, user.getRoles());
			} else {
				throw new UsernameNotFoundException("Username " + username + "not found!");
			}
			return ResponseEntity.ok(tokenResponse);
		} catch (Exception e) {
			throw new BadCredentialsException("Invalid username/password suplied");
		}
	}
	
}
