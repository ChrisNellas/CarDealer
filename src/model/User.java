package model;

public record User(Long id, String name, String username, String password, Role role) {
}
