package app.repository;

import app.entity.HistoryData;
import app.entity.Trader;
import org.springframework.data.repository.CrudRepository;

public interface TraderRepository extends CrudRepository<Trader, Long> {

  //  List<AccountData> findByLastName(String lastName);
}
