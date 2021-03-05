package com.example.spider_test;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @RequestMapping(value = "/test/cate", method = RequestMethod.GET)
    @ResponseBody
    public List<String> cate() {
        List<String> list = new ArrayList<>();
        list.add("c1");
        list.add("c2");
        list.add("c3");
        return list;
    }

    @RequestMapping(value = "/test/item/{cate}/{index}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> item(@PathVariable("cate") String cate, @PathVariable("index") Integer index) {
        switch (cate) {
            case "c1":
                if (index > 30)
                    index = 30;
                return createUrls(cate, index);
            case "c2":
                if (index > 10)
                    index = 10;
                return createUrls(cate, index);
            case "c3":
                if (index > 20)
                    index = 20;
                return createUrls(cate, index);
        }
        return null;
    }

    private List<String> createUrls(String cate, int index) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            list.add(String.format("/test/info/%s--%d--%d", cate, index, i));
        return list;
    }

    @RequestMapping(value = "/test/info/{tag}", method = RequestMethod.GET)
    @ResponseBody
    public String info(@PathVariable("tag") String tag) {
        System.err.println("info==>" + tag);
        return "info-" + tag;
    }
}
