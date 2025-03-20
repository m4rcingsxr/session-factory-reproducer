package com.example;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/test")
public class TestController {

    @Inject
    MockUserRepository repository;


    @GET
    public Uni<MockUser> test() {
        MockUser mockUser = new MockUser();
        mockUser.setName("test");

        return repository.persist(mockUser);
    }


}
