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
 * Servlet para confirmar o registro do usuário.
 */
@WebServlet(name = "ConfirmacaoServlet", urlPatterns = {"/ConfirmacaoServlet"})
public class ConfirmacaoServlet extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");

        if (token == null || token.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Token inválido!");
            return;
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            // Carregar as propriedades do banco de dados
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

            // Registrar o driver JDBC e estabelecer a conexão
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);

            // Atualizar o status do usuário no banco
            String sql = "UPDATE usuario SET email_confirmado = TRUE WHERE token = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, token);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.sendRedirect("login.jsp?status=success&message=E-mail+confirmado+com+sucesso");// Redireciona para a página de login
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("Token inválido ou já utilizado.");
            }

        } catch (Exception e) {
            // Registrar o erro no log
            e.printStackTrace();

            // Enviar resposta de erro ao cliente
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Erro ao confirmar e-mail: " + e.getMessage());
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
