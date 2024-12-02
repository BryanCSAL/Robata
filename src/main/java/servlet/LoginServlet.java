package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Captura os parâmetros do formulário
        String email = request.getParameter("email");
        String senha = request.getParameter("password");

        System.out.println("Email recebido: " + email);
        System.out.println("Senha recebida: " + senha);

        // Validação dos campos
        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            response.sendRedirect("login.jsp?error=empty");
            return;
        }

        // Normaliza o e-mail
        email = email.trim().toLowerCase();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Carrega as propriedades de conexão ao banco
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

            // Conecta ao banco
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);

            // Consulta SQL para buscar o usuário
            String sql = "SELECT nome, id, senha FROM usuario WHERE email = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, email);
            rs = ps.executeQuery();

            if (rs.next()) {
                String senhaBanco = rs.getString("senha");

                // Valida a senha diretamente (sem hash)
                if (senha.equals(senhaBanco)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("usuarioLogado", rs.getString("nome"));
                    session.setAttribute("idUsuario", rs.getString("id"));
                    session.setAttribute("usuarioEmail", email);
                    
                    response.sendRedirect("agendamento.jsp");
                } else {
                    response.sendRedirect("login.jsp?error=invalid");
                }
            } else {
                response.sendRedirect("login.jsp?error=invalid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=exception");
        } finally {
            // Fecha os recursos
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null && !conn.isClosed()) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("login.jsp");
    }

    @Override
    public String getServletInfo() {
        return "Servlet para login de usuários.";
    }
}
