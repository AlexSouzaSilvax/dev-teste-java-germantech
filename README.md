# Dev Teste JAVA - Germantech Sistemas

Faça um programa (CRUD) em JAVA com os requisitos abaixo, utilizando um banco de dados para armazenar as informações (preferencialmente PostgreSQL).

O programa deve cadastrar um usuário com as informações de: Nome, telefone, e-mail, CPF e senha.
Deverá existir outra classe chamada "CadastroUsuarios" que seja responsável por gerenciar a lista de usuários. Esta classe deve permitir as seguintes operações:

Faça uma interface para permitir ao usuário escolher entre as operações mencionadas abaixo. A tela poderá ser em JFrame ou como preferir.

1 - Adicionar um novo usuário.
2 - Editar as informações de um usuário existente (com base no CPF).
3 - Excluir um usuário (com base no CPF).
4 - Listar todos os usuários cadastrados.

Além disso, outras funcionalidades necessárias:
A conexão com o banco de dados pode ser feito preferencialmente com: JDBC Connection ou Hibernate;
Organização na estrutura de pastas (em MVC ou alguma estrutura de sua escolha);
Implementação de Interface com os respectivos métodos do CRUD;
As senhas dos usuários devem ser armazenadas de forma segura no banco de dados utilizando hashing (por exemplo, usando bcrypt ou outro algoritmo seguro);
Ao listar usuários, implemente filtros (por nome, email ou parte do CPF) para tornar a consulta mais eficiente e prática.
Deve existir tratamento de exceções adequado para casos como tentar cadastrar CPF's duplicados e/ou usuário não encontrado para edição/exclusão.

Regras básicas:
Adicionar máscara no campo de CPF.
O campo CPF deve ser validado, para permitir apenas CPF's válidos.
Os campos CPF e nome devem ser obrigatórios.

## Tecnologias Utilizadas

- **Java 8**
- **Java Swing**
- **PostgreSQL**
- **Supabase**

## Hospedagem

- **Banco de Dados**: [Supabase](https://supabase.com/)

## Metodologia de Desenvolvimento

Apliquei metodologias ágeis para o desenvolvimento do teste, especificamente o Kanban. Você pode acompanhar o progresso do projeto no nosso quadro do Trello: [Dev Teste Java Board](https://trello.com/b/DrTUnQpj/dev-teste-java-germantech-sistemas).

## Configurações do Projeto

Defina em sua máquina local ou cloud as seguintes variáveis de ambiente:
 
  export DATABASE_URL=url_seu_banco

  export DATABASE_USERNAME=seu_username

  export DATABASE_PASSWORD=sua_password



Se você tiver alguma dúvida ou sugestão, sinta-se à vontade para entrar em contato.

Email: alexsouzasilvax@gmail.com

---

_Este projeto foi desenvolvido como teste técnico para vaga na empresa Germantech._