package app.service;

import app.entity.AccountData;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<AccountData, Long> {

   // List<AccountData> findBy (String lastName);
}
