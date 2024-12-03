# **ROBATA**

[![Licença MIT](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)  
[![Versão](https://img.shields.io/badge/version-0.1.2-brightgreen.svg)](https://semver.org/)  
[![Status do Projeto](https://img.shields.io/badge/status-em%20desenvolvimento-yellow.svg)]()

---

## Índice
* [Descrição do Projeto](#descrição)
* [Funcionalidades](#funcionalidades)
* [Tecnologias Utilizadas](#tecnologias-utilizadas)
* [Licença](#licença)
* [Como Executar o Projeto](#instalação-e-uso)
* [Autores do Projeto](#autores)

---

## **Descrição**
Robata é um Organizador Inteligente de Agenda que visa ajudar os usuários a gerenciar a agenda de seus negócios de maneira mais eficiente, automatizando o processo de organização de compromissos e tarefas.

---

## **Funcionalidades**
- ✅ ***[Funcionalidade 1: Fazer agendamentos no google calendar através de chatbot]***
- 🚀 ***[Funcionalidade futura: Remover agendamentos através do chatbot]***
- 🚀 ***[Funcionalidade futura: Verificar horários disponíveis através do chatbot]***

---

## **Tecnologias Utilizadas**
- **Front-end**: ***[HTML5 e CSS]***
- **Back-end**: ***[JAVA e JS]***
- **Banco de dados**: ***[MYSQL]***
- **APIs**: ***[Gemini API e Google Calendar API]***

---

## **Licença**
Este projeto está licenciado sob a **Licença MIT**. Consulte o arquivo ***[LICENSE](LICENSE)*** para mais detalhes.

---

## **Instalação e Uso**

### **Pré-requisitos**

#### **Certifique-se de ter instalado:**
- ***[Banco de dados (MYSQL)]***  
- ***[IDE (NETBEANS 22)]***
- ***[JDK 19]***
- ***[Glasfish 7.0]***

#### **Certifique-se de adquirir:**
- ***[credentials.json]:*** É necessário a criar através do Google Cloud: https://console.cloud.google.com/apis/credentials
- ***[Gemini API KEY]:*** Apenas gere Google AI Studio: https://aistudio.google.com/app/apikey

#### **Certifique-se de ter criado:**
```sql
CREATE DATABASE ROBATA;

CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    token VARCHAR(255),
    email_confirmado BOOLEAN DEFAULT FALSE);

```

#### **Uso:**

1. **Descompactação do Projeto**: Feito o download do projeto, descompacte o arquivo em formato ZIP.
2. **Abrir o Projeto no NetBeans**: Utilize o **NetBeans** para abrir e executar o projeto.
3. **Configuração do Banco de Dados**: Crie a databese e a tabela conforme indicado nos Pré-requisitos.
4. **Configuração do Ambiente**: Alterar **credentials.json**, insira a sua adquirida como dito anteriormente nos Pré-requisitos, após cessar as **variáveis de ambiente**, criar nova variável de sistema: **Nome da variável(GEMINI_API_KEY) Valor("Sua api key")** e por fim rodar a classe CalendarQuickstart e confirmar as permissões do Robata.
5. **Execução do Projeto**: Dê o build no projeto e já poderá executa-lo.

---
  
## **Autores**

* [Antônio Hiroky](https://github.com/AntonioUrata)
* [Bryan Lopes](https://github.com/BryanCSAL)
* [Yuri Galhego](https://github.com/Galhego)
