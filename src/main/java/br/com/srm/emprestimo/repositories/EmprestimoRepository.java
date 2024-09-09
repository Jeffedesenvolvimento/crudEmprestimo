package br.com.srm.emprestimo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.srm.emprestimo.model.Emprestimo;
import br.com.srm.emprestimo.model.Pessoa;

@Repository
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long>{

	List<Emprestimo> findByPessoa(Pessoa pessoa);

}
