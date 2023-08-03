package visitor;
import syntaxtree.*;
import visitor.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.*;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

public class SecondVisitor  extends GJDepthFirst<String, Void> {
    public SymbolTable b;
    public String class_visited;
    public String last_expr;
    public boolean method_mode;
    public String method_visited;
   public  SecondVisitor(SymbolTable table){ this.b=table;}



   @Override
   public String visit(MainClass n, Void argu) throws Exception {
       String classname = n.f1.accept(this, null);
       class_visited="main";

  
       for ( Node node: n.f14.nodes){

           StringTokenizer st = new StringTokenizer(node.accept(this,null));
           String temp=st.nextToken();
           String var_name=st.nextToken();
        

           if (temp.equals("int")){
         
        }
        else if (temp.equals("boolean")){
       
        }
        else if(temp.equals("int[]") || temp.equals("boolean[]")){}
        else if(temp.isEmpty()==false){
               b.typecheck(temp, var_name);
           }
       }
      super.visit(n, argu);
       return null;
    }




    @Override
    public String visit(ClassDeclaration n, Void argu) throws Exception {
        String classname = n.f1.accept(this, null);
        class_visited=classname;

   
        for ( Node node: n.f3.nodes){
            StringTokenizer st = new StringTokenizer(node.accept(this,null));
            String temp=st.nextToken();
            String var_name=st.nextToken();
            if (temp.equals("int")){
               
            }
            else if (temp.equals("boolean")){
               

            }
            else if(temp.equals("int[]") || temp.equals("boolean[]")){}
            else{
               
                b.typecheck(temp, var_name);
            }
     
        }

           super.visit(n, argu);
     

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
        String type_b= n.f3.accept(this, null);
        class_visited=classname;
       if(!b.class_declared(type_b)){
           System.out.println("Cant Extend class "+ type_b+" that hasnt been declared");
           throw new Exception("Cant Extend class that hasn't been declared");

       }

    

         
        for ( Node node: n.f5.nodes){
            StringTokenizer st = new StringTokenizer(node.accept(this,null));
            String temp=st.nextToken();
            String var_name=st.nextToken();
            if (temp.equals("int")){
               
            }
            else if (temp.equals("boolean")){
               

            }
            else if(temp.equals("int[]") || temp.equals("boolean[]")){}
            else{
               
                b.typecheck(temp, var_name);
               
            }
     
        }
       


        
     

       super.visit(n, argu);
      // super.visit(n, argu);

        System.out.println();

        return null;
    }
    public boolean confirm_type(String a,String b){
        if(a.equals(b)){
            return true;
        }
        else if(a.equals("boolean")){
            if(b.equals("true") || b.equals("false") || b.equals("boolean"))
            return true;
        }
        else if (a.equals("int")){
            if(b.equals("int") || b.equals("##INT_LIT") )
            return true;
        }
        if(subtype(a, b)){
            return true;
        }
      
    return false;
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
           
      
        method_visited=res;
        method_mode=true;  
        for ( Node node: n.f7.nodes){
            String vardecl=node.accept(this,null);
            

        }
        for ( Node node: n.f8.nodes){
            String statement=node.accept(this,null);
            

        }
        String return_check=n.f10.accept(this,null);
        if(return_check.equals("this")){return_check=class_visited;}
        else{return_check=b.method_expr_check(class_visited, res, return_check);}
        if(!confirm_type(myType,return_check)){
            System.out.print(" In function "+myName+" in class "+class_visited+" return value doesn't match with "+myType);
            throw new Exception(" In function "+myName+" in class "+class_visited+" return value doesn't match with "+myType);

        }
        b.check_overload(class_visited, myName);
        method_mode=false;



  
        
      return res;
    }

