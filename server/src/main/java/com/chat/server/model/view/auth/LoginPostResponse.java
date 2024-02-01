package com.chat.server.model.view.auth;

import com.chat.server.model.view.account.AccountGetResponse;

public record LoginPostResponse(
        String accessToken,
        AccountGetResponse account) {
}
