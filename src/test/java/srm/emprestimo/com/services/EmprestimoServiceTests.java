package srm.emprestimo.com.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import br.com.srm.emprestimo.en.TipoIdentificadorEnum;
import br.com.srm.emprestimo.model.Emprestimo;
import br.com.srm.emprestimo.model.Pessoa;
import br.com.srm.emprestimo.repositories.EmprestimoRepository;
import br.com.srm.emprestimo.services.EmprestimoService;


@ExtendWith(MockitoExtension.class)
public class EmprestimoServiceTests {
	
	@InjectMocks
	private EmprestimoService emprestimoService;
	
	@MockBean
	private EmprestimoRepository emprestimoRepository;

	private Pessoa pessoa;

	@BeforeEach
	public void setUp() {
		this.pessoa = new Pessoa();
		this.pessoa.setId(1l);
	}
	
	@Test
	void IdentificadorPFInvalido() throws Exception {		
		this.pessoa.setTipoIdentificador(TipoIdentificadorEnum.PESSOAFISICA);
		var response = emprestimoService.criarEmprestimo("123456789123", 2, 600d, 1000d, this.pessoa);
		assertEquals("idententificador inválido", response); 
	}
	
	@Test
	void IdentificadorPJInvalido() throws Exception {		
		this.pessoa.setTipoIdentificador(TipoIdentificadorEnum.PESSOAJURIDICA);
		var response = emprestimoService.criarEmprestimo("12345678912345", 2, 600d, 1000d, this.pessoa);
		assertEquals("idententificador inválido", response); 
	}
	
	@Test
	void IdentificadorEUInvalido() throws Exception {		
		this.pessoa.setTipoIdentificador(TipoIdentificadorEnum.ESTUDANTEUNIVERSITARIO);
		var response = emprestimoService.criarEmprestimo("12345677", 2, 600d, 1000d, this.pessoa);
		assertEquals("idententificador inválido", response); 
	}
	
	@Test
	void IdentificadorAPInvalido() throws Exception {		
		this.pessoa.setTipoIdentificador(TipoIdentificadorEnum.APOSENTADO);
		var response = emprestimoService.criarEmprestimo("1234567891", 2, 600d, 1000d, this.pessoa);
		assertEquals("idententificador inválido", response); 
	}
	
	@Test
	void TipoIdentificadorNull() throws Exception {		
		this.pessoa.setTipoIdentificador(null);
		var response = emprestimoService.criarEmprestimo("1234567891", 2, 600d, 1000d, this.pessoa);
		assertEquals("idententificador inválido", response); 
	}
	@Test
	void validarValorPFMaximoForaDoPermitido() throws Exception {
		this.pessoa.setTipoIdentificador(TipoIdentificadorEnum.PESSOAFISICA);
		this.pessoa.setValorMaximoEmprestimo(10000d);
		this.pessoa.setValorMinimoParcela(300d);
		var response = emprestimoService.criarEmprestimo("07909928920", 2, 100d, 100000d, this.pessoa);
		assertEquals("Limite maximo excedido", response); 
	}
	
	@Test
	void validarValorPFForaDoMinimoParcela() throws Exception {		
		this.pessoa.setTipoIdentificador(TipoIdentificadorEnum.PESSOAFISICA);
		this.pessoa.setValorMaximoEmprestimo(10000d);
		this.pessoa.setValorMinimoParcela(300d);
		var response = emprestimoService.criarEmprestimo("07909928920", 2, 100d, 1000d, this.pessoa);
		assertEquals("Valor da parcela inferior a minima permitida", response); 
	}
	
	@Test
	void validarValorPJMaximoForaDoPermitido() throws Exception {
		this.pessoa.setTipoIdentificador(TipoIdentificadorEnum.PESSOAFISICA);
		this.pessoa.setValorMaximoEmprestimo(100000d);
		this.pessoa.setValorMinimoParcela(1000d);
		var response = emprestimoService.criarEmprestimo("07909928920", 2, 1000d, 100000d, this.pessoa);
		assertEquals("Limite maximo excedido", response); 
	}
	
	@Test
	void validarValorPJForaDoMinimoParcela() throws Exception {		
		this.pessoa.setTipoIdentificador(TipoIdentificadorEnum.PESSOAFISICA);
		this.pessoa.setValorMaximoEmprestimo(10000d);
		this.pessoa.setValorMinimoParcela(1000d);
		var response = emprestimoService.criarEmprestimo("07909928920", 2, 100d, 1000d, this.pessoa);
		assertEquals("Valor da parcela inferior a minima permitida", response); 
	}
	
	@Test
	void validarQtdParcelas() throws Exception {
		this.pessoa.setTipoIdentificador(TipoIdentificadorEnum.PESSOAFISICA);
		this.pessoa.setValorMaximoEmprestimo(10000d);
		this.pessoa.setValorMinimoParcela(300d);
		var response = emprestimoService.criarEmprestimo("07909928920", 30, 600d, 1000d, this.pessoa);
		assertEquals("Quantidade de parcelas superior a permetida", response); 
	}
	
}
