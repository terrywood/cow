package app.repository;

import app.entity.HistoryData;
import app.entity.StockListData;
import org.springframework.data.repository.CrudRepository;

public interface HistoryDataRepository extends CrudRepository<HistoryData, Long> {

  //  List<AccountData> findByLastName(String lastName);
}
