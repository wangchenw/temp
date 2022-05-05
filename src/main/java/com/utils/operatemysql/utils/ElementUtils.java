package com.utils.operatemysql.utils;

import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.util.List;

/**
 * @Author: WangChen
 * @Description:
 * @Date: create in 2022/4/30 20:10
 */
public class ElementUtils {

    public static void getChildDiv(Element fatherElement) {

         if (fatherElement.childNodeSize() > 0) {
            for (int i = 0; i < fatherElement.childNodeSize(); i++) {
                Element child = fatherElement.child(i);
                System.out.println(child.text());
                getChildDiv(child);
            }
        }else {
            System.out.println(fatherElement.text());
        }
    }
}
