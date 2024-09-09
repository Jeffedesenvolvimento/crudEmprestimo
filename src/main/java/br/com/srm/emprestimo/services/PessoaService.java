package br.com.srm.emprestimo.services;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.srm.emprestimo.en.StatusRecebimentoEnum;
import br.com.srm.emprestimo.en.TipoIdentificadorEnum;
import br.com.srm.emprestimo.model.Emprestimo;
import br.com.srm.emprestimo.model.Pessoa;
import br.com.srm.emprestimo.repositories.PessoaRepository;
import br.com.srm.emprestimo.utils.ValidaIdentificadorUtil;

@Service
public class PessoaService {
	@Autowired
	private PessoaRepository pessoaRepository;

	@Autowired
	private EmprestimoService emprestimoService;

	private Double valorMinimoParcela = 0D;
	private Double valorMaximoEmprestimo = 0D;

	public String criarPessoa(Long id, String nome, String identificador, Date dataNascimento) {
		if (!this.validarIdentificador(identificador)) {
			return "idententificador inválido";
		}
		return "Criado com sucesso"
				+ this.pessoaRepository.save(this.setPessoa(id, nome, identificador, dataNascimento)).getId();
	}

	private Pessoa setPessoa(Long id, String nome, String identificador, Date dataNascimento) {
		Pessoa pessoa = new Pessoa();
		if (id != null) {
			pessoa.setId(id);
		}
		pessoa.setNome(nome);
		pessoa.setDataNascimento(dataNascimento);
		pessoa.setIdentificador(identificador);
		pessoa.setTipoIdentificador(this.setTipoIdentificador(identificador));
		pessoa.setValorMinimoParcela(this.valorMinimoParcela);
		pessoa.setValorMaximoEmprestimo(this.valorMaximoEmprestimo);
		pessoa.setStatus(true);
		return pessoa;
	}

	private Boolean validarIdentificador(String identificador) {
		ValidaIdentificadorUtil validadorIdentificadorUtil = new ValidaIdentificadorUtil();
		if (identificador.length() == 11) {
			return validadorIdentificadorUtil.validarCpf(identificador);
		}
		if (identificador.length() == 14) {
			return validadorIdentificadorUtil.validarCnpj(identificador);
		}
		if (identificador.length() == 8) {
			return validadorIdentificadorUtil.validarEstudante(identificador);
		}
		if (identificador.length() == 10) {
			return validadorIdentificadorUtil.validarAposentado(identificador);
		}
		return Boolean.FALSE;
	}

	public String deletarPessoa(Long id) {
		Pessoa pessoa = this.pessoaRepository.findById(id).orElse(null);
		if (pessoa != null && this.validarEmprestimoEmDia(pessoa)) {
			pessoa.setStatus(false);
			this.pessoaRepository.save(pessoa);
			return "Deletado com sucesso";
		}
		return "Pessoa com emprestimo em aberto";
	}

	private Boolean validarEmprestimoEmDia(Pessoa pessoa) {
		List<Emprestimo> emprestimos = this.emprestimoService.findByPessoa(pessoa);
		for (Emprestimo emprestimo : emprestimos) {
			if (emprestimo.getStatusRecebimento().getDescricao()
					.equalsIgnoreCase(StatusRecebimentoEnum.ATRASADO.getDescricao())) {
				return Boolean.FALSE;
			}
		}

		return Boolean.TRUE;
	}

	public List<Pessoa> listartodos() {
		return pessoaRepository.findAll();
	}

	private TipoIdentificadorEnum setTipoIdentificador(String identificador) {
		if (identificador.length() == 11) {
			this.valorMinimoParcela = 300.00;
			this.valorMaximoEmprestimo = 10000.00;
			return TipoIdentificadorEnum.PESSOAFISICA;
		}
		if (identificador.length() == 14) {
			this.valorMinimoParcela = 1000.00;
			this.valorMaximoEmprestimo = 100000.00;
			return TipoIdentificadorEnum.PESSOAJURIDICA;
		}
		if (identificador.length() == 8) {
			this.valorMinimoParcela = 100.00;
			this.valorMaximoEmprestimo = 10000.00;
			return TipoIdentificadorEnum.ESTUDANTEUNIVERSITARIO;
		}
		if (identificador.length() == 10) {
			this.valorMinimoParcela = 400.00;
			this.valorMaximoEmprestimo = 25000.00;
			return TipoIdentificadorEnum.APOSENTADO;
		}
		return null;
	}

	public Pessoa validarPessoa(String identificador) {
		return pessoaRepository.findByIdentificador(identificador);
	}
}
