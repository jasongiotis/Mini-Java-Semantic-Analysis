import syntaxtree.*;
import visitor.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // if(args.length != 1){
        //     System.err.println("Usage: java Main <inputFile>");
        //     System.exit(1);
            
        // }


    for(int i=0;i<args.length;i++){
        FileInputStream fis = null;
        try{
            System.out.println("----------------------------------------------START OF FILE "+(i+1)+"------------------------------------------------------------------");
            fis = new FileInputStream(args[i]);
            MiniJavaParser parser = new MiniJavaParser(fis);

            Goal root = parser.Goal();

            System.err.println("Program parsed successfully.");

            MyVisitor eval = new MyVisitor();
            root.accept(eval, null);
            SecondVisitor eval2 = new SecondVisitor(eval.b);
            root.accept(eval2,null);
            eval2.b.print();
         
        }
        catch(ParseException ex){
            System.out.println(ex.getMessage());
        }
        catch(FileNotFoundException ex){
            System.err.println(ex.getMessage());
        }
        catch(Exception ex){
            System.err.println("-------------------------------------ERROR "+ex.getMessage()+"----------------------------------");
            
            
        }
        finally{
            System.out.println("----------------------------------------------END OF FILE "+(i+1)+"------------------------------------------------------------------");


            try{
                if(fis != null) fis.close();
            }
            catch(IOException ex){
                System.err.println(ex.getMessage());
            }
        }
    }
    
}
}


class MyVisitor extends GJDepthFirst<String, Void>{
    public SymbolTable b;
    public String class_visited;
   public  MyVisitor(){ b=new SymbolTable();}
    
    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> "public"
     * f4 -> "static"
     * f5 -> "void"
     * f6 -> "main"
     * f7 -> "("
     * f8 -> "String"
     * f9 -> "["
     * f10 -> "]"
     * f11 -> Identifier()
     * f12 -> ")"
     * f13 -> "{"
     * f14 -> ( VarDeclaration() )*
     * f15 -> ( Statement() )*
     * f16 -> "}"
     * f17 -> "}"
     */
    private  int var_memory=0;
    private static int func_memory=0;
    @Override
    public String visit(MainClass n, Void argu) throws Exception {
        String classname = n.f1.accept(this, null);
        class_visited="main";
        b.add_class_decl("main");

        
        for ( Node node: n.f14.nodes){
            StringTokenizer st = new StringTokenizer(node.accept(this,null));
            String temp=st.nextToken();
            String var_name=st.nextToken();
            if (temp.equals("int")){
               
            }
            else if (temp.equals("boolean")){
             
            }
            else if(temp.equals("int[]") || temp.equals("boolean[]")){}
            else{
                
            }
            b.add_field("main", temp+" "+var_name);
        }
        b.add_field("main","String[] "+n.f11.accept(this,null));

        super.visit(n, argu);




        System.out.println();

        return null;
    }
 
    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "{"
     * f3 -> ( VarDeclaration() )*
     * f4 -> ( MethodDeclaration() )*
     * f5 -> "}"
     */
    @Override
    public String visit(ClassDeclaration n, Void argu) throws Exception {
        String classname = n.f1.accept(this, null);
        b.add_class_decl(classname);
        class_visited=classname;
        
     
        for ( Node node: n.f3.nodes){
            StringTokenizer st = new StringTokenizer(node.accept(this,null));
            String temp=st.nextToken();
            String var_name=st.nextToken();
            if (temp.equals("int")){

            }
            else if (temp.equals("boolean")){
                

            }
            else if(temp.equals("int[]") || temp.equals("boolean[]")){
             
            }
            else{
          
            }
            b.add_field(classname, temp+" "+var_name);
        }

        
        for ( Node node: n.f4.nodes){
            String fnc=node.accept(this,null);
            b.add_field(classname,fnc);
            func_memory+=8;
            // b.add_method(class_visited, fnc);


        }
       
        //     System.out.println("TEEEEEEEST"+mylist);
        //     System.out.println();
            
            
        //     }
        // }

        return null;
    }

    /**
     * f0 -> "class"
     * f1 -> Identifier()
     * f2 -> "extends"s
     * f3 -> Identifier()
     * f4 -> "{"
     * f5 -> ( VarDeclaration() )*
     * f6 -> ( MethodDeclaration() )*
     * f7 -> "}"
     */
    @Override
    public String visit(ClassExtendsDeclaration n, Void argu) throws Exception {
        String classname = n.f1.accept(this, null);
        String type_b=n.f3.accept(this, null);
        
        class_visited=classname;
        b.add_class_decl(classname);
        b.add_extend_decl(classname+" extends "+type_b);
     

        for ( Node node: n.f5.nodes){
            StringTokenizer st = new StringTokenizer(node.accept(this,null));
            String temp=st.nextToken();
            String var_name=st.nextToken();
            if (temp.equals("int")){
       
                
            }
            else if (temp.equals("boolean")){
 
                

            }
            else if(temp.equals("int[]") || temp.equals("boolean[]")){

                
            }
            else{
       
                
            }
            b.add_field(classname, temp+" "+var_name);
        }

        
        for ( Node node: n.f6.nodes){
            String fnc=node.accept(this,null);
            b.add_field(classname,fnc);
            // b.add_method(class_visited, fnc);
            func_memory+=8;


        }
        b.merge_fields(classname);

      

        System.out.println();

        return null;
    }

