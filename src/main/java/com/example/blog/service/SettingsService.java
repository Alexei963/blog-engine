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

  private static final String MULTIUSER_MODE = "MULTIUSER_MODE";
  private static final String POST_PREMODERATION = "POST_PREMODERATION";
  private static final String STATISTICS_IS_PUBLIC = "STATISTICS_IS_PUBLIC";

  public SettingsService(GlobalSettingsRepository settingsRepository) {
    this.settingsRepository = settingsRepository;
  }

  public SettingsResponse getGlobalSettings() {
    SettingsResponse settingsResponse = new SettingsResponse();
    settingsResponse.setMultiUserMode(settingsRepository
        .findGlobalSettingsByCode(MULTIUSER_MODE).getValue().equals("YES"));
    settingsResponse.setPostPreModeration(settingsRepository
        .findGlobalSettingsByCode(POST_PREMODERATION).getValue().equals("YES"));
    settingsResponse.setStatisticsIsPublic(settingsRepository
        .findGlobalSettingsByCode(STATISTICS_IS_PUBLIC).getValue().equals("YES"));
    return settingsResponse;
  }

  public void saveSettings(SettingsRequest settingsRequest) {
    Iterable<GlobalSettings> settings = settingsRepository.findAll();
    settings.forEach(s -> {
      if (s.getCode().equals(MULTIUSER_MODE)) {
        String settingsValue = settingsRequest.isMultiUserMode() ? "YES" : "NO";
        s.setValue(settingsValue);
        settingsRepository.save(s);
      }
      if (s.getCode().equals(POST_PREMODERATION)) {
        String settingsValue = settingsRequest.isPostPreModeration() ? "YES" : "NO";
        s.setValue(settingsValue);
        settingsRepository.save(s);
      }
      if (s.getCode().equals(STATISTICS_IS_PUBLIC)) {
        String settingsValue = settingsRequest.isStatisticsIsPublic() ? "YES" : "NO";
        s.setValue(settingsValue);
        settingsRepository.save(s);
      }
    });
  }

  public boolean statisticsIsPublicSetting() {
    return settingsRepository
        .findGlobalSettingsByCode(STATISTICS_IS_PUBLIC).getValue().equals("YES");
  }
}
