package com.chat.server.viewmodel.error;

import java.util.ArrayList;
import java.util.List;

public record ErrorVm(int statusCode, String title, String detail, List<String> fields) {
    public ErrorVm(int statusCode, String title, String detail) {
        this(statusCode, title, detail, new ArrayList<>());
    }
}
