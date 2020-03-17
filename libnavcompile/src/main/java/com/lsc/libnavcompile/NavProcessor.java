package com.lsc.libnavcompile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.auto.service.AutoService;
import com.lsc.libnavannotation.ActivityNav;
import com.lsc.libnavannotation.FragmentNav;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;

/**
 * Created by lsc on 2020-03-17 08:38.
 * E-Mail:2965219926@qq.com
 *
 * Navgator注解处理器
 */

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"com.lsc.libnavannotation.FragmentNav","com.lsc.libnavannotation.ActivityNav"})
public class NavProcessor extends AbstractProcessor {

    private Messager mMessager;
    private Filer mFiler;

    private static final String OUTPUT_FILE_NAME = "destination.json";

    //初始化时调用
    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);

        mMessager = processingEnvironment.getMessager();
        mFiler = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        //通过处理器环境上下文roundEnv分别获取 项目中标记的FragmentDestination.class 和ActivityDestination.class注解。
        //此目的就是为了收集项目中哪些类 被注解标记了
        Set<? extends Element> fragmentNavs =  roundEnvironment.getElementsAnnotatedWith(FragmentNav.class);
        Set<? extends Element> activityNavs =  roundEnvironment.getElementsAnnotatedWith(ActivityNav.class);

        if(!fragmentNavs.isEmpty() || !activityNavs.isEmpty()){
            HashMap<String, JSONObject> navMap = new HashMap<>();
            handleNav(fragmentNavs,FragmentNav.class,navMap);
            handleNav(activityNavs,ActivityNav.class,navMap);

            // app/src/main/assets
            FileOutputStream fos = null;
            OutputStreamWriter writer = null;
            try{
                //filer.createResource()意思是创建源文件
                //我们可以指定为class文件输出的地方，
                //StandardLocation.CLASS_OUTPUT：java文件生成class文件的位置，/app/build/intermediates/javac/debug/classes/目录下
                //StandardLocation.SOURCE_OUTPUT：java文件的位置，一般在/ppjoke/app/build/generated/source/apt/目录下
                //StandardLocation.CLASS_PATH 和 StandardLocation.SOURCE_PATH用的不多，指的了这个参数，就要指定生成文件的pkg包名了
                FileObject resource = mFiler.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE_NAME);
                String resourcePath = resource.toUri().getPath();
                mMessager.printMessage(Diagnostic.Kind.NOTE, "resourcePath:" + resourcePath);

                //由于我们想要把json文件生成在app/src/main/assets/目录下,所以这里可以对字符串做一个截取，
                //以此便能准确获取项目在每个电脑上的 /app/src/main/assets/的路径
                String appPath = resourcePath.substring(0, resourcePath.indexOf("navigator") + 10);
                String assetsPath = appPath + "src/main/assets/";

                File file = new File(assetsPath);
                if (!file.exists()) {
                    file.mkdirs();
                }

                //此处就是稳健的写入了
                File outPutFile = new File(file, OUTPUT_FILE_NAME);
                if (outPutFile.exists()) {
                    outPutFile.delete();
                }
                outPutFile.createNewFile();

                //利用fastjson把收集到的所有的页面信息 转换成JSON格式的。并输出到文件中
                String content = JSON.toJSONString(navMap);
                fos = new FileOutputStream(outPutFile);
                writer = new OutputStreamWriter(fos, "UTF-8");
                writer.write(content);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }

    private void handleNav(Set<? extends Element> navs,
                           Class<? extends Annotation> annotationClazz,
                           HashMap<String, JSONObject> navMap) {

        for (Element element : navs){
            TypeElement typeElement = (TypeElement)element;
            String pageUrl = null;
            String clazzName = typeElement.getQualifiedName().toString(); //得到全类名
            int id = Math.abs(clazzName.hashCode());//有可能为负值
            boolean needLogin = false;
            boolean asStarter = false;
            boolean isActivity = false;

            Annotation annotation = typeElement.getAnnotation(annotationClazz);
            if (annotation instanceof FragmentNav){
                FragmentNav fragmentNav = (FragmentNav) annotation;
                pageUrl = fragmentNav.pageUrl();
                needLogin = fragmentNav.needLogin();
                asStarter = fragmentNav.asStarter();
                isActivity = false;
            }else if(annotation instanceof ActivityNav){
                ActivityNav activityNav = (ActivityNav) annotation;
                pageUrl = activityNav.pageUrl();
                needLogin = activityNav.needLogin();
                asStarter = activityNav.asStarter();
                isActivity = true;
            }

            if (navMap.containsKey(pageUrl)){
                mMessager.printMessage(Diagnostic.Kind.ERROR,"不同的页面不能使用相同的pageUrl："+ clazzName);
            }else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id",id);
                jsonObject.put("isActivity",isActivity);
                jsonObject.put("clazzName",clazzName);
                jsonObject.put("pageUrl",pageUrl);
                jsonObject.put("needLogin",needLogin);
                jsonObject.put("asStarter",asStarter);
                //将信息放入map中
                navMap.put(pageUrl,jsonObject);
            }
        }
    }
}
