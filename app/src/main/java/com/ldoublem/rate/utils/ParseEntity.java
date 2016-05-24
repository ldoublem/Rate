package com.ldoublem.rate.utils;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lumingmin on 16/4/20.
 */
public class ParseEntity<T>{


    public T parseOne(String s,Class<T> tclass) {
        Gson gson = new Gson();
        T res = gson.fromJson(s.toString(), tclass);
        return res;
    }
    public  <T> List<T> parse(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr);
    }

    public static String getJsonStringFromAssets(Context context,String path){

        String jsonstring_historyRecords = null;
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            while ((jsonstring_historyRecords = bufferedReader.readLine()) != null) {
                stringBuffer.append(jsonstring_historyRecords);
            }
            jsonstring_historyRecords = stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jsonstring_historyRecords;
    }


    //写入数据到data/data中
    public static void WriteToDataData(Context context,String fileName,String data) throws IOException {
        FileOutputStream fileOutputStream = context.openFileOutput(fileName,Context.MODE_PRIVATE);
        fileOutputStream.write(data.getBytes());
        fileOutputStream.close();
    }
}
