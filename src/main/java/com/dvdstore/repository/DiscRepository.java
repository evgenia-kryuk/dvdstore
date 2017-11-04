package com.dvdstore.repository;

import com.dvdstore.model.Disc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiscRepository extends JpaRepository<Disc, Long> {
    @Query("SELECT d FROM Disc d LEFT JOIN FETCH d.owner where d.owner.id=:id")
    List<Disc> findCurrentUserOwnDiscs(@Param("id") int id);

    @Query("SELECT d FROM Disc d where d.id=:id")
    Disc findById(@Param("id") int id);

}
