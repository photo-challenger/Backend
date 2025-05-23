package com.photoChallenger.tripture.domain.point.repository;

import com.photoChallenger.tripture.domain.point.entity.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    @Query("select p from Point p where p.profile.profileId = :profileId and p.pointDate between :startDate and :endDate")
    Page<Point> findPointByProfile_ProfileIdWhereBetweenStartDateAndEndDate(Long profileId, LocalDate startDate, LocalDate endDate, Pageable pageable);

    Boolean existsPointByProfile_ProfileIdAndAndPointTitle(Long profileId, String pointTitle);

    List<Point> findPointByProfile_ProfileIdAndAndPointTitleAndAndPointDate(Long profileId, String pointTitle, LocalDate pointDate);
}
