package app.repository;

import app.entity.StockListData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StockListRepository extends CrudRepository<StockListData, Long> {
    List<StockListData> findByAccountID(String accountID);

    //  List<AccountData> findByLastName(String lastName);
}
