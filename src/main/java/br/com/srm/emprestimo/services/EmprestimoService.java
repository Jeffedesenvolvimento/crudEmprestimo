package br.com.srm.emprestimo.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import br.com.srm.emprestimo.en.StatusPagamentoEnum;
import br.com.srm.emprestimo.en.StatusRecebimentoEnum;
import br.com.srm.emprestimo.en.TipoIdentificadorEnum;
import br.com.srm.emprestimo.model.Emprestimo;
import br.com.srm.emprestimo.model.Pessoa;
import br.com.srm.emprestimo.repositories.EmprestimoRepository;
import br.com.srm.emprestimo.utils.ValidaIdentificadorUtil;

@Service
public class EmprestimoService {

	@Autowired
	private EmprestimoRepository emprestimoRepository;

	private String erroLimite;

	@Value("${urlApiPagemento}")
	private String urlApi;

	public String criarEmprestimo(String identificador, Integer qtdParcelas, Double valorParcela,
			Double valorEmprestimo, Pessoa pessoa) {

		if (!this.validarIdentificador(identificador, pessoa)) {
			return "idententificador inválido";
		}
		if (this.validarLimitesEmprestimo(pessoa, qtdParcelas, valorParcela, valorEmprestimo)) {
			return erroLimite;
		}

		Emprestimo emprestimo = this.setEmprestimo(identificador, qtdParcelas, valorParcela, valorEmprestimo, pessoa);

		emprestimoRepository.save(emprestimo);

		return this.realizarPagamento(emprestimo);
	}

	private String realizarPagamento(Emprestimo emprestimo) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		MultiValueMap<String, Long> map = new LinkedMultiValueMap<String, Long>();
		HttpEntity<MultiValueMap<String, Long>> request = new HttpEntity<MultiValueMap<String, Long>>(map, headers);

		String url = String.format(urlApi + "?idEmprestimo=%s", emprestimo.getId());
		ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

		return response.getBody();
	}

	private Emprestimo setEmprestimo(String identificador, Integer qtdParcelas, Double valorParcela,
			Double valorEmprestimo, Pessoa pessoa) {
		Emprestimo emprestimo = new Emprestimo();
		emprestimo.setPessoa(pessoa);
		emprestimo.setParcelas(qtdParcelas);
		emprestimo.setValorEmprestimo(valorEmprestimo);
		emprestimo.setValorParcela(valorEmprestimo);
		emprestimo.setDiasEmAtraso(0);
		emprestimo.setDtCriacao(LocalDate.now());
		emprestimo.setStatusPagamento(StatusPagamentoEnum.AGUARDANDOPAGAMENTO);
		emprestimo.setStatusRecebimento(StatusRecebimentoEnum.EMDIA);
		return emprestimo;

	}

	private Boolean validarIdentificador(String identificador, Pessoa pessoa) {
		ValidaIdentificadorUtil validadorIdentificadorUtil = new ValidaIdentificadorUtil();

		if(pessoa != null && pessoa.getTipoIdentificador() != null) {
			if (TipoIdentificadorEnum.PESSOAFISICA.getDescricao().equals(pessoa.getTipoIdentificador().getDescricao())) {
				return validadorIdentificadorUtil.validarCpf(identificador);
			}
			if (TipoIdentificadorEnum.PESSOAJURIDICA.getDescricao().equals(pessoa.getTipoIdentificador().getDescricao())) {
				return validadorIdentificadorUtil.validarCnpj(identificador);
			}
			if (TipoIdentificadorEnum.ESTUDANTEUNIVERSITARIO.getDescricao().equals(pessoa.getTipoIdentificador().getDescricao())) {
				return validadorIdentificadorUtil.validarEstudante(identificador);
			}
			if (TipoIdentificadorEnum.APOSENTADO.getDescricao().equals(pessoa.getTipoIdentificador().getDescricao())) {
				return validadorIdentificadorUtil.validarAposentado(identificador);
			}			
		}
		return Boolean.FALSE;

	}

	private Boolean validarLimitesEmprestimo(Pessoa pessoa, Integer qtdParcelas, Double valorParcela,
			Double valorEmprestimo) {
		if (pessoa.getValorMaximoEmprestimo() <= valorEmprestimo) {
			this.erroLimite = "Limite maximo excedido";
			return Boolean.TRUE;
		}

		if (pessoa.getValorMinimoParcela() >= valorParcela) {
			this.erroLimite = "Valor da parcela inferior a minima permitida";
			return Boolean.TRUE;
		}

		if (qtdParcelas >= 24) {
			this.erroLimite = "Quantidade de parcelas superior a permetida";
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public List<Emprestimo> findByPessoa(Pessoa pessoa) {
		return this.emprestimoRepository.findByPessoa(pessoa);
	}

}
