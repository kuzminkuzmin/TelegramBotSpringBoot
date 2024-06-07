package com.dmitriikuzmin.service;

import com.dmitriikuzmin.model.Compliment;

import java.util.List;

public interface ComplimentService {
    Compliment add(Compliment compliment);

    Compliment get(long id);

    List<Compliment> getAllCompliments();

    Compliment getRandomCompliment(long chatId);

    Compliment update(Compliment compliment);


    /*private DataList dataList;
    private Map<Long, List<Compliment>> usedCompliments = new HashMap<>();

    @Autowired
    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }

    public List<Compliment> getAllCompliments() {
        return this.dataList.getCompliments();
    }

    public Compliment getRandom(long chatId) {
        List<Compliment> unused = dataList.getCompliments();
        List<Compliment> used = this.usedCompliments.getOrDefault(chatId, new ArrayList<>());
        unused.retainAll(used);
        if (unused.isEmpty()) {
            throw new IllegalArgumentException("Sorry, we have no compliment for you");
        } else {
            Compliment compliment = unused.get((int) (Math.random() * unused.size()));
            used.add(compliment);
            this.usedCompliments.put(chatId, used);
            return compliment;
        }
    }*/
}