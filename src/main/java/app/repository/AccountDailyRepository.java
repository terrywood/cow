package app.repository;

import app.entity.AccountDaily;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface AccountDailyRepository extends JpaRepository<AccountDaily, Long>, JpaSpecificationExecutor {
    AccountDaily findByDayAndAccountID(Date day,Long accountID);

    @Query("select t from AccountDaily t where t.accountID=?1")
    List<AccountDaily> findByAccountID(Long accountID , Pageable pageable);
}
