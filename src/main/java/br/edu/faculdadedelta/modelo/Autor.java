package br.edu.faculdadedelta.modelo;


import javax.persistence.*;

/**
 * Entity implementation class for Entity: Autor
 *
 */
@Entity
public class Autor extends BaseEntity<Long>  {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id_autor", unique = true, nullable = false)
	private long id;
	
	private String nome;

	public Autor(String nome) {
		this.nome = nome;
	}

	public Autor() {
		super();
	}
	
	@Override
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
   
}
