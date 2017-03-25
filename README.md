# jpa-work
Código fonte do trabalho da disciplina Persistência JPA da pós-graduação Desenvolvimento Full Stack da Faculdade Delta

@MappedSuperclass
@Version
@Entity
	Anotação que demonstra que a entidade é persistível
@Table
@Id
	Referencia de que o atributo é chave primária
@GeneratedValue
	Gerará automaticamente o valor da chave primária
@Column
@Basic
@Temporal
@ManyToOne
	Mapeamento de muitos para um
@ManyToMany
	Mapeamento de muitos para muitos
@OneToOne
	Mapeamento de um para um
@JoinColumn

@JoinTable

Qual a responsabilidade/objeto dos métodos do EntityManager:
 isOpen
Verificar se a conexão está aberta
 close
Fechar a conexão
 createQuery
Criar uma nova query
 find
Encontrar um registro de acordo com algum parâmetro
 merge
Atualizar o registro no bano de dados
 persist
Persistir o objeto no banco de dados
 remove
Remover um registro do banco de dados


Como instânciar Criteria do Hibernate através do EntityManager?
Dê exemplo do código
Como abrir uma transação?
Dê exemplo do código
Como fechar uma transação?
Dê exemplo do código
Como criar e executar uma query com JPQL?
Dê exemplo do código
Qual a responsabilidade dos valores FetchType.LAZY e FetchType.EAGER?
Qual a responsabilidade dos valores CascadeType.PERSIST e CascadeType.REMOVE?
Como fazer uma operação BATCH (DELETE ou UPDATE) através do EntityManager?
Qual a explicação para a exception LazyInitializationException?
