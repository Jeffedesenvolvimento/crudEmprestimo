package br.com.srm.emprestimo.model;

import java.io.Serializable;
import java.sql.Date;

import br.com.srm.emprestimo.en.TipoIdentificadorEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Pessoa implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private String identificador;
	private Date dataNascimento;
	private TipoIdentificadorEnum tipoIdentificador;
	private Double valorMinimoParcela;
	private Double valorMaximoEmprestimo;	
	private boolean status;
}
