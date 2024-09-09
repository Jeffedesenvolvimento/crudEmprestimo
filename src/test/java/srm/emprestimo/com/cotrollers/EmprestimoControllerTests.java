package srm.emprestimo.com.cotrollers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.srm.emprestimo.controllers.EmprestimoController;
import br.com.srm.emprestimo.model.Pessoa;
import br.com.srm.emprestimo.services.EmprestimoService;
import br.com.srm.emprestimo.services.PessoaService;


@ExtendWith(MockitoExtension.class)
public class EmprestimoControllerTests {


	@InjectMocks
	private EmprestimoController emprestimoController;

	@Mock
	EmprestimoService emprestimoService;

	@Mock
	private PessoaService pessoaService;

	private Pessoa pessoa;

	@BeforeEach
	public void setUp() {
		this.pessoa = new Pessoa();
		this.pessoa.setId(1l);
	}

	@Test
	void criarPessoaNaoCadastrada() throws Exception {
		when(pessoaService.validarPessoa("12345678")).thenReturn(null);
		var response = emprestimoController.criarEmprestimo("12345678", 2, 600d, 1000d);
		assertEquals("Pessoa não cadastrado",response);
	}

	@Test
	void criarComSucesso() throws Exception {
		when(pessoaService.validarPessoa("123456789123")).thenReturn(this.pessoa);
		when(emprestimoService.criarEmprestimo("123456789123", 2, 600d, 1000d, this.pessoa)).thenReturn("Pagamento efetuado");
		var response = emprestimoController.criarEmprestimo("123456789123", 2, 600d, 1000d);
		assertEquals("Pagamento efetuado", response);
		;
	}
}
