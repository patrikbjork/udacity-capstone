package bjork.udacity.capstone.repository;

import bjork.udacity.capstone.domain.MyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestRepository extends JpaRepository<MyEntity, String> {
}
