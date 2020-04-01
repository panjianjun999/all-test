package com.pan;

import net.good321.platform.wordfilter.WordFilter;
import net.good321.platform.wordfilter.WordType;

/**
 * Created by DanielHuang on 2019/9/2.
 */
public class WordFilterTest {

    /**
     * 测试主Main
     * @param argv
     */
    public static final void main(String[] argv){
//        WordFilterTest.TestCreateFileFromExcel();
        WordFilterTest.TestWordFilter();
    }

    public static void TestCreateFileFromExcel(){
    	String excelPath = "E:/tempshIE/keyword-2.xls";
        String localFilePath = "E:/tempshIE/keywords.json";
        WordFilter.CreateLocalWordsFile(excelPath, localFilePath);
        System.out.println("打表完成！！！");
    }

    public static void TestWordFilter(){
        String localFilePath = "E:/tempshIE/keywords.json";
        WordFilter filter = new WordFilter(2, localFilePath, null,null);
        String text = "春     宵一刻";
        if(filter.hasSensitiveWord(WordType.NAME, text)){
            System.out.println(text + ":有敏感词" );
        }
        String replaceRs = filter.replaceSensitiveWord(WordType.NAME, text, '*');
        System.out.println("转化文本："+replaceRs);
        System.exit(0);
    }

}
