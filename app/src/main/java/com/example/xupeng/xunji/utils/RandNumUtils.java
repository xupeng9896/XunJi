package com.example.xupeng.xunji.utils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

/**
 * Created by xupeng on 2018/4/9.
 */

public class RandNumUtils {

    /*private static HashSet<Integer> randIdSet=new HashSet<>();
    private static int minNum;
    private static int maxNum;
    private static int count;

    public static void getInstance(int min,int max,int n){
        minNum=min;
        maxNum=max;
        count=n;
    }

    public static Integer getRandId(){

        if(randIdSet.size()==0){
            getRandSet(minNum,maxNum,count,randIdSet);
        }
        Integer[] ids=(Integer[]) randIdSet.toArray();
        Integer id= ids[0];
        randIdSet.remove(id);
        return id;
    }

    *//**
     * 随机指定范围内N个不重复的数
     * 利用HashSet的特征，只能存放不同的值
     * @param min  指定范围最小值
     * @param max  指定范围最大值
     * @param n    随机数个数
     * @param set  set 随机数结果集
     *//*
    private static void getRandSet(int min, int max, int n, HashSet<Integer> set) {
        if (n > (max - min + 1) || max < min) {
            return;
        }
        for (int i = 0; i < n; i++) {
            // 调用Math.random()方法
            int num = (int) (Math.random() * (max - min)) + min;
            set.add(num);// 将不同的数存入HashSet中
        }
        int setSize = set.size();
        // 如果存入的数小于指定生成的个数，则调用递归再生成剩余个数的随机数，如此循环，直到达到指定大小
        if (setSize < n) {
            getRandSet(min, max, n - setSize, set);// 递归
        }
    }
*/
    public static int getRandImageNum(){
        Random random=new Random();
        int num=random.nextInt(20)+1;
        return num;
    }
}
