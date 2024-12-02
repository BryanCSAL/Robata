<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<style>
body {
    background-color: #f8f3e9; /* Fundo com cor quente e clara */
    color: #4b3832; /* Texto com tom terroso */
    font-family: 'Georgia', serif; /* Fonte elegante */
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 100vh;
    margin: 0;
}

h1 {
    color: #d7a79e; /* Título em tom rosado elegante */
    font-size: 2.5em;
    margin-bottom: 20px;
}

form {
    background-color: #ffffff; /* Fundo branco limpo para contraste */
    padding: 30px;
    border-radius: 15px;
    box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2); /* Sombra suave */
    width: 100%;
    max-width: 400px;
    margin-bottom: 20px;
    border: 1px solid #e4d5c7; /* Borda com tom pastel */
    display: flex;
    flex-direction: column; /* Garantir alinhamento vertical */
    gap: 15px; /* Espaçamento uniforme entre os elementos */
}

label {
    font-size: 1em;
    font-weight: bold;
    color: #6d5a56; /* Cor suave para os rótulos */
}

input[type="email"], 
input[type="password"] {
    padding: 10px;
    border: 1px solid #e4d5c7;
    border-radius: 10px;
    background-color: #fef8f5;
    color: #4b3832;
    font-size: 1em;
    width: 100%; /* Garantir que todos os campos ocupem o mesmo espaço */
}

input[type="submit"] {
    background-color: #d7a79e; /* Botão com cor vibrante */
    color: white;
    border: none;
    border-radius: 10px;
    padding: 12px;
    cursor: pointer;
    font-weight: bold;
    font-size: 1em;
    text-align: center;
    display: block;
    width: 100%;
    transition: background-color 0.3s ease;
}

input[type="submit"]:hover {
    background-color: #b6847d; /* Efeito de hover mais escuro */
}

.button-back {
    background-color: #d7a79e; /* Botão com cor vibrante */
    color: white;
    text-decoration: none;
    border-radius: 10px;
    padding: 10px 20px;
    font-size: 1em;
    margin-top: 20px;
    display: inline-block;
    text-align: center;
    transition: background-color 0.3s ease;
}

.button-back:hover {
    background-color: #b6847d; /* Efeito de hover mais escuro */
}

 header {
            width: 100%;
            padding: 15px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            position: absolute;
            top: 0;
            left: 0;
            background-color: #ffffff;
            border-bottom: 1px solid #e4d5c7;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
 header .logo {
            font-size: 1.8em;
        }
        
 header .logo a {
            text-decoration: none; /* Remove o sublinhado do link */
            color: #d7a79e; /* Cor rosada elegante */
            font-weight: bold; /* Deixa o texto mais destacado */
            cursor: pointer; /* Mantém o cursor de link */
            transition: color 0.3s ease; /* Adiciona um efeito suave na mudança de cor */
        }

        header .logo a:hover {
            color: #4b3832;
        }
</style>
</head>
<body>
<header>
      <div class="logo"><a href="index.jsp">Robata</a></div>
</header>
   
<h1>Login</h1>
<form action="${pageContext.request.contextPath}/LoginServlet" method="post">
    <label for="email">Email:</label>
    <input type="email" id="email" name="email" required>
    
    <label for="password">Senha:</label>
    <input type="password" id="password" name="password" required>
    
    <input type="submit" value="Entrar">
</form>
    
<% 
    String status = request.getParameter("status");
    String message = request.getParameter("message");
    if ("success".equals(status)) {
%>
    <p style="color: #d7a79e;"><%= message %></p>
<% 
    }
%>
    
<!-- Botão para voltar para a página de cadastro -->
<a href="cadastro.jsp" class="button-back">Voltar para Cadastro</a>
</body>
</html>

