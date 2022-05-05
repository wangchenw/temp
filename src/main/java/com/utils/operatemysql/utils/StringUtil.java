package com.utils.operatemysql.utils;

import java.io.*;
import java.util.*;

/**
 * @Author: WangChen
 * @Description:
 * @Date: create in 2022/4/28 14:38
 */
public class StringUtil {

    public static void main(String[] args) {

        String t="aaaaaaaaaa<table></table>aaa<table></table>";
        String s="<TABLEc";

//      t="";

        System.out.println("length="+t.length());

        System.out.println(t.indexOf(s,0));
        System.out.println(ignoreCaseIndexOf(t, s,0));

        System.out.println(t.lastIndexOf(s));
        System.out.println(ignoreCaseLastIndexOf(t, s));
    }

    /**
     * 返回指定子字符串在此字符串中第一次出现处的索引，从指定的索引开始，不区分大小。
     *
     * @param subject 被查找字符串。
     * @param search 要查找的子字符串。
     * @return 指定子字符串在此字符串中第一次出现处的索引，从指定的索引开始。
     */
    public static int ignoreCaseIndexOf(String subject, String search) {
        return ignoreCaseIndexOf(subject, search,-1);
    }

    /**
     * 返回指定子字符串在此字符串中第一次出现处的索引，从指定的索引开始，不区分大小。
     *
     * @param subject 被查找字符串。
     * @param search 要查找的子字符串。
     * @param fromIndex 开始查找的索引位置。其值没有限制，如果它为负，则与它为 0 的效果同样：将查找整个字符串。
     *          如果它大于此字符串的长度，则与它等于此字符串长度的效果相同：返回 -1。
     * @return 指定子字符串在此字符串中第一次出现处的索引，从指定的索引开始。
     */
    public static int ignoreCaseIndexOf(String subject, String search,
                                        int fromIndex) {

        //当被查找字符串或查找子字符串为空时，抛出空指针异常。
        if (subject == null || search == null) {
            throw new NullPointerException("输入的参数为空");
        }

        fromIndex = fromIndex < 0 ? 0 : fromIndex;

        if (search.equals("")) {
            return fromIndex >= subject.length() ? subject.length() : fromIndex;
        }

        int index1 = fromIndex;
        int index2 = 0;

        char c1;
        char c2;

        loop1: while (true) {

            if (index1 < subject.length()) {
                c1 = subject.charAt(index1);
                c2 = search.charAt(index2);

            } else {
                break loop1;
            }

            while (true) {
                if (isEqual(c1, c2)) {

                    if (index1 < subject.length() - 1
                            && index2 < search.length() - 1) {

                        c1 = subject.charAt(++index1);
                        c2 = search.charAt(++index2);
                    } else if (index2 == search.length() - 1) {

                        return fromIndex;
                    } else {

                        break loop1;
                    }

                } else {

                    index2 = 0;
                    break;
                }
            }
            //重新查找子字符串的位置
            index1 = ++fromIndex;
        }

        return -1;
    }

    /**
     * 返回指定子字符串在此字符串中最右边出现处的索引。
     *
     * @param subject 被查找字符串。
     * @param search 要查找的子字符。
     * @return 在此对象表示的字符序列中最后一次出现该字符的索引；如果在该点之前未出现该字符，则返回 -1
     */
    public static int ignoreCaseLastIndexOf(String subject, String search){
        if(subject==null){
            throw new NullPointerException("输入的参数为空");
        }
        else{
            return ignoreCaseLastIndexOf(subject,search,subject.length());
        }
    }

