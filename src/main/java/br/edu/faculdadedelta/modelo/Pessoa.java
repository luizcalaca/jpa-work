package br.edu.faculdadedelta.modelo;

import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Pessoa
 *
 */
@Entity

public class Pessoa extends BaseEntity<Long>  {

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "id_pessoa", unique = true, nullable = false)
	private Long id;
	
	@OneToMany(mappedBy = "pessoa", fetch = FetchType.LAZY)
	private List<Reserva> reservas;
	
	private String nome;

	public Pessoa() {
		
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public List<Reserva> getReservas() {
		return reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
   
}
