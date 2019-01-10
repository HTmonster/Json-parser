/**
 * @ClassName: FileIO
 * @Description: 文件操作
 * @author Theo_hui
 * @Email theo_hui@163.com
 * @Date 2019/1/2 16:52
 */


package jsonLex;

import   java.io.*;

public class FileIO {
    //从文件读取内容
    public static String readFile(String path){
        String str="";
        try{
            BufferedReader in = new BufferedReader(new FileReader( path));
            int c;
            while((c = in.read())!=-1)
            {
                str=str+(char)c;
            }
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return str;
    }
    //向文件写入内容
    public static void writeFile(String path,String str) throws IOException{
        File toFile = new File(path);
        if (!toFile.exists()) {
            toFile.createNewFile();
        }
        FileWriter fw = null;
        fw = new FileWriter(toFile);
        fw.write(str);
        fw.close();
    }
}
