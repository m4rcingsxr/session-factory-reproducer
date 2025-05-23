package com.example;

import io.quarkus.test.junit.QuarkusTest;
import org.hibernate.cfg.Configuration;
import org.hibernate.reactive.mutiny.Mutiny;
import org.hibernate.reactive.provider.ReactiveServiceRegistryBuilder;
import org.junit.jupiter.api.Test;

import static org.hibernate.cfg.JdbcSettings.FORMAT_SQL;
import static org.hibernate.cfg.JdbcSettings.HIGHLIGHT_SQL;
import static org.hibernate.cfg.JdbcSettings.PASS;
import static org.hibernate.cfg.JdbcSettings.SHOW_SQL;
import static org.hibernate.cfg.JdbcSettings.URL;
import static org.hibernate.cfg.JdbcSettings.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class TestControllerTest {

    final Mutiny.SessionFactory factory = buildFactory();

    private Mutiny.SessionFactory buildFactory() {
        Configuration configuration = new Configuration()
                .addAnnotatedClass(MockUser.class)
                .setProperty(URL, "jdbc:postgresql://localhost:5432/postgres")
                .setProperty(USER, "quarkus")
                .setProperty(PASS, "quarkus")
                .setProperty("hibernate.agroal.maxSize", 20)
                .setProperty(SHOW_SQL, true)
                .setProperty(FORMAT_SQL, true)
                .setProperty(HIGHLIGHT_SQL, true);


        return configuration.buildSessionFactory(
                        new ReactiveServiceRegistryBuilder()
                                .applySettings(configuration.getProperties())
                                .build()
                )
                .unwrap(Mutiny.SessionFactory.class);
    }

    @Test
    void test() {
        MockUser mockUser = new MockUser();
        mockUser.setName("test");
        factory.withStatelessTransaction(session -> session.insert(mockUser)).await().indefinitely();
        MockUser user = factory.withStatelessTransaction(
                session -> session.get(MockUser.class, 1L)).await().indefinitely();
        assertEquals("test", user.getName());
    }


}