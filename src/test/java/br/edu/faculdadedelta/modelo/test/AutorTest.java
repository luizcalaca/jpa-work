package br.edu.faculdadedelta.modelo.test;

import static org.junit.Assert.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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
	
	@Test
	public void deveSalvarAutor() {
		Autor autor = new Autor("Luiz");
		//assertTrue("não deve ter ID definido", autor.isTransient());

		em.getTransaction().begin();	
		em.persist(autor);
		em.getTransaction().commit();
		
		assertNotNull("deve ter ID definido", autor.getId());
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


}
