package app.repository;

import app.entity.Fish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface FishRepository extends JpaRepository<Fish, String>, JpaSpecificationExecutor {
    List<Fish> findByDate(Date date);

    @Query("select t from Fish t where t.symbol=?1 order by t.date asc")
    List<Fish> findBySymbol(String symbol);
}
