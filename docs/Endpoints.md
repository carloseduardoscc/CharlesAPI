[<< Voltar](../README.md)
# Endpoints da API

**Observação:** Por padrão a aplicação roda na porta 8080, se está rodando localmente as requisições deverão ter `localhost:8080/{url}`

## Índice:

- [/contactRequest](#contactrequest)
  - [POST /contactRequest/send](#1-contactrequestsend)
- [/auth](#auth)
  - [POST /auth/register](#1-authregister)
  - [POST /auth/login](#2-authlogin)

## /contactRequest

### 1. **/contactRequest/send**

#### Descrição:
Este endpoint recebe uma solicitação de contato de um usuário. O usuário envia suas informações (nome, telefone, e-mail, cidade, tipo de pessoa e mensagem), e a solicitação é enviada no próprio e-mail registrado no `application.properties` da aplicação.

#### Requisição:
**URL**: `/contactRequest/send`  
**Método**: `POST`

**Body**:
```json
{
  "name": "João Silva", //obrigatório
  "phone": "+5511999999999", //obrigatório
  "email": "joao.silva@example.com", //obrigatório
  "city": "São Paulo",
  "personType": "PF",
  "message": "Preciso de ajuda com minha conta." //obrigatório
}
```

## /auth

### 1. **/auth/register**

#### Descrição:
Endpoint para criação de um novo usuário, todos os dados são obrigatórios!

#### Requisição:
**URL**: `/auth/register`  
**Método**: `POST`

**Body**:
```json
{
  "email":"joaosilva@gmail.com",
  "password":"123456789",
  "name":"João",
  "lastName":"Silva"
}
```

### 2. **/auth/login**

#### Descrição:
Este endpoint recebe o login e senha, valida no banco de dados e retorna um token JWT para ser usado nas próximas requisições para autenticar o usuário e autorizar as ações pelas roles dele nos respectivos workspaces
#### Requisição:
**URL**: `/auth/login`  
**Método**: `POST`

**Body**:
```json
{
    "login":"joaobezerra@gmail.com",
    "password":"12345"
}
```

**Resposta**
```json
{
  //token a ser guardado e usado nas próximas requisições
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6Ik..."
}
```
