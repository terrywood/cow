package app.repository;

import app.entity.Fish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FishRepository extends JpaRepository<Fish, String>, JpaSpecificationExecutor {
/*    AccountDaily findByDayAndAccountID(Date day, Long accountID);

    @Query("select t from AccountDaily t where t.accountID=?1")
    List<AccountDaily> findByAccountID(Long accountID, Pageable pageable);*/

}
