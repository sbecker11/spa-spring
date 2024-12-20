package com.spexture.model;

import java.util.Objects;
import org.apache.hadoop.hbase.util.Bytes;

public class User {
    private String id;
    private String email;
    private String password;

    // Constructors, getters, setters

    public User() {}

    public User(String id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; } // Note: In real apps, return hashed password or don't expose this

    public byte[] getIdAsBytes() {
        return Bytes.toBytes(id);
    }

    public static User fromBytes(byte[] rowKey, byte[] emailBytes, byte[] passwordBytes) {
        User user = new User();
        user.id = Bytes.toString(rowKey);
        user.email = Bytes.toString(emailBytes);
        user.password = Bytes.toString(passwordBytes);
        return user;
    }
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", password='[PROTECTED]'}" +
                '}';
    }
    

    public void setPassword(String password) {
        // In real scenarios, you'd hash the password here before setting it.
        this.password = password; // Placeholder for hash implementation
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}