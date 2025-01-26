# **ROBATA**

[![Licença MIT](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/MIT)  
[![Versão](https://img.shields.io/badge/version-0.1.2-brightgreen.svg)](https://semver.org/)  
[![Status do Projeto](https://img.shields.io/badge/status-em%20desenvolvimento-yellow.svg)]()

[Leia em Portugês](README.pt_br.md)

---

## Índice
* [Description](#description)
* [Functions](#functions)
* [Technologies Used](#technologies-used)
* [License](#license)
* [How to install/use](#install-and-use)
* [Authors](#authors)

---

## **Description**
Robata is a smart agenda organizer designed to help users manage their business more efficiently by automating appointment scheduling.

---

## **Functions**
- ✅ ***[First Funciontion: Schedule appointments in Google Calendar through th chatbot]***
- 🚀 ***[Future Function: Remove appointments through the chatbot]***
- 🚀 ***[Future Function: Check available times through the chatbot]***

---

## **Technologies Used**
- **Front-end**: ***[HTML5 e CSS]***
- **Back-end**: ***[JAVA e JS]***
- **Database**: ***[MYSQL]***
- **APIs**: ***[Gemini API, Google Calendar API and Jakarta Mail API]***

---

## **License**
This project is licensed under the MIT License. See the ***[LICENSE](LICENSE)*** file for more details.

---

## **Install and Use**

### **Prerequisites**

#### **Make sure you have installed:**
- ***[Database (MYSQL)]***  
- ***[IDE (NETBEANS 22)]***
- ***[JDK 19]***
- ***[Glasfish 7.0]***

#### **Make sure to acquire:**
- ***[credentials.json]:*** Is necessary to acquire through Google Cloud: https://console.cloud.google.com/apis/credentials
- ***[Gemini API KEY]:*** Is necessary to acquire through Google AI Studio: https://aistudio.google.com/app/apikey

#### **Make sure to create:**
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

#### **Use:**

1. **Project Extraction**: After downloading the project, extract the ZIP file.  
2. **Open the Project in NetBeans**: Use **NetBeans** to open and run the project.  
3. **Database Configuration**: Create the database and table as specified in the prerequisites.  
4. **Environment Configuration**: Update **credentials.json**, inserting the one you obtained as mentioned in the prerequisites. Afterward, access the **environment variables**, create a new system variable: **Variable Name (GEMINI_API_KEY), Value ("Your API key")**, and finally, run the `CalendarQuickstart` class to confirm Robata permissions.  
5. **Project Execution**: Build the project, and it will be ready to run.  

---
  
## **Authors**

* [Antônio Hiroky](https://github.com/AntonioUrata)
* [Bryan Lopes](https://github.com/BryanCSAL)
* [Yuri Galhego](https://github.com/Galhego)