           /**
 * Grammar production:
 * f0 -> Type()
 * f1 -> Identifier()
 * f2 -> ";"
     * 
 */ 
@Override
 public String visit(VarDeclaration n,Void argu) throws Exception{
    // return n.f1.accept(this,null);
    n.f0.accept(this,null);
   String idntf= n.f1.accept(this,null);
    n.f2.accept(this,null);
    if(method_mode){
        b.method_typecheck(class_visited, method_visited,idntf);
            
    }


   
    return n.f0.accept(this,null)+" "+ n.f1.accept(this,null);
    
    
}
/** PROSOXH !!!!!!!!!!!!!!!!!!!!!!
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
    public String visit(ArrayType n, Void argu) {
        return "int[]";
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

public boolean subtype(String type_a,String type_b){return this.b.subtype_test(type_a, type_b);}

public boolean is_bool(String a){
    return (a.equals("true") || a.equals("false") || a.equals("boolean"));
}
public boolean is_int(String a){
    return(a.equals("int") || a.equals("##INT_LIT"));
}
public boolean is_array(String a){
    return(a.equals("int[]") || a.equals("boolean[]"));
}
public String array_type(String a){
    if(a.equals("boolean[]")){
        return "boolean";
    }
    else{
        return "int";
    }
}


    /**
 * Grammar production:
 * f0 -> Identifier()
 * f1 -> "="
 * f2 -> Expression()
 * f3 -> ";"
 */
@Override
    public String  visit(AssignmentStatement n,Void argu) throws Exception{
        String identifier_name=n.f0.accept(this,null).toString();
        String  exprs=n.f2.accept(this,null).replaceAll("\\s", "");
        String type=" ";
        if(method_mode){
            type=b.method_assign_check(class_visited, method_visited, identifier_name);
            exprs=b.method_expr_check(class_visited, method_visited,  exprs);
            if(exprs.equals("this")){exprs=class_visited;}
            if(!confirm_type(type, exprs)){
                System.out.println("Bad assignment of "+exprs+" to " + identifier_name +" which is type of "+type+" In function "+method_visited+" in class "+class_visited);
                throw  new Exception("bad asignment");
            }

        }
        else{
        
        if(b.class_contains_field("main", exprs)){
            exprs=b.get_field_types("main", exprs);
        }
        b.check_identifier(class_visited,identifier_name);
        type=b.get_field_types("main", identifier_name);    
        if(!confirm_type(type, exprs)){
            System.out.println("Bad assignment of "+exprs+" to " + identifier_name +" which is type of "+type);
            throw  new Exception("bad asignment");
        }
    }

        return type;
    };
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


    public String visit(Expression n,Void argu) throws Exception{
        String res=n.f0.accept(this,null);
        last_expr=res;
        

        return res;

    }



        /**
 * Grammar production:
 * f0 -> PrimaryExpression()
 * f1 -> "."
 * f2 -> Identifier()
 * f3 -> "("
 * f4 -> ( ExpressionList() )?
 * f5 -> ")"
 */
@Override
public String visit(MessageSend n,Void argu)throws Exception {
    String first=n.f0.accept(this, null);
    String idntf=n.f2.accept(this, null);
    String class_to_use;

    String argumentList = n.f4.present() ? n.f4.accept(this, null) : "";
    List<String> arguments=new ArrayList<String>();
    List<String> argument_types=new ArrayList<String>();
    String type;
    if(first.equals("this")){
       
        class_to_use=class_visited;
        if(class_visited.equals("main")){
            System.out.println(" Main does not have fields hence -this- cant be used ");
            throw new Exception("ivalid use of -this- in main");
        }
    
     
    }
    else{
    if(b.class_declared(first)){class_to_use=first;}
    else{ 
        if(method_mode)
        class_to_use=b.method_expr_check(class_visited, method_visited, first);
        else
        class_to_use=b.get_field_types(class_visited,first);
    }
        
    }  //class_to_use=b.get_field_types(class_visited,first)
     if(! argumentList.equals("")){
       arguments = Arrays.asList(argumentList.split(","));
       for(int i=0;i<arguments.size();i++){
        if(method_mode){
          type= b.method_expr_check(class_visited, method_visited, arguments.get(i));
          argument_types.add(type); 
        }
        else{
        if(b.class_contains_field(class_visited,arguments.get(i))){
            type=b.get_field_types(class_visited,arguments.get(i));
            argument_types.add(type);
    
        }
        else{
            type=arguments.get(i);
            argument_types.add(type);
        }
       } 
    }
    return b.check_this(class_to_use, idntf, argument_types,class_visited);
    }
    else{
        return b.name_to_type(class_to_use, idntf);
    } 


    
  

}




     /**
 * Grammar production:
 * f0 -> PrimaryExpression()
 * f1 -> "["
 * f2 -> PrimaryExpression()
 * f3 -> "]"
 */

public String visit(ArrayLookup n,Void argu ) throws Exception{
    String first=n.f0.accept(this,null);
    String second=n.f2.accept(this,null);
    if(method_mode){
        first=b.method_expr_check(class_visited,method_visited, first);
        second=b.method_expr_check(class_visited,method_visited, second);

    }
    else{
    if(b.class_contains_field(class_visited, first)){
        first=b.get_field_types(class_visited,first);

    }
        
    if(b.class_contains_field(class_visited,second)){
       second=b.get_field_types(class_visited,second);

    }
    }
    if(is_int(second)==false){
        System.out.println(second+" is not an int so you cant index an array with it");
        throw new Exception(" Tried to use non-i object as an array index");

    }
    if(is_array(first) ){
        return  array_type(first);
    }
    System.out.println(first+" is not an array");
    throw new Exception(" Tried to use non-array object as an array");

}


