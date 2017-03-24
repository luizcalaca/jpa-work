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
	@GeneratedValue
	@Column(name = "id_autor", unique = true, nullable = false)
	private Long id;
	
	private String nome;

	public Autor() {
		super();
	}

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
