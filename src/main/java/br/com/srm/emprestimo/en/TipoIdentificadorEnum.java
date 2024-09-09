package br.com.srm.emprestimo.en;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum TipoIdentificadorEnum {

	PESSOAFISICA("Pessoa Física"),
	PESSOAJURIDICA ("Pessoa Jurídica"),
	ESTUDANTEUNIVERSITARIO("Estudante Universitário"),
	APOSENTADO ("Aposentado");

	private String descricao;

	private TipoIdentificadorEnum(String descricao) {
		this.descricao = descricao;
	}

	public static TipoIdentificadorEnum valueOfDescricao(final String descricao) {
		for (TipoIdentificadorEnum tipo : values()) {
			if (tipo.getDescricao().equals(descricao)) {
				return tipo;
			}
		}
		return null;
	}
}
