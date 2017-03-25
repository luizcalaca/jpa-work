# jpa-work
Código fonte do trabalho da disciplina Persistência JPA da pós-graduação Desenvolvimento Full Stack da Faculdade Delta

**@MappedSuperclass**

Quando for gerado as tabelas tal não será gerada, mas, tão somente as classes filhas.
  
**@Version**

Ao adicionar um atributo com essa anotação não precisamos nos preocupar em alterar seu valor porque o Hibernate fica encarregado dessa função. Seu objetivo é usar automaticamente o número da versão para verificar se o objeto utilizado na transição foi atualizado desde a ultima vez em que ele foi requisitado, evitando que transações diferentes usem o mesmo objeto.

**@Entity**

Anotação que demonstra que a entidade é persistível.

**@Table**

Se necessitarmos definir o nome de uma tabela e outros atributos.
  
**@Id**

Referencia de que o atributo é chave primária.

**@GeneratedValue**

Gerará automaticamente o valor da chave primária.

**@Column**

  Especifia o nome da coluna e outros atributos.
  
**@Basic**

Especifica para um atributo que será coluna do bando algumas propriedades, como por exemplo não ser nulo.
  
**@Temporal**

Configuramos como mapear um Calendar para o banco, apenas a hora (TemporalType.TIME) ou timestamp (TemporalType.TIMESTAMP).
  
**@ManyToOne**

Mapeamento de muitos para um.

**@ManyToMany**

Mapeamento de muitos para muitos.

**@OneToOne**

Mapeamento de um para um.

**@JoinColumn**

Aponta uma coluna que servirá de chave primária na tabela de relacionamento.
  
**@JoinTable**

É para o mapeamento da tabela intermediária (que liga duas tabelas) e contém duas chaves estrangeiras.
  
  
  
**Qual a responsabilidade/objeto dos métodos do EntityManager:**

1. **isOpen()**
Verificar se a conexão está aberta


2. **close()**
Fechar a conexão


3. **createQuery()**
Criar uma nova query


4. **find()**
Encontrar um registro de acordo com algum parâmetro


5. **merge()**
Atualizar o registro no bano de dados


6. **persist()**
Persistir o objeto no banco de dados


7. **remove()**
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
