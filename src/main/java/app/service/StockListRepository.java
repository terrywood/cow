package app.service;

import app.entity.StockListData;
import org.springframework.data.repository.CrudRepository;

public interface StockListRepository extends CrudRepository<StockListData, Long> {

  //  List<Customer> findByLastName(String lastName);
}
