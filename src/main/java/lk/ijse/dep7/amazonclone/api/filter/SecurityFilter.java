package lk.ijse.dep7.amazonclone.api.filter;

import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.dep7.amazonclone.dto.UserDTO;
import lk.ijse.dep7.amazonclone.service.UserService;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;

@WebFilter(filterName = "SecurityFilter", servletNames = {"order-servlet"})
public class SecurityFilter extends HttpFilter {

    @Resource(name= "java:comp/env/jdbc/amazon")
    private DataSource dataSource;

    public void init(FilterConfig config) throws ServletException {
    }

    public void destroy() {
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String authorization = req.getHeader("Authorization");

        if (!authorization.startsWith("Basic ")) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid login credentials");
            return;
        }

        String encodedCredential = authorization.replace("Basic ", "");

        if (encodedCredential.trim().isEmpty()) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid login credentials");
            return;
        }

        try {
            byte[] decodedByteArray = Base64.getDecoder().decode(encodedCredential);
            String decodedCredential = new String(decodedByteArray);
            String[] splitCredential = decodedCredential.split(":");

            if (splitCredential.length != 2) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid login credentials");
                return;
            }

            String userId = splitCredential[0];
            String password = splitCredential[1];

            try (Connection connection = dataSource.getConnection()) {
                UserService userService = new UserService(connection);
                UserDTO authenticatedUser = userService.authenticate(userId, password);

                if (authenticatedUser == null) {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid login credentials");
                } else {
                    req.setAttribute("userId", userId);
                    chain.doFilter(req, res);
                }

            } catch (SQLException ex) {
                throw new RuntimeException("Failed to obtain a connection", ex);
            }
        }catch (IllegalArgumentException e){
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid login credentials");
        }

    }
}
