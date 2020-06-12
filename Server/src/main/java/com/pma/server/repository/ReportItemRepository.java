package com.pma.server.repository;

import com.pma.server.model.ReportItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportItemRepository extends JpaRepository<ReportItem, Long> {

    ReportItem findReportItemById(Long id);
}
