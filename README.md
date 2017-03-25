# jpa-work
Código fonte do trabalho da disciplina Persistência JPA da pós-graduação Desenvolvimento Full Stack da Faculdade Delta

**@MappedSuperclass**

Superclasse
  
**@Version**

Versão
  
**@Entity**

Anotação que demonstra que a entidade é persistível

**@Table**

  Tabela
  
**@Id**

Referencia de que o atributo é chave primária

**@GeneratedValue**

Gerará automaticamente o valor da chave primária

**@Column**

  Coluna
  
**@Basic**

  Colunas
  
**@Temporal**

  Calendário
  
**@ManyToOne**

Mapeamento de muitos para um

**@ManyToMany**

Mapeamento de muitos para muitos

**@OneToOne**

Mapeamento de um para um

**@JoinColumn**

  Unir colunas
  
**@JoinTable**

  Unir tabelas
  

**Qual a responsabilidade/objeto dos métodos do EntityManager:**

**isOpen()**
Verificar se a conexão está aberta


**close()**
Fechar a conexão


**createQuery()**
Criar uma nova query


**find()**
Encontrar um registro de acordo com algum parâmetro


**merge()**
Atualizar o registro no bano de dados


**persist()**
Persistir o objeto no banco de dados


**remove()**
Remover um registro do banco de dados



**Como instânciar Criteria do Hibernate através do EntityManager?**

    private EntityManager em;
    
    private Session getSession(){
		  return (Session) em.getDelegate();
	  }
  
	  private Criteria createCriteria(Class<?> clazz){
		  return getSession().createCriteria(clazz);
	  }
  
	  private Criteria createCriteria(Class<?> clazz, String alias){
		  return getSession().createCriteria(clazz, alias);
	  }
  
**Como abrir uma transação?**

	  private void abrirTransaction(){
		  em.getTransaction().begin();
	  }

**Como fechar uma transação?**

  	 private void fecharTransaction(){
		   if(em.isOpen()){em.close();}
	  }

**Como criar e executar uma query com JPQL?**

  	 private void execQueryJpql(){
		    StringBuilder hql = new StringBuilder();
        hql.append("SELECT COUNT(p.id) FROM Venda v ");

        Query query = em.createQuery(hql.toString());
        Long qtdRegistros = (Long) query.getSingleResult();
	  }

**Qual a responsabilidade dos valores FetchType.LAZY e FetchType.EAGER?**

Quando há uma dependência em um objeto, por exemplo, um livro tem uma lista de autores, e ao chamar o objeto Livro, poderemos somente trazê-lo LAZY -- sem dependências -- ou EAGER -- com suas dependências.

**Qual a responsabilidade dos valores CascadeType.PERSIST e CascadeType.REMOVE?**

PERSIST irá persistir o objeto e suas dependências. O REMOVE irá removerr o objeto com suas dependências.

**Como fazer uma operação BATCH (DELETE ou UPDATE) através do EntityManager?**
	
Deletar e atualizar um objeto.

**Qual a explicação para a exception LazyInitializationException?**

Quando se tenta trazer o objeto que não está mais na sessão ou no EntityManager, então, é lançada tal exceção.