          /**
 * Grammar production:
 * f0 -> PrimaryExpression()
 * f1 -> "*"
 * f2 -> PrimaryExpression()
 */

public String visit(TimesExpression n ,Void argu) throws Exception {
    String first= n.f0.accept(this,null);
     String second=n.f2.accept(this,null);

     if(method_mode){
        first=b.method_expr_check(class_visited,method_visited, first);
        second=b.method_expr_check(class_visited,method_visited, second);

    }
    else{
    if(b.class_contains_field(class_visited, first)){
        first=b.get_field_types(class_visited,first);

    }
        
    if(b.class_contains_field(class_visited,second)){
       second=b.get_field_types(class_visited,second);

    }
    }


     if(is_int(first) && is_int(second)){
          return "int";
     }
     else{
         System.out.println("TimesExpression requires 2 int variables the 2 variables given where type of "+first+" and "+  second);
         throw new Exception("Implicit Declaration of TimesExpression");
     }
 }



      /**
 * Grammar production:
 * f0 -> PrimaryExpression()
 * f1 -> "-"
 * f2 -> PrimaryExpression()
 */

public String visit(MinusExpression n ,Void argu) throws Exception {
    String first= n.f0.accept(this,null);
     String second=n.f2.accept(this,null);

     if(method_mode){
        first=b.method_expr_check(class_visited,method_visited, first);
        second=b.method_expr_check(class_visited,method_visited, second);

    }
    else{
    if(b.class_contains_field(class_visited, first)){
        first=b.get_field_types(class_visited,first);

    }
        
    if(b.class_contains_field(class_visited,second)){
       second=b.get_field_types(class_visited,second);

    }
    }
     if(is_int(first) && is_int(second)){
          return "int";
     }
     else{
         System.out.println(" Minus requires 2 int variables the 2 variables given where type of "+first+" and "+  second);
         throw new Exception("Minus Declaration of TimesExpression");
     }
 }



    /**
 * Grammar production:
 * f0 -> PrimaryExpression()
 * f1 -> "+"
 * f2 -> PrimaryExpression()
 */

    public String visit(PlusExpression n ,Void argu) throws Exception {
        String first= n.f0.accept(this,null);
        String second=n.f2.accept(this,null);
        if(method_mode){
            first=b.method_expr_check(class_visited,method_visited, first);
            second=b.method_expr_check(class_visited,method_visited, second);
    
        }
        else{
        if(b.class_contains_field(class_visited, first)){
            first=b.get_field_types(class_visited,first);
    
        }
            
        if(b.class_contains_field(class_visited,second)){
           second=b.get_field_types(class_visited,second);
    
        }
        }
        if(is_int(first) && is_int(second)){
             return "int";
        }
        else{
            System.out.println("Plus requires 2 int variables the 2 variables given where type of "+first+" and "+  second);
            throw new Exception("Implicit Declaration of PlusExpression");
        }
     }



    /**
 * Grammar production:
 * f0 -> PrimaryExpression()
 * f1 -> "<"
 * f2 -> PrimaryExpression()
 */
    public String visit(CompareExpression n ,Void argu) throws Exception {
        String first= n.f0.accept(this,null);
         String second=n.f2.accept(this,null);
         if(method_mode){
            first=b.method_expr_check(class_visited,method_visited, first);
            second=b.method_expr_check(class_visited,method_visited, second);
    
        }
        else{
        if(b.class_contains_field(class_visited, first)){
            first=b.get_field_types(class_visited,first);
    
        }
            
        if(b.class_contains_field(class_visited,second)){
           second=b.get_field_types(class_visited,second);
    
        }
        }
         if(is_int(first) && is_int(second)){
              return "boolean";
         }
         else{
             System.out.println("CompareExpression requires 2 int variables the 2 variables given where type of "+first+" and "+  second);
             throw new Exception("Implicit Declaration of CompareExpression");
         }
     }