    /**
     * f0 -> "public"
     * f1 -> Type()
     * f2 -> Identifier()
     * f3 -> "("
     * f4 -> ( FormalParameterList() )?
     * f5 -> ")"
     * f6 -> "{"
     * f7 -> ( VarDeclaration() )*
     * f8 -> ( Statement() )*
     * f9 -> "return"
     * f10 -> Expression()
     * f11 -> ";"
     * f12 -> "}"
     */
    @Override
    public String visit(MethodDeclaration n, Void argu) throws Exception {
        String argumentList = n.f4.present() ? n.f4.accept(this, null) : "";

        String myType = n.f1.accept(this, null);
        String myName = n.f2.accept(this, null);
        String res=myType  +" " +myName + "(" +argumentList+")";
        b.add_name(class_visited, myName);
        b.add_method_decl(class_visited, res);
           
        List<String> arguments=new ArrayList<String>();
        if(! argumentList.equals("")){
            arguments = Arrays.asList(argumentList.split(","));
            
            
        }
        b.add_method(class_visited, res,arguments);
        
       
        for ( Node node: n.f7.nodes){
            String vardecl=node.accept(this,null);
            b.add_method_field(class_visited, res, vardecl);
            func_memory+=8;


        }
      
     
        
        return res;
    }




       /**
 * Grammar production:
 * f0 -> Type()
 * f1 -> Identifier()
 * f2 -> ";"
     * 
 */ 
 public String visit(VarDeclaration n,Void argu) throws Exception{
    // return n.f1.accept(this,null);
    n.f0.accept(this,null);
    n.f1.accept(this,null);
    n.f2.accept(this,null);
    return n.f0.accept(this,null)+" "+ n.f1.accept(this,null);
    
    
}/** 
 * Grammar production:
 * f0 -> "public"
 * f1 -> Type()
 * f2 -> Identifier()
 * f3 -> "("
 * f4 -> ( FormalParameterList() )?
 * f5 -> ")"
 * f6 -> "{"
 * f7 -> ( VarDeclaration() )*
 * f8 -> ( Statement() )*
 * f9 -> "return"
 * f10 -> Expression()
 * f11 -> ";"
 * f12 -> "}"
 */
// public String visit(MethodDeclaration m) throws Exception {
//     return m.f3.accept(this,null).toString();
// }


    /**
     * f0 -> FormalParameter()
     * f1 -> FormalParameterTail()
     */
    @Override
    public String visit(FormalParameterList n, Void argu) throws Exception {
        String ret = n.f0.accept(this, null);

        if (n.f1 != null) {
            ret += n.f1.accept(this, null);
        }

        return ret;
    }

    /**
     * f0 -> FormalParameter()
     * f1 -> FormalParameterTail()
     */
    public String visit(FormalParameterTerm n, Void argu) throws Exception {
        return n.f1.accept(this, argu);
    }

    /**
     * f0 -> ","
     * f1 -> FormalParameter()
     */
    @Override
    public String visit(FormalParameterTail n, Void argu) throws Exception {
        String ret = "";
        for ( Node node: n.f0.nodes) {
            ret += ", " + node.accept(this, null);
           
        }

        return ret;
    }

    /**
     * f0 -> Type()
     * f1 -> Identifier()
     */
    @Override
    public String visit(FormalParameter n, Void argu) throws Exception{
        String type = n.f0.accept(this, null);
        String name = n.f1.accept(this, null);
        return type + " " + name;
    }

    @Override
    public String visit(IntegerArrayType n, Void argu) {
        return "int[]";
    }
    @Override
    public String visit(BooleanArrayType n, Void argu) {
        return "boolean[]";
    }

    public String visit(BooleanType n, Void argu) {
        return "boolean";
    }

    public String visit(IntegerType n, Void argu) {
        return "int";
    }

    @Override
    public String visit(Identifier n, Void argu) {
        return n.f0.toString();
    }


/**
 * Grammar production:
 * f0 -> AndExpression()
 *       | CompareExpression()
 *       | PlusExpression()
 *       | MinusExpression()
 *       | TimesExpression()
 *       | ArrayLookup()
 *       | ArrayLength()
 *       | MessageSend()
 *       | Clause()
 */
// public String visit(Expression n , Void argu){
//     return "hi";
// }
// public String visit(AndExpression n,Void argu){
//     return "hi";
// }


/**
* Grammar production:
* f0 -> Identifier()
* f1 -> "="
* f2 -> Expression()
* f3 -> ";"
*/
public String visit(AssignmentStatement n,Void argu){
    return "hi";
}

}
