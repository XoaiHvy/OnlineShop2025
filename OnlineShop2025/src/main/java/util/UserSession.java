package util;

import model.User; 

public class UserSession {

    private static UserSession instance;
    private User loggedInUser;

    private UserSession() {
       
    }

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public User getLoggedInUser() {
        return this.loggedInUser;
    }

    public void clearSession() {
        this.loggedInUser = null;
    }

    public boolean isLoggedIn() {
        return this.loggedInUser != null;
    }

    public int getLoggedInUserId() {
        if (isLoggedIn()) {
            return this.loggedInUser.getUserId();
        }
        
        System.err.println("UserSession: Attempted to get user ID when no user is logged in.");
        return -1; 
    }

    public String getLoggedInUsername() {
        if (isLoggedIn()) {
            return this.loggedInUser.getUsername();
        }
        return null;
    }
}