Perfeito! Aqui está um `README.md` completo, estruturado corretamente para você colocar na raiz do projeto:


# TarefaManager

Projeto **TarefaManager** – gerenciamento de tarefas e subtarefas, desenvolvido em **Java 21** com **Spring Boot** e **H2 Database**.

---

## Tecnologias utilizadas

- Java 21
- Spring Boot
- Maven
- H2 Database (em memória)
- Docker
- JUnit 5 / Mockito (para testes unitários)
- Spring Data JPA
- Spring Web
- Spring Validation
- Swagger/OpenAPI (documentação dos endpoints)

---

## Rodando o projeto com Docker

O projeto já possui um `Dockerfile` configurado para build e execução.

### Passos

1. Certifique-se de ter **Docker Desktop** instalado e rodando.
2. Abra o terminal na raiz do projeto (onde está o `pom.xml`).
3. Build da imagem Docker:
```bash
docker build -t tarefa-manager .
````

4. Rodar o container:

```bash
docker run -p 8080:8080 tarefa-manager
```

5. Acesse o serviço em:
   [http://localhost:8080](http://localhost:8080)

> O banco utilizado é o **H2** em memória, não requer configuração externa.

---

## Endpoints disponíveis

### Usuários

| Método | Endpoint         | Descrição             |
| ------ | ---------------- | --------------------- |
| POST   | `/usuarios`      | Cria um novo usuário  |
| GET    | `/usuarios/{id}` | Busca usuário pelo ID |

### Tarefas

| Método | Endpoint               | Descrição                                                    |
| ------ | ---------------------- | ------------------------------------------------------------ |
| POST   | `/tarefas`             | Cria uma nova tarefa                                         |
| GET    | `/tarefas`             | Lista tarefas (com filtros opcionais: `usuarioId`, `status`) |
| PATCH  | `/tarefas/{id}/status` | Atualiza o status de uma tarefa                              |

### Subtarefas

| Método | Endpoint                         | Descrição                                                     |
| ------ | -------------------------------- | ------------------------------------------------------------- |
| POST   | `/tarefas/{tarefaId}/subtarefas` | Cria uma nova subtarefa para a tarefa informada               |
| GET    | `/tarefas/{tarefaId}/subtarefas` | Lista subtarefas de uma tarefa (com filtro opcional `status`) |
| PATCH  | `/subtarefas/{id}/status`        | Atualiza o status de uma subtarefa                            |

---

## Observações

* Validação de campos é realizada via **Jakarta Validation**.
* O banco **H2** é em memória; todos os dados são perdidos ao reiniciar o container.
* Documentação dos endpoints disponível via Swagger/OpenAPI se a dependência estiver ativa.

---

## Postman Collection

Uma **collection do Postman** está disponível para teste de todos os endpoints do projeto.  
Ela já vem montada com:

- Usuários (criar e buscar)
- Tarefas (criar, listar e atualizar status)
- Subtarefas (criar, listar e atualizar status)

Você pode importar a collection no Postman e executar os requests diretamente, ajustando apenas os `UUIDs` conforme necessário.

> Arquivo da collection: TarefaManager.postman_collection.json (localizado na raiz do projeto)