package com.example;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MockUserRepository implements PanacheRepository<MockUser> {
}
