package com.interswitchgroup.accountmanagementsystem.authentication.service;
/**
 * @author Joy Osayi
 * @createdOn Apr-13(Sat)-2024
 */
public interface LoginAttemptService {

    void verifyLoginAccess(String username, boolean credentialMatches);
}
