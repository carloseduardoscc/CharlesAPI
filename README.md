### _Em desenvolvimento..._

# Charles² - API Plataforma de Gerenciamento de Chamados

## Descrição

Esta é a API backend de uma plataforma de gerenciamento e abertura de chamados. O objetivo é fornecer um sistema para organizar e acompanhar ordens de serviço e organizar tudo em áreas de trabalho _'workspaces'_ com suporte a autenticação por cargos _'roles'_.

## Funcionalidades

![funcionalidades](https://github.com/user-attachments/assets/ae8c4556-00c3-4cf3-90b3-2dada235a8a6)

## Endpoints

[Veja aqui os endpoints disponíveis da API](docs/Endpoints.md)

## Tecnologias utilizadas no momento

- **Aplicação backend:** Java, Spring, Maven
- **Banco de Dados:** M2 _(Para testes)_
- **Ambiente:**  Docker e Docker Compose

## Como rodar o projeto
1. **Configurações**<br>
   Os arquivos `.properties` em [resources](src/main/resources) contém valores sensíveis a serem preenchidos, como usuário e senha de email, entre outros.

2. **Requisitos**<br>
   Ter instalado e rodando  o Docker Compose localmente.<br> 
   [Instalar Docker compose](https://docs.docker.com/compose/install/) 

3. **Clonar o repositório e executar o docker-compose**
   ```bash
   git clone git@github.com:carloseduardoscc/CharlesAPI.git
   cd CharlesAPI
   docker-compose up --build
   
4. **Pronto! A API já pode receber as [requisições](#Endpoints)**

_Obs.: A primeira vez subindo a aplicação pode demorar para fazer o download das dependências_
