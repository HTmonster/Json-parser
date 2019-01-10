/**
 * @ClassName: ParseTree
 * @Description: 语法树
 * @author Theo_hui
 * @Email theo_hui@163.com
 * @Date 2019/1/5 17:37
 */


package jsonParser;
import jsonParser.*;

import java.util.ArrayList;
import java.util.List;

/************************************
 * 该树是从下到上建立的
 * 作用等于一个容器，主要功能在Node中
 */
public class ParseTree {

    public String name;//树的名字
    public int depth=0;//深度
    public ParseTreeNode ROOT_NODE;//根节点
    private List<ParseTreeNode> nodes=new ArrayList<>();//树节点存储列表 通过序号索引

    /*构造函数*/
    public ParseTree(String name){
        this.name=name;
    }

    /**
     * 添加一个节点 可以先不指明关系
     *
     * @param node 添加的节点
     *
     * @return 该节点在列表中的序号
     */
    public int addNode(ParseTreeNode node){
        nodes.add(node);
        return nodes.indexOf(node);
    }

    /**
     * 设置一个节点的父节点
     *
     * @param index 该节点对应的序号
     *        parent 要设置的父节点
     *
     * @return
     */
    public void setParent(int index,ParseTreeNode parent){
        nodes.get(index).setParent(parent);
    }

    /**
     * 设置根节点
     *
     * @param index 根节点的对应序号
     *
     * @rerurn 根节点
     */
    public ParseTreeNode setRoot(int index){
        this.ROOT_NODE=nodes.get(index);
        return ROOT_NODE;
    }

    /**
     * 显示某个节点的下属节点
     *
     * @param index 该节点的对应序号
     *
     * @return
     */
    public void showSubNode(int index){
        nodes.get(index).showAllSubNode(0);
    }

    /**
     * 显示整个树的结构
     *
     * @param
     *
     * @return
     */
    public void showTree(){
        if(ROOT_NODE==null) return;

        ROOT_NODE.showAllSubNode(0);
    }
}
