package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.security.SecureRandom;

@WebServlet(name = "CadastroServlet", urlPatterns = {"/CadastroServlet"})
public class CadastroServlet extends HttpServlet {

    private String generateToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return java.util.Base64.getUrlEncoder().encodeToString(bytes);
    }

    private void sendConfirmationEmail(String email, String token) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "*");
        
        String username = "projetorobata@gmail.com";
        String password = "atyu vmgu txsp chgy";

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

       String link = "http://localhost:8080/Robata/ConfirmacaoServlet?token=" + token;


        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        message.setSubject("Confirmação de Registro");
        message.setText("Clique no link para confirmar seu registro: " + link);

        Transport.send(message);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String nome = request.getParameter("username");
        String email = request.getParameter("email");
        String senha = request.getParameter("password");

        if (nome == null || email == null || senha == null || nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Erro: Todos os campos são obrigatórios!");
            return;
        }

        email = email.trim().toLowerCase();
        String token = generateToken();

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // Carregar configurações do banco
            Properties props = new Properties();
            try (InputStream input = getServletContext().getResourceAsStream("/WEB-INF/classes/db.properties")) {
                if (input == null) {
                    throw new Exception("Arquivo db.properties não encontrado.");
                }
                props.load(input);
            }

            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");
            String driver = props.getProperty("db.driver");

            // Registrar driver e conectar ao banco
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);

            // Inserir dados no banco
            String sql = "INSERT INTO usuario (nome, email, senha, token) VALUES (?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, senha); // Senha salva diretamente, sem criptografia
            ps.setString(4, token);
            ps.executeUpdate();

            sendConfirmationEmail(email, token);
            response.getWriter().write("Registro realizado! Verifique seu e-mail para confirmar.");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Erro: " + e.getMessage());
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}

