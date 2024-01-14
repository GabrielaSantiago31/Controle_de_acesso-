package io.github.gabrielasantiago.sbootexpsecurity.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import io.github.gabrielasantiago.sbootexpsecurity.domain.entity.Usuario;
import io.github.gabrielasantiago.sbootexpsecurity.domain.security.CustomAuthentication;
import io.github.gabrielasantiago.sbootexpsecurity.domain.security.IdentificacaoUsuario;
import io.github.gabrielasantiago.sbootexpsecurity.domain.service.UsuarioService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{
	
	private final UsuarioService usuarioService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		String login = authentication.getName();
		String senha = (String) authentication.getCredentials();
		
		Usuario usuario =  usuarioService.obterUsuarioComPermissoes(login);
		if(usuarioService != null) {
			boolean senhasBatem = passwordEncoder.matches(senha, usuario.getSenha());
			if(senhasBatem) {
				IdentificacaoUsuario identificacaoUsuario = new IdentificacaoUsuario(
						
						usuario.getId(),
						usuario.getNome(),
						usuario.getLogin(),
						usuario.getPermissoes()
						
						);
				return new CustomAuthentication(identificacaoUsuario);
			}
		}
		
		
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {

		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
