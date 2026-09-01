package ATS.service;

import ATS.dao.UserDAO;
import ATS.model.User;

public class UserService {

    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public boolean registerCandidate(User user) {
        return userDAO.registerCandidate(user);
    }

    public User loginUser(String email, String password) {
        return userDAO.loginUser(email, password);
    }

    public User getUserByEmail(String email) {
        return userDAO.getUserByEmail(email);
    }
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }}
