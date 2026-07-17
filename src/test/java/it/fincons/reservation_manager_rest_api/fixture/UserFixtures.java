package it.fincons.reservation_manager_rest_api.fixture;

import it.fincons.reservation_manager_rest_api.model.User;

public class UserFixtures {

    public static User createValidUser(Long id) {
        return new User(id, "Mario Rossi", "mario.rossi@email.com");
    }

    public static User createSecondValidUser(Long id) {
        return new User(id, "Luigi Verdi", "luigi.verdi@email.com");
    }
}