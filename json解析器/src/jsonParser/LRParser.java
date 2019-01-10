/**
 * @ClassName: LRParser
 * @Description: (用一句话描述该文件做什么)
 * @author Theo_hui
 * @Email theo_hui@163.com
 * @Date 2019/1/4 0:02
 */


package jsonParser;


import jsonLex.*;
import jsonParser.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class LRParser {

    private ParseTree tree=new ParseTree("paser");//语法树

    private Stack<Integer> states= new Stack<>(); //状态栈
    private Stack<String> symbols= new Stack<>();//符号栈
    private Stack<Integer> nodeStack=new Stack<>();//树节点栈 存储节点的序号

    private List<Token> tokens= new ArrayList<>();//得到的分词TOken结果
    private jsonLex lex =null;//词法分析器

    /*构造函数*/
    public LRParser(String PATH){
        lex=new jsonLex(PATH);//词法分析器
        states.push(0);//填入初始状态
        nodeStack.push(tree.addNode(new ParseTreeNode("$",ParseTreeNode.LEAF)));//填入$
        symbols.push("$");//填入初始符号
    }

    /**
     * 显示状态栈中的内容
     *
     */
    public void showStates(){

        System.out.printf("[");
        for(int s:states){
            System.out.printf("%2d  ",s);
        }
        System.out.println("");
    }
    /**
     * 显示符号栈中的内容
     *
     */
    public void showSymbol(){

        System.out.printf("[");
        for(String s:symbols){
            System.out.printf("%2s  ",s);
        }
        System.out.println("");
    }

    /*词法分析*/
    /**
     * 调用词法分析得到tokens
     *
     * @return boolean 是否解析成功
     */
    public boolean getTokens(){
        boolean ret;

        ret=lex.MatchAlltokens();
        lex.showAlltokens();
        tokens=lex.getTokens();

        return ret;
    }

    /*移进*/
    /**
     * 进行移进操作
     *
     * @param nextState 下一个状态 strIN 要移进的符号 strReal 该值对应的Value
     */
    public void MoveIn(int nextState,String strIn,String strReal){

        //移进
        symbols.push(strIn);
        states.push(nextState);
        nodeStack.push(tree.addNode(
                new ParseTreeNode(strReal,ParseTreeNode.LEAF)));

        //状态输出
        System.out.println("================================");
        System.out.println("进行移进："+strIn);
        showStates();
        showSymbol();
    }

    /*规约*/
    /**
     * 进行规约操作
     *
     * @param Rule 要规约的规则
     */
    public void Statute(int Rule){

        //解析规则
        String from=StateTable.Production[Rule][1];
        String to=StateTable.Production[Rule][0];

        //为规约项目创建一个新的树节点
        ParseTreeNode toNode=new ParseTreeNode(to,ParseTreeNode.COM);

        System.out.println("================================");
        System.out.println("进行规约："+to+"->"+from);

        //移出栈中元素
        String strOut;//退栈出的符号
        int node_Index;//退栈出的树节点序号
        do{
            strOut=symbols.pop();
            states.pop();
            node_Index=nodeStack.pop();
            tree.setParent(node_Index,toNode);//为规约的所有项目设置父节点

            System.out.println("移出："+strOut);
        }//直到第一个规约项目
        while (!strOut.equals(from.substring(0,1)));

        //规约
        symbols.push(to);
        nodeStack.push(tree.addNode(toNode));//移进规约节点序号
        System.out.println("移进："+to);

        //查GOTO表
        int Goto =StateTable.Goto[states.peek()][StateTable.GotoTITLE.indexOf(to)];
        states.push(Goto);

        //栈状态输出
        showStates();
        showSymbol();

    }


    /*解析token*/
    /**
     * 对tokens进行语法分析
     *
     */
    public boolean parse()throws ParserException{

        if(tokens.isEmpty()){
            throw new ParserException("词法Token为空");
        }

        Token tokenIN=null; //取出的token
        String strIN=null;  //token对应的代表符号
        String strReal=null;//token真实的值
        int ord;//token符号在表中对应的序号

        int stateNow;//现在的状态
        String action=null; //action的值

        tokens.remove(0);//去除BEGIN

        /*先取第一个*/
        tokenIN=tokens.remove(0);//取出队列的第一个
        strIN=tokenIN.getInitValue();//得到其代表的值
        strReal=tokenIN.getValue();//真实的值
        ord=StateTable.ActTITLE.indexOf(strIN);//对应Action表中的序号

        System.out.println(">>>>>>得到token:"+strIN+" "+strReal+"<<<<<<<<<<");

        /*循环操作*/
        do{
            stateNow=states.peek();//取现在的状态
            action=StateTable.Action[stateNow][ord];//取action表中对应的值
            System.out.println("["+stateNow+"]"+"action: "+action);

            //CASE: S 移进
            if(action.charAt(0)=='S'){

                //解析得到下一个状态
                int nextState=Integer.parseInt(action.substring(1));

                //移进
                MoveIn(nextState,strIN,strReal);

                //重新取token并解析
                tokenIN=tokens.remove(0);//取出队列的第一个
                strIN=tokenIN.getInitValue();//得到其代表的值
                strReal=tokenIN.getValue();//真实的值
                ord=StateTable.ActTITLE.indexOf(strIN);//str对应表中的序号

                System.out.println(">>>>>>得到token:"+strIN+" "+strReal+"<<<<<<<<<");


            }//CASE: r 规约
            else if(action.charAt(0)=='r'){
                int role=Integer.parseInt(action.substring(1));//取规则

                //规约
                Statute(role);

            }//CASE: acc 接受
            else if(action.equals("acc")){
                System.out.println("acc:分析成功！");

                //设置最后的根节点并显示树结构
                ParseTreeNode root=tree.setRoot(nodeStack.peek());
                System.out.println("=========================得到语法树===========================");
                tree.showTree();

                return true;
            }else{

                throw new ParserException(tokenIN.getLine(),
                        tokenIN.getCol_s(),"语法错误 在 \""+strReal+"\"附近");
            }
        }
        while (!tokenIN.getValue().equals(TokType.END));


        return false;
    }


    /**
     * 返回语法树
     *
     */
    public ParseTree getTree(){
        return tree;
    }

    public static void main(String[] args) {
        String INPATH=System.getProperty("user.dir")+"/src/rawJsons/";
        String OUTPATH=System.getProperty("user.dir")+"/src/out/";
        FileIO IO = new FileIO();
        LRParser test=new LRParser(IO.readFile(INPATH+"weather.txt"));


        try {
            if(test.getTokens()){
                test.parse();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
