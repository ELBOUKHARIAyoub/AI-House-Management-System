package org.example.householdledger.Session;

import org.example.householdledger.Model.user;

public class UserSession {
    private static user currentUser;
    private static boolean isLoggedIn = false;

    // Private constructor to prevent instantiation
    private UserSession() {}

    // Initialize/login user
    public static void init(user u) {
        if (u != null) {
            currentUser = u;
            isLoggedIn = true;
            System.out.println("User session initialized for user ID: " + u.getUserId());
        } else {
            throw new IllegalArgumentException("User cannot be null");
        }
    }

    // Alternative method name for login
    public static void login(user u) {
        init(u);
    }

    // Get current user
    public static user getCurrentUser() {
        return currentUser;
    }

    // Check if user is logged in
    public static boolean isLoggedIn() {
        return isLoggedIn && currentUser != null;
    }

    // Get current user ID (convenience method)
    public static int getCurrentUserId() {
        if (currentUser != null) {
            return currentUser.getUserId();
        }
        throw new IllegalStateException("No user is currently logged in");
    }

    // Check if current user matches given user ID
    public static boolean isCurrentUser(int userId) {
        return isLoggedIn() && currentUser.getUserId() == userId;
    }

    // Clear session/logout
    public static void clear() {
        if (currentUser != null) {
            System.out.println("Clearing session for user ID: " + currentUser.getUserId());
        }
        currentUser = null;
        isLoggedIn = false;
    }

    // Alternative method name for logout
    public static void logout() {
        clear();
    }

    // Update current user (useful for profile updates)
    public static void updateCurrentUser(user updatedUser) {
        if (isLoggedIn() && updatedUser != null &&
                currentUser.getUserId() == updatedUser.getUserId()) {
            currentUser = updatedUser;
            System.out.println("User session updated for user ID: " + updatedUser.getUserId());
        } else {
            throw new IllegalStateException("Cannot update user session: user not logged in or user ID mismatch");
        }
    }

    // Get user info as string (for debugging/logging)
    public static String getUserInfo() {
        if (isLoggedIn()) {
            return String.format("User ID: %d", currentUser.getUserId());
        }
        return "No user logged in";
    }

    // Validate session
    public static boolean validateSession() {
        return currentUser != null && isLoggedIn;
    }

    // Check if session exists (without throwing exception)
    public static boolean hasActiveSession() {
        return currentUser != null;
    }
}