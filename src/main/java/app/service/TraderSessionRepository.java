package app.service;

import app.entity.Trader;
import app.entity.TraderSession;
import org.springframework.data.repository.CrudRepository;

public interface TraderSessionRepository extends CrudRepository<TraderSession, String> {

  //  List<AccountData> findByLastName(String lastName);
}
