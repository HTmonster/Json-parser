/**
 * @ClassName: TokType
 * @Description: Token对应的类型
 * @author Theo_hui
 * @Email theo_hui@163.com
 * @Date 2018/12/30 23:00
 */


package jsonLex;

import java.util.Arrays;
import java.util.List;

public class TokType {

    /*各种Token对象类型及对应的编号*/
    public final static int OBJ_B=0;   //对象开始
    public final static int OBJ_E=1;   //对象结束
    public final static int ARR_B=2;   //队列开始
    public final static int ARR_E=3;   //队列结束
    public final static int DESC=4;    //键对值符号
    public final static int SPLIT=5;   //分隔符
    public final static int STR=6;      //字符串
    public final static int NUM=7;      //数字
    public final static int TRUE=8;     //真值
    public final static int FALSE=9;    //假值
    public final static int NULL=10;     //空
    public final static int BEGIN=11;    //json开始
    public final static int END=12;      //json结束

    /*Token类型名字列表*/
    public final static List<String> TOKEN_NAME_LIST = Arrays.asList(
        "OBJ_B","OBJ_E",
        "ARR_B","ARR_E",
        "DESC","SPLIT",
        "STR","NUM",
        "TRUE","FALSE","NULL",
        "BEGIN","END");
    /*Token类型对应字符列表*/
    public final static List<String> TOKEN_VALUE_LIST = Arrays.asList(
            "{","}",
            "[","]",
            ":",",",
            "s","n",
            "t","f","N",
            "%","$");
    //总类型数
    public static final int TOKEN_NUM = TOKEN_NAME_LIST.size();


    //通过类型获得名字
    public static String getTokenName(int no){
        if(no>=0&&no<TOKEN_NUM){
            return TOKEN_NAME_LIST.get(no);
        }else{
            return null;
        }
    }


    //通过名字获得类型对应的值
    public static String getTokenValueByname(String TOKEN_NAME){
        if(TOKEN_NAME_LIST.contains(TOKEN_NAME)){
            return TOKEN_VALUE_LIST.get(
                    TOKEN_NAME_LIST.indexOf(TOKEN_NAME)
            );
        }else{
            return "undefine";
        }
    }
    //通过编号获得类型对应的值
    public static String getTokenValueByNo(int no){
        if(no>=0&&no<TOKEN_NUM){
            return TOKEN_VALUE_LIST.get(no);
        }else{
            return "undefine";
        }
    }

//    public static void main(String[] args) {
//        TokType test=new TokType();
//        System.out.println(test.getTokenValueByname("xxx"));
//        System.out.println(test.getTokenValueByNo(test.BEGIN));
//    }

}
