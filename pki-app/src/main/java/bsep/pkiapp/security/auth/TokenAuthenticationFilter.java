package bsep.pkiapp.security.auth;

import bsep.pkiapp.security.util.TokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private TokenUtils tokenUtils;

	private UserDetailsService userDetailsService;

	protected final Log loggerLog = LogFactory.getLog(getClass());

	public TokenAuthenticationFilter(TokenUtils tokenHelper, UserDetailsService userDetailsService) {
		this.tokenUtils = tokenHelper;
		this.userDetailsService = userDetailsService;
	}

	@Override
	public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		String username;
		String authToken = tokenUtils.getToken(request);

		try {

			if (authToken != null) {
				username = tokenUtils.getEmailFromToken(authToken);

				if (username != null) {
					UserDetails userDetails = userDetailsService.loadUserByUsername(username);
					if (tokenUtils.validateToken(authToken, userDetails).equals(true)) {
						TokenBasedAuthentication authentication = new TokenBasedAuthentication(userDetails);
						authentication.setToken(authToken);
						SecurityContextHolder.getContext().setAuthentication(authentication);
					}
				}
			}

		} catch (ExpiredJwtException ex) {
			loggerLog.debug("Token expired!");
		}

		chain.doFilter(request, response);
	}

}