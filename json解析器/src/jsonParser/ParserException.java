/**
 * @ClassName: ParserException
 * @Description: 语法分析器的错误类
 * @author Theo_hui
 * @Email theo_hui@163.com
 * @Date 2019/1/5 21:12
 */


package jsonParser;

public class ParserException extends Exception{
    private Integer lineNum = null; //行号
    private Integer colNum = null;  //列号
    private String desc = null;  //描述
    private Throwable cause = null;//原因

    public ParserException() {
        super();
    }

    public ParserException(Integer lineNum, Integer colNum, String message) {

        this.colNum = colNum;
        this.lineNum = lineNum;
        this.desc = message;
    }
    public ParserException(String message) {
        this.desc = message;
    }

    public String getMessage() {
        return  (lineNum == null ? "" :" line:" + lineNum) +
                (colNum == null ? "" :" column:" + colNum) +
                desc + (cause == null ? "" : cause.toString());
    }


    public String toString() {
        return getMessage();
    }
}
