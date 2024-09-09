package br.com.srm.emprestimo.en;

import lombok.Getter;

@Getter
public enum StatusPagamentoEnum {

	AGUARDANDOPAGAMENTO("Aguardando Pagamento"),
	PAGO ("Pago");

	private String descricao;

	private StatusPagamentoEnum(String descricao) {
		this.descricao = descricao;
	}

	public static StatusPagamentoEnum valueOfDescricao(final String descricao) {
		for (StatusPagamentoEnum tipo : values()) {
			if (tipo.getDescricao().equals(descricao)) {
				return tipo;
			}
		}
		return null;
	}
}
