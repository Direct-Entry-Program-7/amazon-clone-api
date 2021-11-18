package lk.ijse.dep7.amazonclone.api;

import jakarta.annotation.Resource;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.dep7.amazonclone.dto.UserDTO;
import lk.ijse.dep7.amazonclone.service.UserService;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(name = "UserServlet", urlPatterns = {"/users", "/login"})
public class UserServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/amazon")
    private DataSource dataSource;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        switch (request.getServletPath()) {

            case "/users":
                if (request.getContentType() == null || !request.getContentType().equalsIgnoreCase("application/json")) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid content type");
                    return;
                }

                try (Connection connection = dataSource.getConnection()) {
                    Jsonb jsonb = JsonbBuilder.create();
                    UserDTO user = jsonb.fromJson(request.getReader(), UserDTO.class);

                    if (user == null) {
                        throw new JsonbException("Invalid user");
                    } else if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
                        throw new JsonbException("User id can't be empty");
                    } else if (user.getUserId().trim().length() < 5) {
                        throw new JsonbException("User id should be at least 5 chars long");
                    } else if (user.getName() == null || user.getName().trim().isEmpty()) {
                        throw new JsonbException("User's name can't be empty");
                    } else if (!user.getName().trim().matches("[A-Za-z ]+")) {
                        throw new JsonbException("Invalid name");
                    } else if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                        throw new JsonbException("Password can't be empty");
                    } else if (user.getPassword().trim().length() < 5) {
                        throw new JsonbException("Password should be at lest 5 chars long");
                    } else if (user.getUserId().contains(":")) {
                        throw new JsonbException("User id can't contain colon");
                    }

                    UserService userService = new UserService(connection);
                    userService.saveUser(user);
                    response.setStatus(HttpServletResponse.SC_CREATED);

                } catch (JsonbException exp) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, exp.getMessage());
                } catch (SQLException exp) {
                    throw new RuntimeException("Failed to obtain a connection", exp);
                }
                break;


            case "/login":
                if (request.getContentType() == null || !request.getContentType().startsWith("application/x-www-form-urlencoded")) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid content type");
                    return;
                }

                String userId = request.getParameter("userId");
                String password = request.getParameter("password");

                if (userId == null || userId.trim().isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "User id can't be empty");
                } else if (password == null || password.trim().isEmpty()) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Password can't be empty");
                } else {

                    try (Connection connection = dataSource.getConnection()) {
                        UserService userService = new UserService(connection);
                        UserDTO authenticatedUser = userService.authenticate(userId, password);

                        if (authenticatedUser == null) {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid login credentials");
                        } else {
                            response.setContentType("application/json");
                            response.getWriter().println(JsonbBuilder.create().toJson(authenticatedUser));
                        }

                    } catch (SQLException exp) {
                        throw new RuntimeException("Failed to obtain a connection", exp);
                    }

                }
        }

    }
}
