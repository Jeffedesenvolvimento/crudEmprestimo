package br.com.srm.emprestimo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.srm.emprestimo.model.Pessoa;
import br.com.srm.emprestimo.services.EmprestimoService;
import br.com.srm.emprestimo.services.PessoaService;

@Controller
@RequestMapping("/emprestimo")
public class EmprestimoController {

	@Autowired
	EmprestimoService emprestimoService;
	
	@Autowired
	private PessoaService pessoaService;

	@PostMapping("/criar")
	@ResponseBody
	public String criarEmprestimo(@RequestParam String identificador, @RequestParam Integer qtdParcelas,
			@RequestParam Double valorParcela, @RequestParam Double valorEmprestimo) {
		Pessoa pessoa = pessoaService.validarPessoa(identificador);
		if (pessoa == null) {
			return "Pessoa não cadastrado";
		}
		return emprestimoService.criarEmprestimo(identificador, qtdParcelas, valorParcela, valorEmprestimo, pessoa);
	}

}
