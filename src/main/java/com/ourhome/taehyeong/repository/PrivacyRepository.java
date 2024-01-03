package com.ourhome.taehyeong.repository;

import com.ourhome.taehyeong.entities.Privacy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface PrivacyRepository extends JpaRepository<Privacy, Long> {

    List<Privacy> findPrivacyByName(String arg);

    @Query(value = "select i from Privacy i where i.university like %:arg%")
    List<Privacy> findPrivacyByUniversity(String arg);

    @Query(value = "select i from Privacy i where i.address like %:arg%")
    List<Privacy> findPrivacyByAddress(String arg);

    List<Privacy> findPrivacyByBirthBetween(Date start, Date end);

}
