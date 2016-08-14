package app.repository;

import app.entity.Fish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface FishRepository extends JpaRepository<Fish, String>, JpaSpecificationExecutor {
    List<Fish> findByDate(Date date);
}
