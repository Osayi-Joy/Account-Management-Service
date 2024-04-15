package com.interswitchgroup.accountmanagementsystem.authentication.dto.response;
/*
 * @author Oluwatobi Ogunwuyi
 * @createdOn Jun-26(Mon)-2023
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
  private String accessToken;
  private Map<String, Object> additionalInformation;
}
