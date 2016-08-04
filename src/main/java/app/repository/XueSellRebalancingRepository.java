package app.repository;

import app.bean.XueSellRebalancing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Administrator on 2016/7/25 0025.
 */
public interface XueSellRebalancingRepository extends JpaRepository<XueSellRebalancing, Long>, JpaSpecificationExecutor {
    @Query("select t from XueSellRebalancing t where t.id=?1")
    XueSellRebalancing findXueSellRebalancingByPK(Long id);
}
