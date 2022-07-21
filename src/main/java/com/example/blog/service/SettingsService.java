package com.example.blog.service;

import com.example.blog.api.request.SettingsRequest;
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
    settingsResponse.setMultiUserMode(settingsRepository
        .findGlobalSettingsByCode("MULTIUSER_MODE").getValue().equals("YES"));
    settingsResponse.setPostPreModeration(settingsRepository
        .findGlobalSettingsByCode("POST_PREMODERATION").getValue().equals("YES"));
    settingsResponse.setStatisticsIsPublic(settingsRepository
        .findGlobalSettingsByCode("STATISTICS_IS_PUBLIC").getValue().equals("YES"));
    return settingsResponse;
  }

  public void saveSettings(SettingsRequest settingsRequest) {
    Iterable<GlobalSettings> settings = settingsRepository.findAll();
    settings.forEach(s -> {
      if (s.getCode().equals("MULTIUSER_MODE")) {
        if (settingsRequest.isMultiUserMode()) {
          s.setValue("YES");
        } else {
          s.setValue("NO");
        }
        settingsRepository.save(s);
      }
      if (s.getCode().equals("POST_PREMODERATION")) {
        if (settingsRequest.isPostPreModeration()) {
          s.setValue("YES");
        } else {
          s.setValue("NO");
        }
        settingsRepository.save(s);
      }
      if (s.getCode().equals("STATISTICS_IS_PUBLIC")) {
        if (settingsRequest.isStatisticsIsPublic()) {
          s.setValue("YES");
        } else {
          s.setValue("NO");
        }
        settingsRepository.save(s);
      }
    });
  }
}
