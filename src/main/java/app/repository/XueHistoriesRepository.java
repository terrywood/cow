package app.repository;

import app.bean.XueHistories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by Administrator on 2016/7/25 0025.
 */
public interface XueHistoriesRepository extends JpaRepository<XueHistories, Long>, JpaSpecificationExecutor {
    List<XueHistories> findByRebalancing_id(Long rbid);
}
