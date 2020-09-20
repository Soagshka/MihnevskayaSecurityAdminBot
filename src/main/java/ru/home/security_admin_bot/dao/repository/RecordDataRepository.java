package ru.home.security_admin_bot.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.home.security_admin_bot.dao.RecordDataEntity;

import java.util.List;

@Repository
public interface RecordDataRepository extends JpaRepository<RecordDataEntity, Long> {
    List<RecordDataEntity> findTop5ByOrderByIdDesc();

    List<RecordDataEntity> findTop5ByPhoneNumberOrderByIdDesc(String phoneNumber);

    List<RecordDataEntity> findTop5ByCarNumberOrderByIdDesc(String carNumber);
}
