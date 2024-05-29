package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.Compliment;
import com.dmitriikuzmin.model.DataList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ComplimentService {
    private DataList dataList;
    private Map<Integer, List<Compliment>> usedCompliments;

    @Autowired
    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }

    public List<Compliment> get() {
        return this.dataList.getCompliments();
    }
}