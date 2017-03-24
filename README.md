# jpa-work
Código fonte do trabalho da disciplina Persistência JPA da pós-graduação Desenvolvimento Full Stack da Faculdade Delta

@MappedSuperclass
@Version
@Entity
@Table
@Id
@GeneratedValue
@Column
@Basic
@Temporal
@ManyToOne
@ManyToMany
@OneToOne
@JoinColumn
@JoinTable

Qual a responsabilidade/objeto dos métodos do EntityManager:
 isOpen
 close
 createQuery
 find
 merge
 persist
 remove

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
