package br.com.srm.emprestimo.en;

import lombok.Getter;

@Getter
public enum StatusRecebimentoEnum {

	ATRASADO("Atrasado "),
	EMDIA ("Em dia");

	private String descricao;

	private StatusRecebimentoEnum(String descricao) {
		this.descricao = descricao;
	}

	public static StatusRecebimentoEnum valueOfDescricao(final String descricao) {
		for (StatusRecebimentoEnum tipo : values()) {
			if (tipo.getDescricao().equals(descricao)) {
				return tipo;
			}
		}
		return null;
	}
}
