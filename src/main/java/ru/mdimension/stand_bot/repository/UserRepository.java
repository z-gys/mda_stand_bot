package ru.mdimension.stand_bot.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.mdimension.stand_bot.dto.User;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    User findByUserId(long userId);

    User findByLastName(String userName);

    User findByUserName(String userName);

    List<User> getByRole(String role);
}
