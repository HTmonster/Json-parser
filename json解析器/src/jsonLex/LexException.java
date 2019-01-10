package jsonLex;

/**
 * @ClassName: jsonLex.LexException
 * @Description: 词法分析器的错误类
 * @author Theo_hui
 * @Email theo_hui@163.com
 * @Date 2018/12/30 23:42
 */


public class LexException extends Exception {
    private Integer lineNum = null; //行号
    private Integer colNum = null;  //列号
    private String desc = null;  //描述
    private Throwable cause = null;//原因

    public LexException() {
        super();
    }

    public LexException(Integer lineNum, Integer colNum, String message) {

        this.colNum = colNum;
        this.lineNum = lineNum;
        this.desc = message;
    }

    public String getMessage() {
        return "[line:" + lineNum + ",column:" + colNum
                + "]" + desc + (cause == null ? "" : cause.toString());
    }


    public String toString() {
        return getMessage();
    }

}
