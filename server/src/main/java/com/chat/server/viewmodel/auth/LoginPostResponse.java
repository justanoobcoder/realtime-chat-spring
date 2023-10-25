package com.chat.server.viewmodel.auth;

import com.chat.server.viewmodel.account.AccountGetResponse;

public record LoginPostResponse(
        String accessToken,
        AccountGetResponse account
) {
}
