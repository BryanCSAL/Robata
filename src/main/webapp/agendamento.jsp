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
        header .perfil {
            font-size: 1.8em;
            display: flex;
        }

        header .perfil a {
            text-decoration: none; /* Remove o sublinhado do link */
            color: #d7a79e; /* Cor rosa*/
            font-weight: bold; /* Deixa o texto mais destacado */
            cursor: pointer; /* Mantém o cursor de link */
            transition: color 0.3s ease; /* Adiciona um efeito suave na mudança de cor */
        }

        header .perfil a:hover {
            color: #4b3832;
        }

    </style>
</head>
<body>
    <header>
        <div class="perfil"><a href="perfil.jsp">Perfil</a></div>
    </header>
    <div class="chat-container">
        <h1>Agendamento Robata</h1>
        <div class="chat-box" id="chat-box">
            <p><strong>Rob:</strong> Olá! Como posso ajudar você hoje?</p>
        </div>
        <div class="input-container">
            <textarea id="user-input" placeholder="Digite sua mensagem aqui..."></textarea>
            <button onclick="sendMessage()">Enviar</button>
        </div>
    </div>

    <script>
        function sendMessage() {
            const userInput = document.getElementById("user-input").value;
            const chatBox = document.getElementById("chat-box");

            if (userInput.trim() !== "") {
                const userMessage = document.createElement("div");
                userMessage.innerHTML = '<p><strong>Você: </strong>' +  userInput + '</p>';
                chatBox.appendChild(userMessage);

                sendToServlet(userInput);

                document.getElementById("user-input").value = "";
                chatBox.scrollTop = chatBox.scrollHeight;
            }
        }

        function sendToServlet(userInput) {
            fetch("AgendamentoServlet", {
                method: "POST",
                headers: { "Content-Type": "application/x-www-form-urlencoded" },
                body: "userPrompt=" + encodeURIComponent(userInput)
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Erro no servidor: " + response.status);
                    }
                    return response.text();
                })
                .then(result => {
                    const chatBox = document.getElementById("chat-box");
                    const botMessage = document.createElement("div");
                    botMessage.innerHTML = `<p><strong>Rob:</strong>`  + result + `</p>`;
                    chatBox.appendChild(botMessage);
                    chatBox.scrollTop = chatBox.scrollHeight;
                })
                .catch(error => {
                    console.error("Erro ao enviar ao servlet:", error);
                    const chatBox = document.getElementById("chat-box");
                    const botMessage = document.createElement("div");
                    botMessage.innerHTML = `<p><strong>Rob:</strong> Desculpe, ocorreu um erro. Tente novamente mais tarde. ` + error + `</p>`;
                    chatBox.appendChild(botMessage);
                    chatBox.scrollTop = chatBox.scrollHeight;
                });
        }
    </script>
</body>
</html>
