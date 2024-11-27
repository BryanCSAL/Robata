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

input[type="text"], 
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

.forgot-password {
    margin-top: 10px;
    font-size: 14px;
    text-align: center; /* Centralizar o texto */
}

.forgot-password a {
    color: #b6847d;
    text-decoration: none;
}

.forgot-password a:hover {
    text-decoration: underline;
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
</style>
</head>
<body>
<h1>Login</h1>
<form action="LoginServlet" method="post">
    <label for="username">Usuário:</label>
    <input type="text" id="username" name="username" required>
    
    <label for="password">Senha:</label>
    <input type="password" id="password" name="password" required>
    
    <input type="submit" value="Entrar">
</form>
<!-- Botão para voltar para a página de cadastro -->
<a href="index.jsp" class="button-back">Voltar para Cadastro</a>
</body>
</html>