package app.repository;

import app.entity.AccountDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;

public interface AccountDailyRepository extends JpaRepository<AccountDaily, Long>, JpaSpecificationExecutor {
    AccountDaily findByDayAndAccountID(Date day,Long accountID);
}