    /**
 * Grammar production:
 * f0 -> Clause()
 * f1 -> "&&"
 * f2 -> Clause()
 */
    public String visit(AndExpression n ,Void argu) throws Exception {
        String first= n.f0.accept(this,null);
        String second=n.f2.accept(this,null);
        if(method_mode){
            first=b.method_expr_check(class_visited,method_visited, first);
            second=b.method_expr_check(class_visited,method_visited, second);
    
        }
        else{
        if(b.class_contains_field(class_visited, first)){
            first=b.get_field_types(class_visited,first);
    
        }
            
        if(b.class_contains_field(class_visited,second)){
           second=b.get_field_types(class_visited,second);
    
        }
        }
        if(is_bool(first) && is_bool(second)){
             return "boolean";
        }
        else{
            System.out.println("AndExpression requires 2 boolean variables the 2 variables given where type of "+first+" and "+  second);
            throw new Exception("Implicit Declaration of AndExpression");
        }
    }

    
    @Override
    /**
    * Grammar production:
    * f0 -> "!"
    * f1 -> Clause()
    */
    public String visit(NotExpression n,Void argu) throws Exception{
        String res=n.f1.accept(this,null);
        if(method_mode){
            res=b.method_expr_check(class_visited, method_visited,  res);
        }
       else{
            if(b.class_contains_field(class_visited, res)){
                res=b.get_field_types(class_visited,res);
        
            }
       }
       if(!is_bool(res)){
           System.out.println("Invalid use of No (!) Expression  ");
           throw new Exception("Invalid use of No (!) Expression  ");
       }
            
        return "boolean";
    }

    /**
    * Grammar production:
    * f0 -> NotExpression()
    *       | PrimaryExpression()
    */
    @Override
    public String visit(Clause n,Void argu) throws Exception{
        
        String res=n.f0.accept(this,null);
        if(res==null){
            return null;
        }
        return res;
    }

  /**
 * Grammar production:
 * f0 -> IntegerLiteral()
 *       | TrueLiteral()
 *       | FalseLiteral()
 *       | Identifier()
 *       | ThisExpression()
 *       | ArrayAllocationExpression()
 *       | AllocationExpression()
 *       | BracketExpression()
 */

/**
 * Grammar production:
 * f0 -> "new"
 * f1 -> Identifier()
 * f2 -> "("
 * f3 -> ")"
 */
    public String visit(AllocationExpression n,Void argu) throws Exception{
        return n.f1.accept(this,null);
            // return n.f1.accept(this,null);
        
        }

    @Override
    public String visit(PrimaryExpression n,Void argu) throws Exception{
        


        return n.f0.accept(this,argu);
    }
    
    @Override
    /**
 * Grammar production:
 * f0 -> "("
 * f1 -> Expression()
 * f2 -> ")"
 */
    public String visit(BracketExpression n,Void argu) throws Exception{
        


        return n.f1.accept(this,argu);
    }

    @Override
    public String visit(BooleanArrayType n,Void argu) throws Exception{
        


    return "boolean[]";
    }

    @Override
    public String visit(IntegerArrayType n,Void argu) throws Exception{
        


    return "int[]";
    }
    @Override
    public String visit(IntegerLiteral n,Void argu) throws Exception{
        


    return "##INT_LIT";
    }
    @Override
    public String visit(TrueLiteral n,Void argu) throws Exception{
        


    return "true";
    }
    @Override
    public String visit(FalseLiteral n,Void argu) throws Exception{
        


    return "false";
    }
    @Override
    public String visit(ThisExpression n,Void argu) throws Exception{
        


    return "this";
    }
    
    /**
 * Grammar production:
 * f0 -> BooleanArrayAllocationExpression()
 *       | IntegerArrayAllocationExpression()
 */
    public String visit(ArrayAllocationExpression n,Void argu) throws Exception {
        return n.f0.accept(this,null);


    }

    
/**
 * Grammar production:
 * f0 -> PrimaryExpression()
 * f1 -> "."
 * f2 -> "length"
 */
    public String visit(ArrayLength n,Void argu) throws Exception {
        String a=n.f0.accept(this,null);
        if(method_mode){a=b.method_expr_check(class_visited, method_visited,a );}
       else if(b.class_contains_field(class_visited, a)){
           a=b.get_field_types(class_visited, a);

       }
        if(!is_array(a)){
            System.out.println("Non array expression does not have .len atribute");
            throw new Exception("Non array expression does not have .len atribute");
        }
        return "int";



    }
    
   

  
    /**
 * Grammar production:
 * f0 -> "new"
 * f1 -> "int"
 * f2 -> "["
 * f3 -> Expression()
 * f4 -> "]"
 */
    public String visit(IntegerArrayAllocationExpression n,Void argu)throws Exception{ 
       String a=n.f3.accept(this,null);
       if(method_mode){a=b.method_expr_check(class_visited, method_visited,a );}
       else if(b.class_contains_field(class_visited, a)){
           a=b.get_field_types(class_visited, a);

       }
       if(is_int(a)){
           return "int[]";
       }
       else{
       System.out.println("Array size must be int ");
       throw new Exception("invalid array size");
       }

    }

