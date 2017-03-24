package br.edu.faculdadedelta.modelo;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Reserva
 *
 */
@Entity

public class Reserva extends BaseEntity<Long>  {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "id_reserva", unique = true, nullable = false)
	private long id;

	@OneToMany(fetch=FetchType.LAZY)
	private List<Livro> livros = new ArrayList<Livro>();
	
	@ManyToOne(cascade = { CascadeType.PERSIST }, fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa", insertable = true, updatable = false, nullable = false)
	private Pessoa pessoa;
	
	public Reserva() {
	
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Livro> getLivros() {
		return livros;
	}

	public void setLivros(List<Livro> livros) {
		this.livros = livros;
	}
   
	
}
