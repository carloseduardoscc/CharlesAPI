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
  - [POST /serviceorder/{id}/report](#4-serviceorderidreport)
  - [POST /serviceorder/{id}/assign](#5-serviceorderidassign)
  - [POST /serviceorder/{id}/cancel](#6-serviceorderidcancel)
  - [POST /serviceorder/{id}/complete](#7-serviceorderidcomplete)
  - [POST /serviceorder/{id}/statistcs](#8-serviceorderstatistcs)
- [/participants](#participants)
  - [POST /participants](#1-participants)
  - [POST /participants](#1-participants)

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
Endpoint para listar ordens de serviço com suporte para filtros de data máxima, mínima e responsável 'assignee'
Obs.: Collaborators apenas podem ver OS que eles mesmos abriram

#### Requisição:
**URL**: `/serviceorder?maxDate=2025-07-06&minDate=2025-07-01&assignee=name`  
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

### 5. **/serviceorder/{id}/assign**

#### Descrição:
Endpoint para se responsabilizar numa ordem de serviço aberta

#### Requisição:
**URL**: `/serviceorder/{id}/assign`  
**Método**: `POST`

### 6. **/serviceorder/{id}/cancel**

#### Descrição:
Endpoint para cancelar uma os, permitido se:

Usuário abriu a ordem e ela ainda está aberta, 

Usuário se responsabilizou e ela ainda está em andamento

#### Requisição:
**URL**: `/serviceorder/{id}/cancel`  
**Método**: `POST`

### 7. **/serviceorder/{id}/complete**

#### Descrição:
Endpoint para completar uma os, permitido se:

Usuário se responsabilizou e ela ainda está em andamento

#### Requisição:
**URL**: `/serviceorder/{id}/complete`  
**Método**: `POST`

### 8. **/serviceorder/statistcs**

#### Descrição:
Endpoint para gerar estatísticas sobre as ordens de serviço

#### Requisição:
**URL**: `/serviceorder/statistcs`  
**Método**: `GET`

**Resposta**
```json
{
  "open": 0,
  "assigned": 1,
  "canceled": 1,
  "completed": 1,
  "closed": 2,
  "all": 3
}
```

## /participants

### 1. **/participants**

#### Descrição:
Endpoint para adicionar um novo participante, permitido se:

Owner pode adicionar admin, supporter e collaborator,

Admin pode adicionar supporter e collaborator

Outros cargos não podem adicionar participantes

#### Requisição:
**URL**: `/participants`  
**Método**: `POST`

**Body**:
```json
{
  "name": "NewParticipant",
  "lastName": "Test",
  "email": "newparticipant3@gmail.com",
  "password": "passwordtest123",
  "role": "OWNER"
}
```

**Resposta**
```json
{
  "email": "newparticipant3@gmail.com",
  "password": "passwordtest123"
}
```

### 1. **/participants**

#### Descrição:
Endpoint para listar participants do próprio workspace

#### Requisição:
**URL**: `/participants`  
**Método**: `GET`

**Resposta**
```json
[
  {
    "id": 2,
    "name": "Owner2 Name",
    "email": "owner2@example.com",
    "role": "OWNER",
    "isActive": true
  },
  {
    "id": 4,
    "name": "Admin2 Name",
    "email": "admin2@example.com",
    "role": "ADMIN",
    "isActive": true
  }
]
```
### 1. **/participants**

#### Descrição:
Endpoint para desativar um participante:

Owner pode adicionar admin, supporter e collaborator,

Admin pode adicionar supporter e collaborator

Outros cargos não podem adicionar participantes

#### Requisição:
**URL**: `/participants/{id}/deactivate`  
**Método**: `POST`