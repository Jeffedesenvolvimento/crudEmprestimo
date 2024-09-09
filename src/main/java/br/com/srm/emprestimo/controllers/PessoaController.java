package br.com.srm.emprestimo.controllers;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import br.com.srm.emprestimo.model.Pessoa;
import br.com.srm.emprestimo.services.PessoaService;

@Controller
@RequestMapping("/pessoa")
public class PessoaController {

	@Autowired
	PessoaService pessoaService;

	@GetMapping("/teste")
	@ResponseBody
	public String processarArquivoCSV() {
		return "teste ok";
	}

	@PostMapping("/criar")
	@ResponseBody
	public String criarPessoa(@RequestParam String nome, @RequestParam String identificador,
			@RequestParam Date dataNascimento) {
		return pessoaService.criarPessoa(null, nome, identificador, dataNascimento);
	}

	@PutMapping("/atualizar")
	@ResponseBody
	public String atualizarPessoa(@RequestParam(name = "id") Long id, @RequestParam String nome,
			@RequestParam String identificador, @RequestParam Date dataNascimento) {
		return pessoaService.criarPessoa(id, nome, identificador, dataNascimento);
	}

	@DeleteMapping("/deletar")
	@ResponseBody
	public String deletar(@RequestParam(name = "id") Long id) {
		return pessoaService.deletarPessoa(id);
	}

	@GetMapping("")
	@ResponseBody
	public List<Pessoa> listar() {
		return pessoaService.listartodos();
	}
}
