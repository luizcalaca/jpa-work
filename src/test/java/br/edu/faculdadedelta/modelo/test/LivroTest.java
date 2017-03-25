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

import br.edu.faculdadedelta.modelo.Livro;
import br.edu.faculdadedelta.util.JPAUtil;
import br.edu.faculdadedelta.util.test.JpaUtil;

public class LivroTest{
	
	private static final Object TITULO_PADRAO = "Novo";
	private EntityManager em;
	
	
	@Before
	public void instanciarEntityManager(){em = JpaUtil.INSTANCE.getEntityManager();}
	
	@After
	public void fecharEntityManager(){if(em.isOpen()){em.close();}}
	
	@AfterClass
	public static void deveLimparBaseTeste() {
		EntityManager entityManager = JPAUtil.INSTANCE.getEntityManager();
		
		entityManager.getTransaction().begin();
		
		Query query = entityManager.createQuery("DELETE FROM Livro p");
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
	public void deveSalvarLivro() {
		Livro livro = new Livro();
		livro.setTitulo("Novo");
		livro.setIsbn("456-56-564");
		livro.setPreco(50);
		assertTrue("não deve ter ID definido", livro.isTransient());

		em.getTransaction().begin();	
		em.persist(livro);
		em.getTransaction().commit();
		
		assertNotNull("deve ter ID definido", livro.getId());
	}
	
	public void criarLivroes(int quantidade){
		em.getTransaction().begin();
		for (int i = 0; i < 10; i++) {
			Livro livro = new Livro();
			livro.setTitulo("teste" + i);
			livro.setIsbn("456-56-564" + i);
			livro.setPreco(50 + i);
			em.persist(livro);
		}
		em.getTransaction().commit();
	}

	@Test
	public void deveAlterarLivro() {
		deveSalvarLivro();
		
		TypedQuery<Livro> query = em.createQuery("SELECT p FROM livro p", Livro.class).setMaxResults(1);
		
		Livro livro = query.getSingleResult();
		
		assertNotNull("deve ter encontrado um Livro", livro);
		
		Integer versao = livro.getVersion();
		
		em.getTransaction().begin();
		
		livro.setTitulo("Antônio Bandeira");
		livro.setIsbn("456-56-564");
		livro.setPreco(50);
		
		livro = em.merge(livro);
		
		em.getTransaction().commit();
		
		assertNotEquals("deve ter versao incrementada", versao.intValue(), livro.getVersion().intValue());
	}
	
	@Test
	public void deveExcluirLivro() {
		deveSalvarLivro();
		
		TypedQuery<Long> query = em.createQuery("SELECT MAX(p.id) FROM Livro p", Long.class);
		Long id = query.getSingleResult();
		
		em.getTransaction().begin();
		
		Livro livro = em.find(Livro.class, id);
		em.remove(livro);
		
		em.getTransaction().commit();
		
		Livro livroExcluido = em.find(Livro.class, id);
		
		assertNull("não deve ter encontrado o Livro", livroExcluido);
	}
	
	
	@Test
	public void deveTrazerTodosLivroes() {
		for (int i = 0; i < 10; i++) {
			deveSalvarLivro();
		}
		
		TypedQuery<Livro> query = em.createQuery("SELECT p FROM Livro p", Livro.class);
		List<Livro> livros = query.getResultList();
		
		assertFalse("deve ter encontrado um Livro", livros.isEmpty());
		assertTrue("deve ter encontrado vários Livros", livros.size() >= 10);
	}
	
	@Test
	public void deveTrazerTodosLuizLivroes() {
		for (int i = 0; i < 10; i++) {
			deveSalvarLivro();
		}
		
		TypedQuery<Livro> query = em.createQuery("SELECT p FROM Livro p where p.titulo = :titulo", Livro.class);
		query.setParameter("titulo", TITULO_PADRAO);
		List<Livro> livroes = query.getResultList();
		
		assertFalse("deve ter encontrado um Livro", livroes.isEmpty());
		assertTrue("deve ter encontrado vários Livros", livroes.size() >= 2);
	}
	
	@Test
	public void deveBuscarPorIdLivro() {
		for (int i = 0; i < 10; i++) {
			deveSalvarLivro();
		}
		
		TypedQuery<Livro> query = em.createQuery("SELECT p FROM Livro p where p.id = :id", Livro.class);
		query.setParameter("id", 1);
		Livro livro = query.getSingleResult();

		assertTrue("deve ter encontrado um Livro", livro.getId() == 1);
	}
	
	@Test
	public void deveConsultarLivroesChaveValor(){
		criarLivroes(5);
		
		ProjectionList projection = Projections.projectionList()
				.add(Projections.property("c.id").as("id"))
				.add(Projections.property("c.titulo").as("titulo"));
		
		Criteria criteria = createCriteria(Livro.class, "c")
				.setProjection(projection);
		
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> clientes = criteria
				.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP)
				.list();
		
		assertTrue("verifica se a quantidade de Livroes é pelo menos 5", clientes.size() >= 5);
		
		clientes.forEach(clienteMap ->{
			clienteMap.forEach((chave, valor) -> {
				assertTrue("chave deve ser String", chave instanceof String);
				assertTrue("valor deve ser String ou Long", valor instanceof String || valor instanceof Long);
			}) ;
		});
	}
	
	@Test
	public void deveConsultarTodosLivroes(){
		criarLivroes(3);
		
		Criteria criteria = createCriteria(Livro.class,"c");
		
		@SuppressWarnings("unchecked")
		List<Livro> livroes = 
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
		.list();
		
		
		assertTrue("lista deve ter pelo menos 3", livroes.size() >= 3);
		
		livroes.forEach(cliente -> {
			assertFalse(cliente.isTransient());
		});
	}
	
	@Test
	public void deveConsultarMaiorIdLivro(){
		criarLivroes(3);
		
		Criteria criteria = createCriteria(Livro.class, "c")
				.setProjection(Projections.max("c.id"));
		
		Long maiorId = (Long) criteria
				.setResultTransformer(Criteria.PROJECTION)
				.uniqueResult();
		
		assertTrue("ID deve ser pelo menos 3", maiorId >= 3);
	}


}
