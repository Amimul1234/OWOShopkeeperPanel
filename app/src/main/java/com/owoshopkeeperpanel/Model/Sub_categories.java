package com.owoshopkeeperpanel.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Sub_categories {

    private List<HashMap<String, String>> sub_categories = new ArrayList<>();

    public Sub_categories() {
    }

    public void add(HashMap<String, String> hash)
    {
        sub_categories.add(hash);
    }

    public Sub_categories(List<HashMap<String, String>> sub_categories) {
        this.sub_categories = sub_categories;
    }

    public List<HashMap<String, String>> getSub_categories() {
        return sub_categories;
    }

    public void setSub_categories(List<HashMap<String, String>> sub_categories) {
        this.sub_categories = sub_categories;
    }
}
