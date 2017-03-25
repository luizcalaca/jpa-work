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

public class PessoaTest{
	
	private static final Object NOME_PADRAO = "Luiz Fernando";
	private EntityManager em;
	
	
	@Before
	public void instanciarEntityManager(){em = JpaUtil.INSTANCE.getEntityManager();}
	
	@After
	public void fecharEntityManager(){if(em.isOpen()){em.close();}}
	
	@AfterClass
	public static void deveLimparBaseTeste() {
		EntityManager entityManager = JPAUtil.INSTANCE.getEntityManager();
		
		entityManager.getTransaction().begin();
		
		Query query = entityManager.createQuery("DELETE FROM Pessoa p");
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
	public void deveSalvarPessoa() {
		Pessoa pessoa = new Pessoa();
		pessoa.setNome("Luiz");
		
		Autor autor = new Autor();
		autor.setNome("Fernando");
		
		Reserva reserva = new Reserva();
		
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
		assertTrue("não deve ter ID definido", pessoa.isTransient());

		em.getTransaction().begin();	
		em.persist(pessoa);
		em.getTransaction().commit();
		
		assertNotNull("deve ter ID definido", pessoa.getId());
	}
	
	public void criarPessoaes(int quantidade){
		em.getTransaction().begin();
		for (int i = 0; i < 10; i++) {
			Pessoa pessoa = new Pessoa();
			pessoa.setNome("Luiz Fernando");
			
			Autor autor = new Autor();
			autor.setNome("Fernando");
			
			Reserva reserva = new Reserva();
			
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
			em.persist(pessoa);
		}
		em.getTransaction().commit();
	}

	@Test
	public void deveAlterarPessoa() {
		deveSalvarPessoa();
		
		TypedQuery<Pessoa> query = em.createQuery("SELECT p FROM Pessoa p", Pessoa.class).setMaxResults(1);
		
		Pessoa pessoa = query.getSingleResult();
		
		assertNotNull("deve ter encontrado um Pessoa", pessoa);
		
		Integer versao = pessoa.getVersion();
		
		em.getTransaction().begin();
		
		pessoa.setNome("Luiz Fernando");
		
		Autor autor = new Autor();
		autor.setNome("Fernando");
		
		Reserva reserva = new Reserva();
		
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
		
		pessoa = em.merge(pessoa);
		
		em.getTransaction().commit();
		
		assertNotEquals("deve ter versao incrementada", versao.intValue(), pessoa.getVersion().intValue());
	}
	
	@Test
	public void deveExcluirPessoa() {
		deveSalvarPessoa();
		
		TypedQuery<Long> query = em.createQuery("SELECT MAX(p.id) FROM Pessoa p", Long.class);
		Long id = query.getSingleResult();
		
		em.getTransaction().begin();
		
		Pessoa Pessoa = em.find(Pessoa.class, id);
		em.remove(Pessoa);
		
		em.getTransaction().commit();
		
		Pessoa PessoaExcluido = em.find(Pessoa.class, id);
		
		assertNull("não deve ter encontrado o Pessoa", PessoaExcluido);
	}
	
	
	@Test
	public void deveTrazerTodosPessoaes() {
		for (int i = 0; i < 10; i++) {
			deveSalvarPessoa();
		}
		
		TypedQuery<Pessoa> query = em.createQuery("SELECT p FROM Pessoa p", Pessoa.class);
		List<Pessoa> Pessoas = query.getResultList();
		
		assertFalse("deve ter encontrado um Pessoa", Pessoas.isEmpty());
		assertTrue("deve ter encontrado vários Pessoas", Pessoas.size() >= 10);
	}
	
	@Test
	public void deveTrazerTodosLuizPessoaes() {
		for (int i = 0; i < 10; i++) {
			deveSalvarPessoa();
		}
		
		TypedQuery<Pessoa> query = em.createQuery("SELECT p FROM Pessoa p where p.nome = :nome", Pessoa.class);
		query.setParameter("nome", NOME_PADRAO);
		List<Pessoa> Pessoaes = query.getResultList();
		
		assertFalse("deve ter encontrado um Pessoa", Pessoaes.isEmpty());
		assertTrue("deve ter encontrado vários Pessoas", Pessoaes.size() >= 2);
	}
	
	@Test
	public void deveBuscarPorIdPessoa() {
		for (int i = 0; i < 10; i++) {
			deveSalvarPessoa();
		}
		
		TypedQuery<Pessoa> query = em.createQuery("SELECT p FROM Pessoa p where p.id = :id", Pessoa.class);
		query.setParameter("id", 1);
		Pessoa Pessoa = query.getSingleResult();

		assertTrue("deve ter encontrado um Pessoa", Pessoa.getId() == 1);
	}
	
	@Test
	public void deveConsultarPessoaesChaveValor(){
		criarPessoaes(5);
		
		ProjectionList projection = Projections.projectionList()
				.add(Projections.property("c.id").as("id"))
				.add(Projections.property("c.nome").as("nome"));
		
		Criteria criteria = createCriteria(Pessoa.class, "c")
				.setProjection(projection);
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> clientes = criteria
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
				.list();
		
		assertTrue("verifica se a quantidade de Pessoaes é pelo menos 5", clientes.size() >= 5);
		
		clientes.forEach(clienteMap ->{
			clienteMap.forEach((chave, valor) -> {
				assertTrue("chave deve ser String", chave instanceof String);
				assertTrue("valor deve ser String ou Long", valor instanceof String || valor instanceof Long);
			}) ;
		});
	}
	
	@Test
	public void deveConsultarTodosPessoaes(){
		criarPessoaes(3);
		
		Criteria criteria = createCriteria(Pessoa.class,"c");
		
		@SuppressWarnings("unchecked")
		List<Pessoa> Pessoaes = 
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		.list();
		
		
		assertTrue("lista deve ter pelo menos 3", Pessoaes.size() >= 3);
		
		Pessoaes.forEach(cliente -> {
			assertFalse(cliente.isTransient());
		});
	}
	
	@Test
	public void deveConsultarMaiorIdPessoa(){
		criarPessoaes(3);
		
		Criteria criteria = createCriteria(Pessoa.class, "c")
				.setProjection(Projections.max("c.id"));
		
		Long maiorId = (Long) criteria
				.setResultTransformer(Criteria.PROJECTION)
				.uniqueResult();
		
		assertTrue("ID deve ser pelo menos 3", maiorId >= 3);
	}


}
