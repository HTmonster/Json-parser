/**
 * @ClassName: jsonSyntax
 * @Description: 语义分析器
 * @author Theo_hui
 * @Email theo_hui@163.com
 * @Date 2019/1/5 21:55
 */


package jsonSyntax;

import jsonLex.FileIO;
import jsonParser.*;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class jsonSyntax {

    public LRParser parser;//语法分析器
    private static  List<String> code=new ArrayList<>();//中间代码

    private static int idDespatch=0;
    private static final int NULL_ID=-1;

    private ParseTree tree ;

    /**********************************中间代码列举************************
     * 1. GenObj Oid             创建一个对象         id:编号
     * 2. GenArr Aid             创建一个数组         id:编号
     * 3. GenMem Memid v         创建一个数组成员     id:编号 v:值
     * 4. GneAtr Atrid n v       创建一个对象属性     id:编号 n:名字 v:值
     * 4. SetAttr id value    设置对象的属性   id:对象id value：属性值
     * 5. AddItem id Value    添加值到数组中   id:数组id value:对应的值
     */

    public jsonSyntax(String PATH){
        parser=new LRParser(PATH);
    }

    /**
     * 进行前期的语法分析
     *
     */
    public void getParseTree(){
        try {
            if(parser.getTokens()){
                parser.parse();
                tree=parser.getTree();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 迭代生成代码  深度优先
     *
     * @param node 当前的节点 fid 父亲节点的id
     *
     * @return string 返回值 可以返回id 或value
     */

    public static String genCode(ParseTreeNode node,int fid)throws syntaxException{

        String data=node.getData();

        switch (data){
            case "O":
                int oid=idDespatch++;
                code.add("GenObj "+"O"+oid+" _ "+" _ ");

                String attr=node.getChild(1).getData();
                //IF: O->{M}类型
                if(attr.equals("M")){
                    genCode(node.getChild(1),oid);
                }//ELSE: O->{}类型

                return String.valueOf(oid);
            case "M":

                //IF: M->P,M
                if(node.getChild(2)!=null){
                    genCode(node.getChild(0),fid);
                    genCode(node.getChild(2),fid);
                }//ELSE: M->P
                else{
                    genCode(node.getChild(0),fid);
                }

                return String.valueOf(NULL_ID);
            case "P":
                int pid=idDespatch++;
                //P->str:V
                String name=node.getChild(0).getData();//取得str
                String value=genCode(node.getChild(2),NULL_ID);//取得V
                code.add("GenAtr "+"Atr"+pid+" "+name+" "+value);
                code.add("AddAtr "+"Atr"+pid+" "+fid+" _ ");
                return String.valueOf(NULL_ID);
            case "V":
                String vItem =node.getChild(0).getData();

                //V->O V->A
                if(vItem.equals("O")||vItem.equals("A")){
                    return genCode(node.getChild(0),NULL_ID);
                } else {
                    return vItem;
                }
            case "A":

                int aid=idDespatch++;
                code.add("GenArr "+"A"+aid+" _ "+" _ ");

                String Mem=node.getChild(1).getData();
                //IF: A->[E]类型
                if(Mem.equals("E")){
                    genCode(node.getChild(1),aid);
                }//ELSE: O->{}类型

                return String.valueOf(aid);
            case "E":
                int eid=idDespatch++;
                //IF: E->V,E
                if(node.getChild(2)!=null){
                    String Evalue=genCode(node.getChild(0),eid);
                    code.add("GenMem "+"Mem"+eid+" "+Evalue+" _ ");
                    code.add("AddMem "+"Mem"+eid+" "+fid+" _ ");
                    genCode(node.getChild(2),fid);
                }//ELSE: E->V
                else{
                    String Evalue=genCode(node.getChild(0),eid);
                    code.add("GenMem "+"Mem"+eid+" "+Evalue+" _ ");
                    code.add("AddMem "+"Mem"+eid+" "+fid+" _ ");
                }

                return String.valueOf(NULL_ID);
            default:
                throw new syntaxException("error!非规定内语法树");
        }
    }

    /**
     * 对语法树生成对应的代码
     *
     */
    public void genCode4tree(){
        try {
            genCode(tree.ROOT_NODE,NULL_ID);
        }catch (Exception e){
            e.printStackTrace();
        }

        int i=0;
        System.out.println("================生成代码===============");
        for(String s:code){
            System.out.printf("(%3d) "+s+'\n',i);
            i++;
        }
    }

    /**
     * 把生成的代码保存在文件中
     *
     * @param filename 要保存的文件名
     *
     * @return
     */

    public boolean saveCode2File(String filename){

        try{

            File outfile = new File(filename);
            FileWriter fileWriter = new FileWriter(outfile);

            int i=0;
            for(String s:code){
                fileWriter.write("("+i+")"+s+'\n');
                i++;
            }

            fileWriter.close();

            return true;
        }catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }

        return false;
    }

    public static void main(String[] args) {
        String INPATH=System.getProperty("user.dir")+"/src/rawJsons/";
        String OUTPATH=System.getProperty("user.dir")+"/src/out/";
        FileIO IO = new FileIO();
       jsonSyntax test =new jsonSyntax(IO.readFile(INPATH+"weather.txt"));

       try {
           test.getParseTree();
           //test.genCode(test.tree.ROOT_NODE,0);
           test.genCode4tree();
           test.saveCode2File(OUTPATH+"weatherSyntax.txt");
       }catch (Exception e){
           e.printStackTrace();
       }

    }
}
