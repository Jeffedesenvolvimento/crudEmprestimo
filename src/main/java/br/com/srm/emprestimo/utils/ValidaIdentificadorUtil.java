package br.com.srm.emprestimo.utils;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;

public class ValidaIdentificadorUtil {

	public Boolean validarCpf(String identificador) {
		CPFValidator cpfValidator = new CPFValidator();
		try {
			cpfValidator.assertValid(identificador);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Boolean validarCnpj(String identificador) {
		CNPJValidator cnpjValidator = new CNPJValidator();
		try {
			cnpjValidator.assertValid(identificador);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public Boolean validarEstudante(String identificador) {
		if (identificador.length() == 8 && (Integer.sum(Integer.valueOf(identificador.substring(0, 1)),
				Integer.valueOf(identificador.substring(7, 8))) == 9)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean validarAposentado(String identificador) {
		CharSequence ultimoElemento = identificador.substring(identificador.length() - 1, identificador.length());
		if (identificador.length() == 10 && (!identificador.substring(0, 9).contains(ultimoElemento))) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
