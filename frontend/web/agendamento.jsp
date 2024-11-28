<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chatbot</title>
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
            font-size: 1.5em;
            margin-bottom: 20px;
        }

        .chat-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 15px;
        }

        .chat-box {
            background-color: #ffffff;
            border: 1px solid #e4d5c7; /* Borda em tom pastel */
            border-radius: 10px;
            width: 100%;
            max-width: 600px;
            height: 300px; /* Altura fixa */
            overflow-y: auto; /* Rolagem se necessário */
            padding: 15px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Sombra leve */
        }

        .input-container {
            background-color: #ffffff;
            border: 1px solid #e4d5c7; /* Borda em tom pastel */
            border-radius: 10px;
            width: 100%;
            max-width: 600px;
            padding: 15px;
            display: flex;
            gap: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* Sombra leve */
        }

        .input-container textarea {
            flex: 1;
            border: 1px solid #e4d5c7;
            border-radius: 10px;
            padding: 10px;
            font-size: 1em;
            background-color: #fef8f5;
            color: #4b3832;
            resize: none; /* Desativa o redimensionamento */
            height: 50px;
        }

        .input-container button {
            background-color: #d7a79e;
            color: white;
            border: none;
            border-radius: 10px;
            padding: 12px 20px;
            cursor: pointer;
            font-weight: bold;
            font-size: 1em;
            transition: background-color 0.3s ease;
        }

        .input-container button:hover {
            background-color: #b6847d; /* Cor de hover */
        }
    </style>
</head>
<body>
    <div class="chat-container">
        <h1>Agendamento Robata</h1>
        <!-- Caixa de mensagens -->
        <div class="chat-box" id="chat-box">
            <!-- Exemplo de mensagens -->
            <p><strong>Rob:</strong> Olá! Como posso ajudar você hoje?</p>
        </div>
        <!-- Caixa de entrada -->
        <div class="input-container">
            <textarea id="user-input" placeholder="Digite sua mensagem aqui..."></textarea>
            <button onclick="sendMessage()">Enviar</button>
        </div>
    </div>

    <script>
        function sendMessage() {
            // Obtém o valor digitado pelo usuário
            const userInput = document.getElementById("user-input").value;
            const chatBox = document.getElementById("chat-box");

            // Verifica se a entrada não está vazia
            if (userInput.trim() !== "") {
                // Adiciona a mensagem do usuário ao chat
                const userMessage = document.createElement("div");
                userMessage.innerHTML = '<p><strong>Você: </strong>' +  userInput + '</p>';
                chatBox.appendChild(userMessage);


                // Limpa o campo de entrada
                document.getElementById("user-input").value = "";

                // Faz o scroll automático para a última mensagem
                chatBox.scrollTop = chatBox.scrollHeight;
            }
        }
    </script>
</body>
</html>
