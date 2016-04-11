package app.service;

import app.entity.StockListData;
import app.pojo.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StockListRepository extends CrudRepository<StockListData, Long> {

  //  List<Customer> findByLastName(String lastName);
}
