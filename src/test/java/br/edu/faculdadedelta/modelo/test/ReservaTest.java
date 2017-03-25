package br.edu.faculdadedelta.modelo.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import br.edu.faculdadedelta.modelo.Autor;
import br.edu.faculdadedelta.modelo.Livro;
import br.edu.faculdadedelta.modelo.Pessoa;
import br.edu.faculdadedelta.modelo.Reserva;
import br.edu.faculdadedelta.util.JPAUtil;
import br.edu.faculdadedelta.util.test.JpaUtil;

public class ReservaTest{
	
	private static final Object NOME_PADRAO = "Luiz";
	private EntityManager em;
	
	
	@Before
	public void instanciarEntityManager(){em = JpaUtil.INSTANCE.getEntityManager();}
	
	@After
	public void fecharEntityManager(){if(em.isOpen()){em.close();}}
	
	@AfterClass
	public static void deveLimparBaseTeste() {
		EntityManager entityManager = JPAUtil.INSTANCE.getEntityManager();
		
		entityManager.getTransaction().begin();
		
		Query query = entityManager.createQuery("DELETE FROM Reserva p");
		int qtdRegistrosExcluidos = query.executeUpdate();
		
		entityManager.getTransaction().commit();

		assertTrue("certifica que a base foi limpada", qtdRegistrosExcluidos > 0);
	}
	
	private Session getSession(){
		return (Session) em.getDelegate();
	}
	
	@SuppressWarnings("unused")
	private Criteria createCriteria(Class<?> clazz){
		return getSession().createCriteria(clazz);
	}
	
	private Criteria createCriteria(Class<?> clazz, String alias){
		return getSession().createCriteria(clazz, alias);
	}
	
	@Test
	public void deveSalvarReserva() {
		Reserva reserva = new Reserva();
		
		Pessoa pessoa = new Pessoa();
		pessoa.setNome("Luiz Fernando");
		
		Autor autor = new Autor();
		autor.setNome("Fernando");
		
		
		Livro livro = new Livro();
		livro.setTitulo("teste");
		livro.setIsbn("456-56-564");
		livro.setPreco(50);
		
		List<Livro> livros = new ArrayList<Livro>();
		List<Autor> autores = new ArrayList<Autor>();
		List<Reserva> reservas = new ArrayList<Reserva>();


		autores.add(autor);
		livros.add(livro);
		reservas.add(reserva);
		
		livro.setAutores(autores);
		
		pessoa.setReservas(reservas);
		
		
		reserva.setLivros(livros);
		reserva.setPessoa(pessoa);
		assertTrue("não deve ter ID definido", reserva.isTransient());

		em.getTransaction().begin();	
		em.persist(reserva);
		em.getTransaction().commit();
		
		assertNotNull("deve ter ID definido", reserva.getId());
	}
	
	public void criarReservaes(int quantidade){
		em.getTransaction().begin();
		for (int i = 0; i < 10; i++) {
			Reserva reserva = new Reserva();
			
			Pessoa pessoa = new Pessoa();
			pessoa.setNome("Luiz Fernando");
			
			Autor autor = new Autor();
			autor.setNome("Fernando");
			
			
			Livro livro = new Livro();
			livro.setTitulo("teste");
			livro.setIsbn("456-56-564");
			livro.setPreco(50);
			
			List<Livro> livros = new ArrayList<Livro>();
			List<Autor> autores = new ArrayList<Autor>();
			List<Reserva> reservas = new ArrayList<Reserva>();


			autores.add(autor);
			livros.add(livro);
			reservas.add(reserva);
			
			livro.setAutores(autores);
			
			pessoa.setReservas(reservas);
			
			
			reserva.setLivros(livros);
			reserva.setPessoa(pessoa);
			em.persist(reserva);
		}
		em.getTransaction().commit();
	}

	@Test
	public void deveAlterarReserva() {
		deveSalvarReserva();
		
		TypedQuery<Reserva> query = em.createQuery("SELECT p FROM Reserva p", Reserva.class).setMaxResults(1);
		
		Reserva reserva = query.getSingleResult();
		
		assertNotNull("deve ter encontrado um Reserva", reserva);
		
		Integer versao = reserva.getVersion();
		
		em.getTransaction().begin();
		
		
		Pessoa pessoa = new Pessoa();
		pessoa.setNome("Luiz Fernando");
		
		Autor autor = new Autor();
		autor.setNome("Fernando");
		
		
		Livro livro = new Livro();
		livro.setTitulo("teste");
		livro.setIsbn("456-56-564");
		livro.setPreco(50);
		
		List<Livro> livros = new ArrayList<Livro>();
		List<Autor> autores = new ArrayList<Autor>();
		List<Reserva> reservas = new ArrayList<Reserva>();


		autores.add(autor);
		livros.add(livro);
		reservas.add(reserva);
		
		livro.setAutores(autores);
		
		pessoa.setReservas(reservas);
		
		
		reserva.setLivros(livros);
		reserva.setPessoa(pessoa);
		
		reserva = em.merge(reserva);
		
		em.getTransaction().commit();
		
		assertNotEquals("deve ter versao incrementada", versao.intValue(), reserva.getVersion().intValue());
	}
	
