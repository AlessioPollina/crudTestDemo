package co.develhope.crudTestDemo.Repositories;

import co.develhope.crudTestDemo.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
