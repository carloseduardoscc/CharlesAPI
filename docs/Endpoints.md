[<< Voltar](../README.md)
# Endpoints da API

**Observação:** Por padrão a aplicação roda na porta 8080, se está rodando localmente as requisições deverão ter `localhost:8080/{url}`

## Índice:

- [/contactRequest](#contactrequest)
  - [POST /contactRequest/send](#1-contactrequestsend)
- [/auth](#auth)
  - [POST /auth/register](#1-authregister)
  - [POST /auth/login](#2-authlogin)
  - [POST /auth/me](#3-authme)
- [/serviceorder](#serviceorder)
  - [POST /serviceorder](#1-serviceorder)
  - [GET /serviceorder](#2-serviceorder)
  - [GET /serviceorder/{id}](#3-serviceorderid)
  - [GET /serviceorder/{id}/report](#4-serviceorderidreport)

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
  "lastName":"Silva",
  "workspaceName": "My workspace"
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

### 3. **/auth/me**

#### Descrição:
Endpoint para acessar detalhes do usuário autenticado pelo JWT
#### Requisição:
**URL**: `/auth/me`  
**Método**: `GET`

**Resposta**
```json
{
  "id": 10,
  "email": "collaborator3@example.com",
  "name": "Collaborator3",
  "lastname": "Name",
  "role": "COLLABORATOR",
  "workspaceId": 2,
  "workspaceIdentification": "Workspace2"
}
```

## /serviceorder

### 1. **/serviceorder**

#### Descrição:
Endpoint para abrir ordens de serviço!

#### Requisição:
**URL**: `/serviceorder`  
**Método**: `POST`

**Body**:
```json
{
  "description": "Meu monitor parou de funcionar!"
}
```

### 2. **/serviceorder**

#### Descrição:
Endpoint para listar ordens de serviço
Obs.: Collaborators apenas podem ver OS que eles mesmos abriram

#### Requisição:
**URL**: `/serviceorder`  
**Método**: `GET`

**Resposta**
```json
[
  {
    "id": 4,
    "soCode": "CH-00004",
    "description": "Impressora não funciona",
    "currentState": "CANCELED",
    "collaboratorName": "Collaborator3 Name",
    "supporterName": "Supporter2 Name"
  }
]
```

### 3. **/serviceorder/{id}**

#### Descrição:
Endpoint para detalhar um ordem de serviço

#### Requisição:
**URL**: `/serviceorder/{id}`  
**Método**: `GET`


**Resposta**
```json
{
  "id": 4,
  "soCode": "CH-00004",
  "description": "Impressora não funciona",
  "diagnostic": "Usuário pediu para cancelar",
  "assignee": {
    "id": 6,
    "email": "supporter2@example.com",
    "name": "Supporter2",
    "lastname": "Name",
    "role": "SUPPORTER",
    "workspaceId": 1,
    "workspaceIdentification": "Workspace1"
  },
  "solicitant": {
    "id": 10,
    "email": "collaborator3@example.com",
    "name": "Collaborator3",
    "lastname": "Name",
    "role": "COLLABORATOR",
    "workspaceId": 2,
    "workspaceIdentification": "Workspace2"
  },
  "states": [
    {
      "dateTime": "2025-06-24T13:17:27.037319",
      "type": "OPEN"
    },
    {
      "dateTime": "2025-06-25T13:17:27.037319",
      "type": "ASSIGNED"
    },
    {
      "dateTime": "2025-06-26T13:17:27.037319",
      "type": "CANCELED"
    }
  ],
  "currentState": "CANCELED"
}
```


### 4. **/serviceorder/{id}/report**

#### Descrição:
Endpoint para fazer download do relatório em PDF

#### Requisição:
**URL**: `/serviceorder/{id}/report`  
**Método**: `GET`


