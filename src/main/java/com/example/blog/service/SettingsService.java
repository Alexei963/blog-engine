package com.example.blog.service;

import com.example.blog.api.response.SettingsResponse;
import com.example.blog.repository.GlobalSettingsRepository;
import org.springframework.stereotype.Service;

/**
 * Settings Service.
 */

@Service
public class SettingsService {

  private final GlobalSettingsRepository settingsRepository;

  public SettingsService(GlobalSettingsRepository settingsRepository) {
    this.settingsRepository = settingsRepository;
  }

  public SettingsResponse getGlobalSettings() {
    SettingsResponse settingsResponse = new SettingsResponse();
    settingsResponse.setMultiUserMode(settingsRepository
        .findGlobalSettingsByCode("MULTIUSER_MODE").getValue().equals("YES"));
    settingsResponse.setPostPreModeration(settingsRepository
        .findGlobalSettingsByCode("POST_PREMODERATION").getValue().equals("YES"));
    settingsResponse.setStatisticsIsPublic(settingsRepository
        .findGlobalSettingsByCode("STATISTICS_IS_PUBLIC").getValue().equals("YES"));
    return settingsResponse;
  }
}
