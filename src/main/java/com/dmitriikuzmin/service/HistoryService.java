package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.History;

public interface HistoryService {
    History add(long chatId, History history);
}
