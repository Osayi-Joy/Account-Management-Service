package com.interswitchgroup.accountmanagementsystem.common.config.database;


import java.util.Optional;

import com.interswitchgroup.accountmanagementsystem.common.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.util.StringUtils;

/**
 * @author Joy Osayi
 * @createdOn April-12(Fri)-2024
 */
@Configuration
@RequiredArgsConstructor
public class EntityAuditorAware implements AuditorAware<String> {

  @Override
  public Optional<String> getCurrentAuditor() {
    String author = "SYSTEM";
    String loggedInUsername = CommonUtils.getLoggedInUsername();
    if (!StringUtils.isEmpty(loggedInUsername)) author = loggedInUsername;

    return Optional.of(author);
  }
}
