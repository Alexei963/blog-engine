package com.example.blog.repository;

import com.example.blog.model.GlobalSettings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Global Settings Repository.
 */

@Repository
public interface GlobalSettingsRepository extends CrudRepository<GlobalSettings, Integer> {

  @Query("SELECT s FROM GlobalSettings s where s.code = :code")
  GlobalSettings findGlobalSettingsByCode(@Param("code") String code);
}
