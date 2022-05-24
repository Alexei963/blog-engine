package com.example.blog.service;

import com.example.blog.api.response.SettingsResponse;
import com.example.blog.model.GlobalSettings;
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
    GlobalSettings multiuserMode =
        settingsRepository.findGlobalSettingsByCode("MULTIUSER_MODE");
    GlobalSettings postPreModeration =
        settingsRepository.findGlobalSettingsByCode("POST_PREMODERATION");
    GlobalSettings statisticsIsPublic =
        settingsRepository.findGlobalSettingsByCode("STATISTICS_IS_PUBLIC");
    if (multiuserMode.getValue().equals("YES")) {
      settingsResponse.setMultiUserMode(true);
    }
    if (postPreModeration.getValue().equals("YES")) {
      settingsResponse.setPostPreModeration(true);
    }
    if (statisticsIsPublic.getValue().equals("YES")) {
      settingsResponse.setStatisticsIsPublic(true);
    }
    return settingsResponse;
  }
}