    /**
     * 返回指定字符在此字符串中最后一次出现处的索引，从指定的索引处开始进行反向查找。
     * @param subject 被查找字符串 。
     * @param search 要查找的子字符串。
     * @param fromIndex 开始查找的索引。fromIndex 的值没有限制。如果它大于等于此字符串的长度，则与它小于此字符串长度减 1 的效果相同：将查找整个字符串。
     *          如果它为负，则与它为 -1 的效果相同：返回 -1。
     * @return 在此对象表示的字符序列（小于等于 fromIndex）中最后一次出现该字符的索引；
     *          如果在该点之前未出现该字符，则返回 -1
     */
    public static int ignoreCaseLastIndexOf(String subject, String search,
                                            int fromIndex) {

        //当被查找字符串或查找子字符串为空时，抛出空指针异常。
        if (subject == null || search == null) {
            throw new NullPointerException("输入的参数为空");
        }

        if (search.equals("")) {
            return fromIndex >= subject.length() ? subject.length() : fromIndex;
        }

        fromIndex = fromIndex >= subject.length() ? subject.length() - 1 : fromIndex;

        int index1 = fromIndex;
        int index2 = 0;

        char c1;
        char c2;

        loop1: while (true) {

            if (index1 >= 0) {
                c1 = subject.charAt(index1);
                c2 = search.charAt(index2);
            } else {
                break loop1;
            }

            while (true) {
                //判断两个字符是否相等
                if (isEqual(c1, c2)) {
                    if (index1 < subject.length() - 1
                            && index2 < search.length() - 1) {

                        c1 = subject.charAt(++index1);
                        c2 = search.charAt(++index2);
                    } else if (index2 == search.length() - 1) {

                        return fromIndex;
                    } else {

                        break loop1;
                    }
                } else {
                    //在比较时，发现查找子字符串中某个字符不匹配，则重新开始查找子字符串
                    index2 = 0;
                    break;
                }
            }
            //重新查找子字符串的位置
            index1 = --fromIndex;
        }

        return -1;
    }

    /**
     * 判断两个字符是否相等。
     * @param c1 字符1
     * @param c2 字符2
     * @return 若是英文字母，不区分大小写，相等true，不等返回false；
     *          若不是则区分，相等返回true，不等返回false。
     */
    private static boolean isEqual(char c1,char c2){
        //  字母小写                   字母大写
        if(((97<=c1 && c1<=122) || (65<=c1 && c1<=90))
                && ((97<=c2 && c2<=122) || (65<=c2 && c2<=90))
                && ((c1-c2==32) || (c2-c1==32))){

            return true;
        }
        else if(c1==c2){
            return true;
        }

        return false;
    }

    public static String getMinValue(List<String> arrayList) {
        //定义一个map来存截取的数据
        HashMap<Integer,String> hashMap=new  HashMap<Integer,String>();
        for (String string : arrayList) {
            hashMap.put(string.length(), string);
        }
        //获取所有的key值
        Set<Integer> set = hashMap.keySet();
        Object[] obj = set.toArray();
        //排序
        Arrays.sort(obj);
        //最小的Value值的变量
        String  resultValue = "";
        //根据key ,找Value
        for(Map.Entry<Integer,String> str : hashMap.entrySet()){
            if(obj[0].equals(str.getKey())){
                resultValue = str.getValue();
            }
        }
        return resultValue ;
    }
    public static String getMaxValue(List<String> arrayList) {
        //定义一个map来存截取的数据
        HashMap<Integer,String> hashMap=new  HashMap<Integer,String>();
        for (String string : arrayList) {
            hashMap.put(string.length(), string);
        }
        //获取所有的key值
        Set<Integer> set = hashMap.keySet();
        Object[] obj = set.toArray();
        //排序
        Arrays.sort(obj);
        //最小的Value值的变量
        String  resultValue = "";
        //根据key ,找Value
        for(Map.Entry<Integer,String> str : hashMap.entrySet()){
            if(obj[arrayList.size()-1].equals(str.getKey())){
                resultValue = str.getValue();
            }
        }
        return resultValue ;
    }

    public static String toHtmlString(File file) {
        // 获取HTML文件流
        StringBuffer htmlSb = new StringBuffer();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), "utf-8"));
            while (br.ready()) {
                htmlSb.append(br.readLine());
            }
            br.close();
            // 删除临时文件
            //file.delete();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // HTML文件字符串
        String htmlStr = htmlSb.toString();
        // 返回经过清洁的html文本
        return htmlStr;
    }
}
