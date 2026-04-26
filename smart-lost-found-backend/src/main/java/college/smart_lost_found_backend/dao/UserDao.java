package college.smart_lost_found_backend.dao;

import college.smart_lost_found_backend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    int save(User user);

    Optional<User> findById(Long userId);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    int update(User user);

    int deleteById(Long userId);

}
