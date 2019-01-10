/**
 * @ClassName: ParseTreeNode
 * @Description: 语法树节点
 * @author Theo_hui
 * @Email theo_hui@163.com
 * @Date 2019/1/5 11:21
 */


package jsonParser;

import java.util.ArrayList;
import java.util.List;

public class ParseTreeNode {

    private String data; //节点数据
    private int type;//节点类型
    private int depth;//深度

    private ParseTreeNode parent;//父节点
    private List<ParseTreeNode> children=new ArrayList<>();//孩子节点列表

    //节点类型
    public static final int ROOT=0;//根节点
    public static final int COM=1;//普通节点
    public static final int LEAF=2;//叶子节点

    /*构建函数*/
    public ParseTreeNode(String data){
        this.data=data;
    }

    public  ParseTreeNode(String data,int type){
        this.data=data;
        this.type=type;
    }

    public  ParseTreeNode(String data,ParseTreeNode parent){
        this.data=data;
        this.parent=parent;
    }

    //private属性对外接口
    public void setData(String data){
        this.data=data;
    }
    public String getData(){
        return this.data;
    }
    public void setDepth(int depth){
        this.depth=depth;
    }
    public int getDepth(){
        return this.depth;
    }
    public ParseTreeNode getParent(){
        return this.parent;
    }
    public List<ParseTreeNode> getChildren(){
        return this.children;
    }
    public ParseTreeNode getChild(int index){
        if (index<0||index>children.size()){
            return null;
        }else {
            return this.children.get(index);
        }
    }


    /*节点操作*/

    /**
     * 设置该节点的父节点
     *
     * @param parent 父节点
     *
     * @return
     */
    public void setParent(ParseTreeNode parent){
        this.parent=parent;
        parent.addChild(this);
        int oldp=parent.getDepth();
        int newP=this.getDepth()+1;

        if(oldp<newP){
            parent.setDepth(newP);
        }
    }

    /**
     * 添加一个孩子
     *
     * @param child 孩子节点
     *
     * @return
     */
    public void addChild(ParseTreeNode child){
        this.children.add(0,child);
    }

    /**
     * 以侧边树的方法显示该节点的所有节点
     *
     * 递归的方法
     *
     * @param level 该节点对应的节点
     *
     * @return
     */

    public void showAllSubNode(int level){

        //打印自己
        for(int i=0;i<level;i++){
            System.out.printf("| ");
        }
        System.out.printf("|--"+getData()+"\n");

        //打印孩子节点
        if(!children.isEmpty()){
            for(int i=0;i<level;i++){
                System.out.printf("| ");
            }
            System.out.printf("|__\n");

            for (int i=0;i<children.size();i++){
                children.get(i).showAllSubNode(level+1);
            }
        }


    }

//    public static void main(String[] args) {
//        ParseTreeNode test=new ParseTreeNode("test",ParseTreeNode.COM);
//        ParseTreeNode com1=new ParseTreeNode("com1",ParseTreeNode.COM);
//        ParseTreeNode sun1=new ParseTreeNode("sun1",ParseTreeNode.COM);
//        ParseTreeNode sun2=new ParseTreeNode("sun1",ParseTreeNode.COM);
//        ParseTreeNode sun3=new ParseTreeNode("sun1",ParseTreeNode.COM);
//        ParseTreeNode xms3=new ParseTreeNode("xms1",ParseTreeNode.COM);
//        com1.addChild(sun1);
//        com1.addChild(sun2);
//        com1.addChild(sun3);
//        sun1.addChild(xms3);
//        test.addChild(com1);
//        test.addChild(new ParseTreeNode("com2",ParseTreeNode.COM));
//        test.addChild(new ParseTreeNode("com3",ParseTreeNode.COM));
//        test.showAllSubNode(1);
//    }
}
