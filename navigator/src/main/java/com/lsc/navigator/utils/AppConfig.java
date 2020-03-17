package com.lsc.navigator.utils;

import android.content.res.AssetManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.lsc.navigator.model.BottomBar;
import com.lsc.navigator.model.Nav;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by lsc on 2020-03-17 10:12.
 * E-Mail:2965219926@qq.com
 *
 * 解析nav json文件
 * 解析bottombar json文件
 */
public class AppConfig {

    private static HashMap<String, Nav> mNavConfigs;

    private static BottomBar mBottomBar;

    public static HashMap<String,Nav> getNavConfigs(){

        if (mNavConfigs == null){

            String content = parseAssetsFile("destination.json");

            mNavConfigs = JSON.parseObject(content,
                    new TypeReference<HashMap<String,Nav>>(){}.getType());
        }

        return mNavConfigs;
    }

    public static BottomBar getBottomBar(){

        if (mBottomBar == null){
            String content = parseAssetsFile("main_tabs_config.json");
            mBottomBar = JSON.parseObject(content,BottomBar.class);
        }
        return mBottomBar;
    }

    /**
     * 读取Assets目录下的文件
     *
     * @param fileName
     * @return
     */
    private static String parseAssetsFile(String fileName){

        AssetManager assetManager = AppGlobals.getApplication().getResources().getAssets();

        InputStream stream = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try{
            stream = assetManager.open(fileName);
            reader = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            while ((line = reader.readLine()) != null){
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if (stream != null){
                    stream.close();
                }
                if (reader != null){
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }
}