	@Test
	public void deveExcluirReserva() {
		deveSalvarReserva();
		
		TypedQuery<Long> query = em.createQuery("SELECT MAX(p.id) FROM Reserva p", Long.class);
		Long id = query.getSingleResult();
		
		em.getTransaction().begin();
		
		Reserva Reserva = em.find(Reserva.class, id);
		em.remove(Reserva);
		
		em.getTransaction().commit();
		
		Reserva ReservaExcluido = em.find(Reserva.class, id);
		
		assertNull("não deve ter encontrado o Reserva", ReservaExcluido);
	}
	
	
	@Test
	public void deveTrazerTodosReservaes() {
		for (int i = 0; i < 10; i++) {
			deveSalvarReserva();
		}
		
		TypedQuery<Reserva> query = em.createQuery("SELECT p FROM Reserva p", Reserva.class);
		List<Reserva> Reservas = query.getResultList();
		
		assertFalse("deve ter encontrado um Reserva", Reservas.isEmpty());
		assertTrue("deve ter encontrado vários Reservas", Reservas.size() >= 10);
	}
	
	@Test
	public void deveTrazerTodosLuizReservaes() {
		for (int i = 0; i < 10; i++) {
			deveSalvarReserva();
		}
		
		TypedQuery<Reserva> query = em.createQuery("SELECT p FROM Reserva p where p.nome = :nome", Reserva.class);
		query.setParameter("nome", NOME_PADRAO);
		List<Reserva> reservaes = query.getResultList();
		
		assertFalse("deve ter encontrado um Reserva", reservaes.isEmpty());
		assertTrue("deve ter encontrado vários Reservas", reservaes.size() >= 2);
	}
	
	@Test
	public void deveBuscarPorIdReserva() {
		for (int i = 0; i < 10; i++) {
			deveSalvarReserva();
		}
		
		TypedQuery<Reserva> query = em.createQuery("SELECT p FROM Reserva p where p.id = :id", Reserva.class);
		query.setParameter("id", 1);
		Reserva reserva = query.getSingleResult();

		assertTrue("deve ter encontrado um Reserva", reserva.getId() == 1);
	}
	
	@Test
	public void deveConsultarReservaesChaveValor(){
		criarReservaes(5);
		
		ProjectionList projection = Projections.projectionList()
				.add(Projections.property("c.id").as("id"))
				.add(Projections.property("c.nome").as("nome"));
		
		Criteria criteria = createCriteria(Reserva.class, "c")
				.setProjection(projection);
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> clientes = criteria
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
				.list();
		
		assertTrue("verifica se a quantidade de reservaes é pelo menos 5", clientes.size() >= 5);
		
		clientes.forEach(clienteMap ->{
			clienteMap.forEach((chave, valor) -> {
				assertTrue("chave deve ser String", chave instanceof String);
				assertTrue("valor deve ser String ou Long", valor instanceof String || valor instanceof Long);
			}) ;
		});
	}
	
	@Test
	public void deveConsultarTodosReservaes(){
		criarReservaes(3);
		
		Criteria criteria = createCriteria(Reserva.class,"c");
		
		@SuppressWarnings("unchecked")
		List<Reserva> reservaes = 
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		.list();
		
		
		assertTrue("lista deve ter pelo menos 3", reservaes.size() >= 3);
		
		reservaes.forEach(cliente -> {
			assertFalse(cliente.isTransient());
		});
	}
	
	@Test
	public void deveConsultarMaiorIdReserva(){
		criarReservaes(3);
		
		Criteria criteria = createCriteria(Reserva.class, "c")
				.setProjection(Projections.max("c.id"));
		
		Long maiorId = (Long) criteria
				.setResultTransformer(Criteria.PROJECTION)
				.uniqueResult();
		
		assertTrue("ID deve ser pelo menos 3", maiorId >= 3);
	}


}
