package br.edu.faculdadedelta.modelo.test;

import static org.junit.Assert.*;

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
import br.edu.faculdadedelta.util.JPAUtil;
import br.edu.faculdadedelta.util.test.JpaUtil;

public class AutorTest{
	
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
		
		Query query = entityManager.createQuery("DELETE FROM Autor p");
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
	
	@SuppressWarnings("unused")
	private Criteria createCriteria(Class<?> clazz, String alias){
		return getSession().createCriteria(clazz, alias);
	}
	
	@Test
	public void deveSalvarAutor() {
		Autor autor = new Autor("Luiz");
		//assertTrue("não deve ter ID definido", autor.isTransient());

		em.getTransaction().begin();	
		em.persist(autor);
		em.getTransaction().commit();
		
		assertNotNull("deve ter ID definido", autor.getId());
	}
	
	public void criarAutores(int quantidade){
		em.getTransaction().begin();
		for (int i = 0; i < 10; i++) {
			Autor autor = new Autor();
			autor.setNome("teste" + i);
			em.persist(autor);
		}
		em.getTransaction().commit();
	}

	@Test
	public void deveAlterarAutor() {
		deveSalvarAutor();
		
		TypedQuery<Autor> query = em.createQuery("SELECT p FROM Autor p", Autor.class).setMaxResults(1);
		
		Autor autor = query.getSingleResult();
		
		assertNotNull("deve ter encontrado um Autor", autor);
		
		Integer versao = autor.getVersion();
		
		em.getTransaction().begin();
		
		autor.setNome("Antônio Bandeira");
		
		autor = em.merge(autor);
		
		em.getTransaction().commit();
		
		assertNotEquals("deve ter versao incrementada", versao.intValue(), autor.getVersion().intValue());
	}
	
	@Test
	public void deveExcluirAutor() {
		deveSalvarAutor();
		
		TypedQuery<Long> query = em.createQuery("SELECT MAX(p.id) FROM Autor p", Long.class);
		Long id = query.getSingleResult();
		
		em.getTransaction().begin();
		
		Autor Autor = em.find(Autor.class, id);
		em.remove(Autor);
		
		em.getTransaction().commit();
		
		Autor AutorExcluido = em.find(Autor.class, id);
		
		assertNull("não deve ter encontrado o Autor", AutorExcluido);
	}
	
	
	@Test
	public void deveTrazerTodosAutores() {
		for (int i = 0; i < 10; i++) {
			deveSalvarAutor();
		}
		
		TypedQuery<Autor> query = em.createQuery("SELECT p FROM Autor p", Autor.class);
		List<Autor> Autors = query.getResultList();
		
		assertFalse("deve ter encontrado um Autor", Autors.isEmpty());
		assertTrue("deve ter encontrado vários Autors", Autors.size() >= 10);
	}
	
	@Test
	public void deveTrazerTodosLuizAutores() {
		for (int i = 0; i < 10; i++) {
			deveSalvarAutor();
		}
		
		TypedQuery<Autor> query = em.createQuery("SELECT p FROM Autor p where p.nome = :nome", Autor.class);
		query.setParameter("nome", NOME_PADRAO);
		List<Autor> autores = query.getResultList();
		
		assertFalse("deve ter encontrado um Autor", autores.isEmpty());
		assertTrue("deve ter encontrado vários Autors", autores.size() >= 2);
	}
	
	@Test
	public void deveBuscarPorIdAutor() {
		for (int i = 0; i < 10; i++) {
			deveSalvarAutor();
		}
		
		TypedQuery<Autor> query = em.createQuery("SELECT p FROM Autor p where p.id = :id", Autor.class);
		query.setParameter("id", 1);
		Autor autor = query.getSingleResult();

		assertTrue("deve ter encontrado um Autor", autor.getId() == 1);
	}
	
	@Test
	public void deveConsultarAutoresChaveValor(){
		criarAutores(5);
		
		ProjectionList projection = Projections.projectionList()
				.add(Projections.property("c.id").as("id"))
				.add(Projections.property("c.nome").as("nome"));
		
		Criteria criteria = createCriteria(Autor.class, "c")
				.setProjection(projection);
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> clientes = criteria
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
				.list();
		
		assertTrue("verifica se a quantidade de autores é pelo menos 5", clientes.size() >= 5);
		
		clientes.forEach(clienteMap ->{
			clienteMap.forEach((chave, valor) -> {
				assertTrue("chave deve ser String", chave instanceof String);
				assertTrue("valor deve ser String ou Long", valor instanceof String || valor instanceof Long);
			}) ;
		});
	}
	
	@Test
	public void deveConsultarTodosAutores(){
		criarAutores(3);
		
		Criteria criteria = createCriteria(Autor.class,"c");
		
		@SuppressWarnings("unchecked")
		List<Autor> autores = 
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		.list();
		
		
		assertTrue("lista deve ter pelo menos 3", autores.size() >= 3);
		
		autores.forEach(cliente -> {
			assertFalse(cliente.isTransient());
		});
	}
	
	@Test
	public void deveConsultarMaiorIdAutor(){
		criarAutores(3);
		
		Criteria criteria = createCriteria(Autor.class, "c")
				.setProjection(Projections.max("c.id"));
		
		Long maiorId = (Long) criteria
				.setResultTransformer(Criteria.PROJECTION)
				.uniqueResult();
		
		assertTrue("ID deve ser pelo menos 3", maiorId >= 3);
	}


}
