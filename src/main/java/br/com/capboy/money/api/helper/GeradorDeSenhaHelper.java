package br.com.capboy.money.api.helper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorDeSenhaHelper {
	
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("admin"));
	}
}
