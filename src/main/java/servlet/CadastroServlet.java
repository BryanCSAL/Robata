package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet para registro de usuário
 */
@WebServlet(name = "CadastroServlett", urlPatterns = {"/CadastroServlet"})
public class CadastroServlet extends HttpServlet {

    // Método para processar o registro de usuário
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String nome = request.getParameter("username");
        String email = request.getParameter("email");
        String senha = request.getParameter("password");

        // Validação dos campos
        if (nome == null || nome.isEmpty() || email == null || email.isEmpty() || senha == null || senha.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Erro: Todos os campos são obrigatórios!");
            return;
        }

        // Normaliza o e-mail
        email = email.trim().toLowerCase();

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // Carrega as propriedades do banco
            Properties props = new Properties();
            InputStream input = getServletContext().getResourceAsStream("/WEB-INF/classes/db.properties");
            if (input == null) {
                throw new Exception("Arquivo db.properties não encontrado.");
            }
            props.load(input);

            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");
            String driver = props.getProperty("db.driver");

            // Conexão ao banco
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);

            // Insere os dados do usuário
            String sql = "INSERT INTO usuario (nome, email, senha) VALUES (?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, senha); 
            ps.executeUpdate();

            // Resposta ao cliente
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("Usuário registrado com sucesso!");

        } catch (Exception e) {
            // Registro do erro no log
            e.printStackTrace();

            // Resposta ao cliente
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erro ao registrar usuário: " + e.getMessage());
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("Use o método POST para registrar usuários.");
    }
}
