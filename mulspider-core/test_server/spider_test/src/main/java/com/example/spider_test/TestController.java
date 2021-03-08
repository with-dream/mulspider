package com.example.spider_test;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @RequestMapping(value = "/test/cate", method = RequestMethod.GET)
    public List<String> cate() {
        List<String> list = new ArrayList<>();
        list.add("c1");
        list.add("c2");
        list.add("c3");
        return list;
    }

    @RequestMapping(value = "/test/item/{cate}/{index}", method = RequestMethod.GET)
    public List<String> item(@PathVariable("cate") String cate, @PathVariable("index") Integer index) {
        switch (cate) {
            case "c1":
                if (index > 30)
                    index = 30;
                return createUrls(cate, index);
            case "c2":
                if (index > 50)
                    index = 50;
                return createUrls(cate, index);
            case "c3":
                if (index > 70)
                    index = 70;
                return createUrls(cate, index);
        }
        return null;
    }

    private List<String> createUrls(String cate, int index) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 5; i++)
            list.add(String.format("/test/info/%s_%d_%d", cate, index, i));
        return list;
    }

    @RequestMapping(value = "/test/info/{tag}", method = RequestMethod.GET)
    @ResponseBody
    public String info(@PathVariable("tag") String tag) {
        System.err.println("info==>" + tag);
        return "info-" + tag;
    }

    //127.0.0.1:8080/download/123
    @RequestMapping(value = "/download/{fileid}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable("fileid") String fileid)
            throws IOException {
        String filePath = "./file/123.mp4";
        FileSystemResource file = new FileSystemResource(filePath);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Content-Disposition", "fileName=" + fileid+".mp4");
        headers.add("Expires", "0");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.contentLength())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(file.getInputStream()));
    }
}
