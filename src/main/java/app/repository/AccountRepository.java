package app.repository;

import app.entity.AccountData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository  extends JpaRepository<AccountData, Long>, JpaSpecificationExecutor {

   // List<AccountData> findBy (String lastName);

    @Query("select o from AccountData o where o.status=1")
    List<AccountData> findTrackAccount();

}
