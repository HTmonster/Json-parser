/**
 * @ClassName: jsonLex
 * @Description: json json词法分析器
 * @author Theo_hui
 * @Email theo_hui@163.com
 * @Date 2018/12/29 11:04
 */


package jsonLex;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class jsonLex {


    private int lineNum = 0;// 当前行号
    private int startCol = 0;//每个Token的开始位置
    private int cur = -1;// 当前字符游标
    private String str = null; // 保存当前要解析的字符串
    private int len = 0; // 保存当前要解析的总字符串的长度

    //解析得到的token
    private List<Token> tokens=new ArrayList<>();


    /*构造函数*/
    public jsonLex(String str) {
        if (str == null)
            throw new NullPointerException("词法解析构造函数不能传递null");
        this.str = str;
        this.len = str.length();
        this.startCol = 0;
        this.cur = -1;
        this.lineNum = 0;
    }

    /*判断部件*/
    //字母
    public boolean isLetter(char c) {
        return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
    }
    //数字
    public boolean isNum(char c) {
        return (c >= '0' && c <= '9');
    }

    //字母数字
    public boolean isNumLetter(char c) {
        return (isNum(c) || isLetter(c));
    }
    //正负符号
    public boolean isSign(char c){
        return (c=='+'||c=='-');
    }
    //标点
    public boolean isPunctuation(char c){
        return (c==','||c=='.'||c=='-'||c=='('||c==')');
    }
    //空格符
    public static boolean isSpace(char c) {
        return (c == ' ' || c == '\t' || c == '\n'||c=='\r');
    }

    //合法的数字结尾符
    public static boolean isLegalNumEndNext(char c){
        return (isSpace(c)|| c==','||c==']'||c=='}');
    }

    //合法的json符号
    public static boolean isLegalSymbol(char c){
        return (c=='{'||c=='}'||c=='['||c==']'||c==','||c==':');
    }

    //中文
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if(ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }

    /*解析部件*/
    /**
     * 解析获取字符串的值
     *
     * @param s 当前的字符 \"
     *
     * @return 匹配得到的字符串
     */
    private String matchStrValue(char s) throws LexException {
        int start = cur;
        char c;
        c=nextChar();
        while (c!=0) {
//            if (isNumLetterUnderline(c)||isSpace(c)||isPunctuation(c)||isChinese(c)) {
//                c = nextChar();
//            }
            if(c!='\"'){
                c = nextChar();
            }
            else if (c=='\"') {
                return str.substring(start + 1, cur);
            } else {
                throw new LexException(lineNum,startCol,"字符串符号错误\" "+c+" \"");
            }
        }

        return null;
    }

    /**
     * 解析获取数字的值
     *
     * @param s 当前的字符
     *
     * @return 匹配得到的字符串
     */
    private String matchNUMValue(char s) throws LexException {
        int start = cur;
        char c;

        boolean flag_point=false;
        boolean flag_e=false;

        //TODO:数字判断优化
        while ((c=nextChar())!=0) {
            //CASE: 数字
            if(isNum(c)){
                continue;
            }
            //CASE: "."
            else if(c=='.'){
               if(flag_point){
                   throw new LexException(lineNum,start,"数字错误！多余.");
               }else{
                   flag_point=true;
               }
            }
            //CASE: E e
            else if(c=='E'||c=='e'){
               if(flag_e){
                   throw new LexException(lineNum,start,"数字错误！多余e");
               }else{
                   flag_e=true;
               }

               //e后的如果是符号取进
               if(isSign(str.charAt(cur+1))){
                   c=nextChar();
               }
            }
            //CASE:非数字字符
            else if(isLetter(c)){
                throw new LexException(lineNum,start,"数字错误！数字中不能有字符"+c);
            }
            //CASE:合法的结束
            else if(isLegalNumEndNext(c)){
                return str.substring(start,backChar());
            }
            //CASE:非预料情况
            else{
                throw new LexException(lineNum,start,"数字错误！数字非法错误"+c);
            }
        }

        return null;
    }

    /**
     * 匹配 true false null
     *
     * @param s 当前字符  t|f|n
     *
     * @return 匹配得到的token
     */
    private Token matchDefToken(char s) throws LexException {
        int start = cur;
        char c;

        //取得整个字符串标志符
        while ((c=nextChar())!=0&&isLetter(c));

        String substr =str.substring(start,backChar());

        if("true".equals(substr)){
            return new Token(TokType.TRUE,TokType.getTokenValueByNo(TokType.TRUE));
        }else if("false".equals(substr)){
            return new Token(TokType.FALSE,TokType.getTokenValueByNo(TokType.FALSE));
        }else if("null".equals(substr)){
            return new Token(TokType.NULL,TokType.getTokenValueByNo(TokType.NULL));
        }else {
            throw new LexException(lineNum,start,"非法标志符 "+substr);
        }

    }

    /**
     * 匹配 [ ] { } , :
     *
     * @param s 当前字符
     *
     * @return 匹配得到的token
     */
    private Token matchSymToken(char s) throws LexException {
        int start = cur;
        char c;

        switch (s){
            case '{':return new Token(TokType.OBJ_B,TokType.getTokenValueByNo(TokType.OBJ_B));
            case '}':return new Token(TokType.OBJ_E,TokType.getTokenValueByNo(TokType.OBJ_E));
            case '[':return new Token(TokType.ARR_B,TokType.getTokenValueByNo(TokType.ARR_B));
            case ']':return new Token(TokType.ARR_E,TokType.getTokenValueByNo(TokType.ARR_E));
            case ',':return new Token(TokType.SPLIT,TokType.getTokenValueByNo(TokType.SPLIT));
            case ':':return new Token(TokType.DESC,TokType.getTokenValueByNo(TokType.DESC));
        }

        return null;
    }


    /*操作部件*/

    /**
     * 下一个字符
     *
     * @return 取得的字符
     */
    private char nextChar() {
        //IF:还有字符
        if (cur >= len - 1) {
            return 0;
        }

        //取下一个字符
        cur++;
        startCol++;
        char c = str.charAt(cur);

        //空格或换行
        if (c == '\n'||c=='\r') {
            if(str.charAt(cur+1)!='\n'&&str.charAt(cur+1)!='\r'){
                lineNum++;
                startCol = 0;
            }
        }
        return c;
    }
    /**
     * 回退字符
     *
     * @return 游标
     */
    private int backChar() {
        //IF:不是开头
        if (cur <= 0) {
            return 0;
        }

        //回退
        int rcur = cur--;
        char c = str.charAt(rcur);
        if (c == '\n') {
            lineNum--;
        }
        return rcur;
    }
    /**
     * 检查是否结束
     *
     * @param
     *
     * @return 未结束 返回true  结束返回false
     */
    private boolean checkEnd() throws LexException {
        if(cur<len-1){
            return true;
        }
        else if(cur==len-1){
            return false;
        }
        else{
            throw new LexException(lineNum,startCol,"未知错误");
        }
    }

    /*运行部件*/

    /**
     * 获得token
     *
     * @return 获得token
     */
    public Token next() throws LexException {
        if (lineNum == 0) {
            lineNum = 1;
            return new Token(TokType.BEGIN,
                    TokType.getTokenValueByNo(TokType.BEGIN));
        }

        char c;
        while ((c = nextChar()) != 0) {
            //CASE:"  字符串 STR
            if (c == '\"') {
                Token tempToken = new Token(TokType.STR);
                String tempStr=matchStrValue(c);
                tempToken.setLine(lineNum);
                tempToken.setCol_s(startCol-tempStr.length());
                tempToken.setValue(tempStr);
                tempToken.setCol_e(startCol);
                return tempToken;
            }
            //CASE: 数字
            else if(isNum(c)||isSign(c)){
                Token tempToken = new Token(TokType.NUM);
                String tempValue = matchNUMValue(c);
                tempToken.setLine(lineNum);
                tempToken.setCol_s(startCol-tempValue.length());
                tempToken.setValue(tempValue);
                tempToken.setCol_e(startCol);
                return tempToken;
            }
            //CASE: 非"包围的字符 true|false|null|非法字符
            else if(isLetter(c)){
                Token tempToken= matchDefToken(c);
                tempToken.setLine(lineNum);
                tempToken.setCol_s(startCol-TokType.getTokenName(tempToken.getType()).length());
                tempToken.setCol_e(startCol);
                return tempToken;
            }
            //CASE: 规定的合法字符标识
            else if (isLegalSymbol(c)){
                Token tempToken= matchSymToken(c);
                tempToken.setLine(lineNum);
                tempToken.setCol_s(startCol-1);
                tempToken.setCol_e(startCol);
                return tempToken;
            }
            //CASE: 空格或者换行
            else if (isSpace(c)){
                continue;
            }
            //CASE:其他情况
            else{
                throw new LexException(lineNum,startCol,"解析运行中未知错误");
            }
        }
        if (c == 0) {
            return new Token(TokType.END);
        }
        return null;
    }


    /*总操作部件*/
    /**
     * 遍历总字符串获得所有的token
     *
     * @return 是否成功
     */
    public boolean MatchAlltokens(){
        tokens.clear();

        try {
            while (checkEnd()){
                tokens.add(next());
            }
            tokens.add(new Token(TokType.END,
                    TokType.getTokenValueByNo(TokType.END)));
            return true;
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

    /**
     * 显示所有获得的token
     *
     */
    public void showAlltokens(){
        System.out.println("编号\t token \t行号 \t列号 \t值");
        System.out.println("================================================\n");

        for(Token tok:tokens){
            System.out.format("%2d \t %-4s\t%4d\t %d:%d\t%s\n",
                    tok.getType(),TokType.getTokenName(tok.getType()),
                    tok.getLine(),tok.getCol_s(),tok.getCol_e(),
                    tok.getValue());
        }
    }

    /**
     * 把得到的分词结果保存在文件中
     *
     * @param path  存储的路径
     *
     * @return
     */
    public void saveAlltokens2File(String path){

        String str="";

        //得到要保存的信息
        for(Token tok:tokens){
            str+=tok.getType()+"\t"+ tok.getValue()+"\n";
        }

        try {
            //不存在则创建
            File toFile = new File(path);
            if (!toFile.exists()) {
                toFile.createNewFile();
            }
            FileWriter fw = null;
            fw = new FileWriter(toFile);
            fw.write(str);
            fw.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 输出所有的tokens
     *
     *
     * @return tokens的list
     */
    public List<Token> getTokens(){
        return tokens;
    }

    public static void main(String[] args) {
        String INPATH=System.getProperty("user.dir")+"/src/rawJsons/";
        String OUTPATH=System.getProperty("user.dir")+"/src/out/";
        FileIO IO = new FileIO();
        jsonLex test =new jsonLex(IO.readFile(INPATH+"weather.txt"));

        test.MatchAlltokens();
        test.showAlltokens();
        test.saveAlltokens2File(OUTPATH+"weatherLex.txt");
    }


}
