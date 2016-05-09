package app.repository;

import app.entity.AccountData;
import app.entity.TraderSession;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<AccountData, Long> {

   // List<AccountData> findBy (String lastName);

    @Query("select o from AccountData o where o.status=1")
    List<AccountData> findTrackAccount();

}