        /**
 * Grammar production:
 * f0 -> "new"
 * f1 -> "boolean"
 * f2 -> "["
 * f3 -> Expression()
 * f4 -> "]"
 */

    public String visit(BooleanArrayAllocationExpression n,Void argu) throws Exception {
       
        String a=n.f3.accept(this,null);
        if(method_mode){a=b.method_expr_check(class_visited, method_visited,a );}
        else if(b.class_contains_field(class_visited, a)){
            a=b.get_field_types(class_visited, a);
 
        }
        if(is_int(a)){
            return "boolean[]";
        }
        else{
        System.out.println("Array size must be int ");
        throw new Exception("invalid array size");
        }

    }

    // public String visit(ThisExpression n,Void argu) throws Exception{
    //     return class_visited;

    // }


   String form_parameters(String a){
    
        if(a.equals("true") || a.equals("false") )
        return "boolean";
    
     
        if( a.equals("##INT_LIT") )
        return "int";

        return a;
    }
   

    @Override
    /**
    * Grammar production:
    * f0 -> ","
     * f1 -> Expression()
     */
    public String visit(ExpressionTerm n, Void argu) throws Exception{ return ","+form_parameters(n.f1.accept(this,null));}
    /**
    * Grammar production:
    * f0 -> ( ExpressionTerm() )*
    */
    @Override
    public String visit(ExpressionTail n, Void argu) throws Exception{ 
        String res="";
        for ( Node node: n.f0.nodes){
            res+=node.accept(this,null);
        }
        return res;
    }
    @Override
    /**
 * Grammar production:
 * f0 -> Expression()
 * f1 -> ExpressionTail()
 */
    public String visit(ExpressionList n, Void argu) throws Exception{
        String type = form_parameters(n.f0.accept(this,null));
        String name = n.f1.accept(this,null);
        if(name==null){name="";}
        return type+name;
    }


    
/**
 * Grammar production:
 * f0 -> "if"
 * f1 -> "("
 * f2 -> Expression()
 * f3 -> ")"
 * f4 -> Statement()
 * f5 -> "else"
 * f6 -> Statement()
 */
    public String visit(IfStatement n,Void argu) throws Exception{
        String res=n.f2.accept(this,null);
        if(method_mode){
            res=b.method_expr_check(class_visited, method_visited,  res);
        }
       else{
            if(b.class_contains_field(class_visited, res)){
                res=b.get_field_types(class_visited,res);
        
            }
       }
       if(!is_bool(res)){
           System.out.println("Invalid use of if Expression  ");
           throw new Exception("Invalid use of if Expression  ");
       }
       n.f4.accept(this,null);
       n.f6.accept(this,null);

        return null;
    }
    /**
 * Grammar production:
 * f0 -> "while"
 * f1 -> "("
 * f2 -> Expression()
 * f3 -> ")"
 * f4 -> Statement()
 */
@Override
    public String visit(WhileStatement n,Void argu) throws Exception{
        String res=n.f2.accept(this,null);
        if(method_mode){
            res=b.method_expr_check(class_visited, method_visited,  res);
        }
       else{
            if(b.class_contains_field(class_visited, res)){
                res=b.get_field_types(class_visited,res);
        
            }
       }
       if(!is_bool(res)){
           System.out.println("Invalid use of while Expression  ");
           throw new Exception("Invalid use of while Expression  ");
       }
       n.f4.accept(this,null);
       return null;
    }
    /**
 * Grammar production:
 * f0 -> "System.out.println"
 * f1 -> "("
 * f2 -> Expression()
 * f3 -> ")"
 * f4 -> ";"
 */
    @Override
    public String visit(PrintStatement n,Void argu) throws Exception{
        String res=n.f2.accept(this,null);
        if(method_mode){
            res=b.method_expr_check(class_visited, method_visited,  res);
        }
       else{
            if(b.class_contains_field(class_visited, res)){
                res=b.get_field_types(class_visited,res);
        
            }
       }
       if(!is_bool(res) && !is_int(res)){
           System.out.println("Invalid use of Print Expression  ");
           throw new Exception("Invalid use of Print Expression  ");
       }
       return null;
    }


    
}
