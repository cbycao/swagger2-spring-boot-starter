/**
 * Copyright: Copyright(C) 2020 Easy-Java-Rest-Framework.
 */

package io.github.swagger2.autoconfigure.utils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类  .
 *
 * <p>
 * 工具类
 *
 * @author caobaoyu
 * @date 2020/4/5 16:49
 */
public class Utils {
    private static final Pattern PATTERN_PACKAGE_REGULAR = Pattern.compile("^([\\s\\S]*)[/]\\*[\\s\\S]*$");
    /**
     * 一个字符串包含另外一个字符串出现的次数 .
     *
     * <p>
     * 一个字符串包含另外一个字符串出现的次数
     *
     * @param str       字符串
     * @param contained 被包含的字符串
     * @return int 次数
     * @author caobaoyu
     * @date 2020/4/5 16:56
     */
    public static int containCount(String str, String contained) {
        int count = 0;
        if (str != null && contained != null) {
            int index = 0;
            index = str.indexOf(contained);
            while (index != -1) {
                count++;
                index = str.indexOf(contained, index + 1);
            }
        }
        return count;
    }

    /**
     * 将配置带有统配符的包扫描转换为不带统配符的包路径 .
     *
     * <p>
     * 将配置带有统配符的包扫描转换为不带统配符的包路径
     *
     * @param classLoader ClassLoader
     * @param packageNameList 包扫描配置List
     * @return java.util.List<java.lang.String> 带统配符的包路径List
     * @author caobaoyu
     * @date 2020/4/6 14:45
     */
    public static List<String> getPackagePaths(ClassLoader classLoader,List<String> packageNameList) throws Exception{
        List<String> realPackageList =  new ArrayList<>();
        if(packageNameList!=null){
            for (String packageName:packageNameList){
                if(packageName!=null){
                    int starCount = containCount(packageName,"*");
                    if(starCount==0){
                        realPackageList.add(packageName);
                    }
                    if(starCount==1){
                        List<String> nameList = findPackageList(classLoader,PATTERN_PACKAGE_REGULAR,packageName);
                        realPackageList.addAll(nameList);
                    }
                    if(starCount>1){
                        throw new Exception("包扫描不支持多个*");
                    }
                }
            }
        }
        return realPackageList;
    }

    /**
     * 解析统配符前面的基础包路径 .
     *
     * <p>
     * 解析统配符前面的基础包路径
     *
     * @param pattern Pattern
     * @param value   包路径，带统配符
     * @return java.lang.String
     * @author caobaoyu
     * @date 2020/4/6 14:47
     */
    public static String parserBasePackage(Pattern pattern, String value){
        String  path= null;
        if(value!=null && pattern !=null){
            Matcher matcher = pattern.matcher(value);
            if (matcher.find()) {
                try {
                    path = matcher.group(1);
                } catch (Exception e) {

                }
            }
        }
        return path;
    }

    /**
     * 找目前下的子目录 .
     *
     * <p>
     * 找目前下的子目录
     *
     * @param file 父目录
     * @return java.util.List<java.lang.String> 子目录名称List
     * @author caobaoyu
     * @date 2020/4/6 14:49
     */
    public static List<String> findSubDirectory(File file){
        List<String> directoryList = new ArrayList<>();
        if(file.exists()) {
            if(!file.isFile()){
                File[] listFiles = file.listFiles();
                for (File f : listFiles) {
                    if( !f.isFile()){
                        directoryList.add(f.getName());
                    }
                }
            }
        }
        return directoryList;
    }

    /**
     * 找父包下的子包 .
     *
     * <p>
     * 找父包下的子包
     *
     * @param classLoader     ClassLoader
     * @param packageBasePath 父包路径
     * @return java.util.List<java.lang.String>
     * @author caobaoyu
     * @date 2020/4/6 14:52
     */
    public static List<String> findSubPackageNameList(ClassLoader classLoader, String packageBasePath ) throws Exception{
        List<String> packageSubNameList = new ArrayList<>();
        if(packageBasePath!=null){
            Enumeration<URL> urls = classLoader.getResources(packageBasePath);
            while(urls.hasMoreElements()) {
                URL url = urls.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String path = url.getPath();
                    File file = new File(path);
                    List<String> subDirList = findSubDirectory(file);
                    packageSubNameList.addAll(subDirList);
                }
            }
        }
        return packageSubNameList;
    }

    /**
     *  检查路径是否是包
     *
     *  <p>
     * 检查路径是否是包
     *
     * @param classLoader ClassLoader
     * @param packagePath 包路径
     *
     * @return boolean 是否是包，true:是;false：不是
     *
     * @author caobaoyu
     * @date 2020/4/6 14:53
     */
    public static boolean checkPackage(ClassLoader classLoader, String packagePath ) throws Exception{
        boolean check = false;
        if(packagePath!=null){
            Enumeration<URL> urls = classLoader.getResources(packagePath);
            while(urls.hasMoreElements()) {
                URL url = urls.nextElement();
                String protocol = url.getProtocol();
                if ("file".equals(protocol)) {
                    String path = url.getPath();
                    File file = new File(path);
                    if(file.exists()) {
                        if(!file.isFile()){
                            check = true;
                            break;
                        }
                    }
                }
            }
        }
        return check;
    }

    /**
     * 找到包统配符描述的所有包的完整路径 .
     *
     * <p>
     * 找到包统配符描述的所有包的完整路径
     *
     * @param classLoader ClassLoader
     * @param pattern 父包解析正则表达式
     * @param packageName 包描述，带有*
     * @return java.util.List<java.lang.String>
     * @author caobaoyu
     * @date 2020/4/6 14:55
     */
    public static List<String> findPackageList(ClassLoader classLoader,Pattern pattern, String packageName)throws Exception{
        List<String> packageNameList = new ArrayList<>();
        if(packageName!=null && classLoader!=null){
            String packagePath = packageName.replaceAll("\\.", "/");
            String packageBasePath = parserBasePackage(pattern,packagePath);
            List<String> packageSubNameList = findSubPackageNameList(classLoader,packageBasePath);
            for (String packageSubName:packageSubNameList){
                String fullPackagePath = packagePath.replaceAll("\\*", packageSubName);
                if(checkPackage(classLoader,fullPackagePath)){
                    String fullPackageName = packageName.replaceAll("\\*", packageSubName);
                    packageNameList.add(fullPackageName);
                }
            }
        }
        return packageNameList;
    }

}
