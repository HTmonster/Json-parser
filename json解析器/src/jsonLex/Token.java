/**
 * @ClassName: Token
 * @Description: 词法分析的结果
 *               type属性 value属性
 * @author Theo_hui
 * @Email theo_hui@163.com
 * @Date 2018/12/29 22:10
 */


package jsonLex;

import jsonLex.TokType;

public class Token {

    private int  type; //token类的类型
    private String  value; //该token类的值

    //位置标记
    private int Line,Col_s,Col_e; //在的行号 列号开始值 列号结束值

    /*构造函数 对于非NUM与STR*/
    public Token(int type){
        this.type=type;
        this.value=null;
    }

    /*构造函数 对于NUM与STR*/
    public Token(int type,String value){
        this.type=type;
        this.value=value;
    }

    //私有属性操作
    public int getType(){
        return type;
    }
    public void setType(int type){
        this.type=type;
    }
    public String getValue(){
        return value;
    }
    public void setValue(String value){
        this.value=value;
    }
    public int getLine(){
        return Line;
    }
    public void setLine(int line){
        this.Line=line;
    }
    public int getCol_s(){
        return Col_s;
    }
    public void setCol_s(int col_s){
        this.Col_s=col_s;
    }
    public int getCol_e(){
        return Col_e;
    }
    public void setCol_e(int col_e){
        this.Col_e=col_e;
    }


    public String getName(){
        return TokType.getTokenName(this.getType());
    }

    public String getInitValue(){
        return TokType.getTokenValueByNo(this.getType());
    }
    /**
     * 获得对应的具体对象的值
     * TRUE FALSE  返回真假值对象
     * NULL  返回null对象
     * STR   返回string对象
     * NUM   返回double或int
     *
     * @param
     *
     * @return 具体对象
     */
    public Object getRealValue(){
        Object ret=null;
        switch (this.getType()){
            case TokType.TRUE:
                ret=true;
                break;
            case TokType.FALSE:
                ret=false;
                break;
            case TokType.NULL:
                ret=null;
                break;
            case TokType.STR:
                ret= value;
                break;
            case TokType.NUM:
                if (value.indexOf('.')>=0){
                    ret= Double.parseDouble(value);//Double对象
                }else{
                    ret= Integer.parseInt(value);
                }
                break;
        }

        return ret;
    }

    /**
     * 格式化输出token的信息
     *
     * @param
     *
     * @return 格式化信息
     */
    public String toString(){
        return "["+getType()+"]\t["+TokType.getTokenName(getType())+"]\t"+
                getValue()+"\t"+getLine()+": ("+getCol_s()+" "+getCol_e()+")";
    }


}
