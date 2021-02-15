package com.example.core.result;

import com.example.core.annotation.ResultMethod;
import com.example.core.annotation.SpiderWork;
import com.example.core.annotation.WorkInit;
import com.example.core.annotation.WorkRelease;
import com.example.core.models.Result;
import com.example.core.utils.Constant;
import com.example.core.utils.D;
import com.google.gson.Gson;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.WeakHashMap;

@SpiderWork(name = DefaultFile.NAME)
public class DefaultFile {
    public static final String NAME = "core.DefaultFile";
    private Map<String, BufferedWriter> files = new WeakHashMap<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private Gson gson = new Gson();

    @WorkInit
    public void init() {
        D.d("DefaultFile==>init");
    }

    @WorkRelease
    public void release() {
        D.d("DefaultFile==>release");
    }

    @ResultMethod(methods = {"default_savefile"}, lock = true)
    public void writeFile(Result result) {
        String name = result.request.name + sdf.format(new Date());
        if (files.get(name) == null) {
            File dir = new File(Constant.RESULT_FILE_PATH + File.separator + result.request.name);
            if (!dir.exists())
                dir.mkdirs();
            String path = dir.getAbsolutePath() + File.separator + name;
            D.e("path==>" + path);

            BufferedWriter out = null;
            try {
                out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(path + ".txt", true)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            files.put(name, out);
        } else {
            try {
                files.get(name).write(result.request.url + "\n");
                files.get(name).write(gson.toJson(result.result) + "\n\n");
                files.get(name).flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    files.get(name).flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
