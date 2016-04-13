package app.repository;

import app.entity.Trader;
import app.entity.TraderSession;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface TraderSessionRepository extends CrudRepository<TraderSession, String> {

    @Query("select o from TraderSession o")
    List<TraderSession> findAllData();

}
