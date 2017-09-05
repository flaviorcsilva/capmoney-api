package br.com.capboy.money.api.security.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import br.com.capboy.money.api.config.CapmoneyApiProperty;

/**
 * Caso eu queresse interceptar as requisições de um lançamento eu implementaria
 * o ResponseBodyAdvice<Lancamento>. Nesse caso a intercepção é do
 * <OAuth2AccessToken>
 *
 */
@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {
	
	@Autowired
	private CapmoneyApiProperty property;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return returnType.getMethod().getName().equals("postAccessToken");
	}

	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {
		HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
		HttpServletResponse resp = ((ServletServerHttpResponse) response).getServletResponse();

		String refreshToken = body.getRefreshToken().getValue();
		adicionaRefreshTokenNoCookie(refreshToken, req, resp);

		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;
		removeRefreshTokenDoBody(token);

		return body;
	}

	private void adicionaRefreshTokenNoCookie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {
		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
		refreshTokenCookie.setHttpOnly(true);
		refreshTokenCookie.setSecure(property.getSeguranca().isEnableHttps());
		refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token");
		refreshTokenCookie.setMaxAge(2592000); // 30 dias
		
		resp.addCookie(refreshTokenCookie);
	}

	private void removeRefreshTokenDoBody(DefaultOAuth2AccessToken token) {
		token.setRefreshToken(null);
	}
}