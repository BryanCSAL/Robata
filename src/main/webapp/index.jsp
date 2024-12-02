<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Home</title>
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
            font-weight: bold;
            color: #d7a79e;
        }

        header .menu {
            display: flex;
            gap: 15px;
            margin-right: 50px; /* Move para a esquerda */
        }

        header .menu a {
            color: #d7a79e;
            text-decoration: none;
            font-size: 1em;
            font-weight: bold;
            transition: color 0.3s ease;
        }

        header .menu a:hover {
            color: #d7a79e;
        }

        main {
            display: flex;
            flex-direction: column;
            align-items: center;
            text-align: center;
            margin-top: 80px; /* Espaço abaixo do header */
        }

        .text-box {
            background-color: #ffffff;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 8px 15px rgba(0, 0, 0, 0.2);
            width: 100%;
            max-width: 600px;
            border: 1px solid #e4d5c7;
        }
    </style>
</head>
<body>
    <header>
        <div class="logo">Robata</div>
        <div class="menu">
            <a href="login.jsp">Login</a>
            <a href="cadastro.jsp">Cadastro</a>
        </div>
    </header>
    <main>
        <div class="text-box">
            <!-- Espaço para inserir o texto -->
            <h1>Bem-vindo à nossa plataforma!</h1>
            <p>Aqui você pode explorar várias funcionalidades incríveis. Faça login ou cadastre-se para começar.</p>
        </div>
    </main>
<%@ include file="WEB-INF/jspf/footer.jspf" %>
</body>
</html>
