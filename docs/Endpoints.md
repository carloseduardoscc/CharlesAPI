[<< Voltar](../README.md)
# Endpoints da API

**Observação:** Por padrão a aplicação roda na porta 8080, se está rodando localmente as requisições deverão ter `localhost:8080/{endpoint}`

## Índice:

- [/contactRequest](#contactrequest)
  - [POST /contactRequest/send](#1-contactrequestsend)

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
  "name": "João Silva",
  "phone": "+5511999999999",
  "email": "joao.silva@example.com",
  "city": "São Paulo",
  "personType": "PF",
  "message": "Preciso de ajuda com minha conta."
}
