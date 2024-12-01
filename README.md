# **ROBATA**

[![Licença MIT](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)  
[![Versão](https://img.shields.io/badge/version-0.1.2-brightgreen.svg)](https://semver.org/)  
[![Status do Projeto](https://img.shields.io/badge/status-em%20desenvolvimento-yellow.svg)]()

---

## **Descrição**
Robata é um Organizador Inteligente de Agenda que visa ajudar os usuários a gerenciar a agenda de seus negócios de maneira mais eficiente, automatizando o processo de organização de compromissos e tarefas.

---

## **Funcionalidades**
- ✅ [Funcionalidade 1: Fazer agendamentos no google calendar através de chatbot]
- 🚀 [Funcionalidade futura: Remover agendamentos através do chatbot]
- 🚀 [Funcionalidade futura: Verificar horários disponíveis através do chatbot]

---

## **Tecnologias Utilizadas**
- **Front-end**: [HTML5, CSS e JS]
- **Back-end**: [JAVA]
- **Banco de dados**: [MYSQL]
- **Outras ferramentas**: [...]

---

## **Licença**
Este projeto está licenciado sob a **Licença MIT**. Consulte o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## **Instalação e Uso**

### **Pré-requisitos**

#### **Certifique-se de ter instalado:**
- [Banco de dados (MYSQL)]  
- [IDE (NETBEANS 22)]
- [JDK 19]
- [Glasfish 7.0]

#### **Certifique-se de adquirir:**
- [credentials.json] (É necessário a criar através do Google Cloud: https://console.cloud.google.com/apis/credentials)
- [Gemini API KEY] (Apenas gere Google AI Studio: https://aistudio.google.com/app/apikey)
- [Driver Banco de dados] (Apenas copie do Maven Repository: https://mvnrepository.com/artifact/mysql/mysql-connector-java)

#### **Certifique-se de ter criado:**
```sql
CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL
);

INSERT INTO usuario (nome, email, senha)
VALUES ('Teste', 'teste@gmail.com','1234');
```
