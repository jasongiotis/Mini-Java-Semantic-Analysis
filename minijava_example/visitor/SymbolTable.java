package visitor;

import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

import javax.print.DocFlavor.STRING;
import javax.print.attribute.standard.MediaSize.NA;

import org.omg.CORBA.INTERNAL;
class Method_node{
    public String method_name;
    public ArrayList<String> method_fields;
    public ArrayList<String> method_field_names;
    public ArrayList<String> method_field_types ; 
    public List<String> arguments;
    public List<String> argument_names;
    public List<String> argument_types;
    
    Method_node(String name,List<String> lst ) throws Exception{
        method_name=name;
        method_field_names=new ArrayList<String>();
        method_field_types=new ArrayList<String>();   
        method_fields=new ArrayList<String>();
        arguments=lst ;
        argument_names=new ArrayList<String>();
        argument_types=new ArrayList<String>();
        for(int i=0;i<arguments.size();i++){
            StringTokenizer st = new StringTokenizer(arguments.get(i));
            argument_types.add(st.nextToken());
            argument_names.add(st.nextToken());
            
        }
        
        for(int i=0;i<argument_names.size();i++){
            int occurrences = Collections.frequency(argument_names,argument_names.get(i));
            if(occurrences>1){
                // System.out.print("Method argument "+argument_names.get(i)+" redecleared in method "+method_name);
                throw new Exception("Redecleared function argument");
            }

        }

    }
    public void set_args(List<String> lst){
        this.arguments=lst;
    }
    public String get_argument_type(String field_name){
        int i= this.argument_names.indexOf(field_name);
        return this.argument_types.get(i);
  
      }





public void add_field(String field)throws Exception{
        if(this.method_fields.contains(field)){
            // System.out.print("Field "+field+ " redecleared  in method "+method_name);
            throw new Exception("Field redecleared Error");
        }
        this.method_fields.add(field);
        StringTokenizer st = new StringTokenizer(field);
        method_field_types.add(st.nextToken());
        String check=st.nextToken().replaceAll("\\s", "");
        if(method_field_names.contains(check)){
            // System.out.print("Field "+check+ " redecleared  in method "+method_name);
            throw new Exception("Field redecleared Error");

        }
        this.method_field_names.add(check);

    }
    public boolean method_contains_field(String field_name){
        return this.method_field_names.contains(field_name);
    }

    public String get_field_type(String field_name){
      int i= this.method_field_names.indexOf(field_name);
      return this.method_field_types.get(i);

    }
    public void print(String classname){

        System.out.println("------- For method "+method_name+" -------------");
        int occurrences = Collections.frequency(argument_names, "i");
        System.out.println("------- arguments are "+argument_names+" -------------");

        for(int i=0;i<this.method_fields.size();i++){
            System.out.println(this.method_fields.get(i));
        }
  
    }


        
}

public class SymbolTable {
    public ArrayList<String> declarations;
    public ArrayList<String> extend_declarations;
    public Map<String, ArrayList<String>> classes ;
    public Map<String, ArrayList<String>> field_names;
    public Map<String, ArrayList<String>> field_types ; 
    public Map<String, ArrayList<String>> method_names;
    public Map<String, ArrayList<String>> method_decls;
    public Map<String, ArrayList<Method_node>> Methods ;
    public  Map<String, String >extend_map;
    public  Map<String, String >parent_map;
    public Map<String, ArrayList<String>> individual_vars;
    public Map<String, ArrayList<String>> individual_func;
    public Map<String, ArrayList<Integer>>parent_offset;
    
    

    
    public SymbolTable(){
        declarations=new ArrayList<String>();
        extend_declarations=new ArrayList<String>();
        extend_map=new HashMap<String,String>();
        parent_map=new HashMap<String,String>();
        classes = new HashMap<String, ArrayList<String>>();
        field_names=new HashMap<String, ArrayList<String>>();
        field_types=new HashMap<String, ArrayList<String>>();
        method_names=new HashMap<String, ArrayList<String>>();
        method_decls=new HashMap<String, ArrayList<String>>();
        Methods=new HashMap<String, ArrayList<Method_node>>(); 
        individual_vars=new HashMap<String, ArrayList<String>>();
        individual_func=new HashMap<String, ArrayList<String>>();
        parent_offset=new HashMap<String, ArrayList<Integer>>();
        

    }
    public void add_class_decl(String decl) throws Exception{
        if(this.declarations.contains(decl)){
            System.out.print("class "+decl +" was redeclered");
            throw new Exception("Class redecleared Error");

        }
        this.declarations.add(decl);
        this.classes.put(decl, new  ArrayList<String>());
        this.field_names.put(decl, new  ArrayList<String>());
        this.field_types.put(decl, new  ArrayList<String>());
        this.Methods.put(decl,new  ArrayList<Method_node>());
        this.method_names.put(decl,new  ArrayList<String>());
        this.method_decls.put(decl,new  ArrayList<String>());
        this.extend_map.put(decl, "null");

        
    }
    public String get_field_types(String class_name, String field_name) {
    
    int i= (field_names.get(class_name)).indexOf(field_name);
    return field_types.get(class_name).get(i);
     

    }
    public void add_field(String class_name,String field)throws Exception{
        if(this.classes.get(class_name).contains(field)){
            System.out.println("In class "+class_name+" "+field+" was redecleared");
            throw new Exception("Field redecleard Error");
        }
        this.classes.get(class_name).add(field);
        StringTokenizer st = new StringTokenizer(field);
        String type=st.nextToken();
        field_types.get(class_name).add(type);
        String check=st.nextToken();
        if( this.field_names.get(class_name).contains(check)){
            System.out.println("In class "+class_name+" "+check+" was redecleared");
            throw new Exception("variable redeclered");
        }
        this.field_names.get(class_name).add(check);

    }
    public boolean class_contains_field(String classname, String field_name){
        return this.field_names.get(classname).contains(field_name);
    }
   
    public void typecheck(String class_name,String type) throws Exception{
        if(declarations.contains(class_name)==false){
            System.out.println("type "+class_name +" is invalid/undecleared");
            throw new Exception("Invalid type");
        }

    }
   public void check_identifier(String classname,String identif) throws   Exception{
        // System.out.println("classname ins "+classname + identif+" <- identifier is");
        if(class_contains_field(classname, identif)==false){
           System.out.println("variable "+identif +" not declared");
           throw new Exception("Udecleared variable");
        }
    }
  public void add_method(String class_name,String method,List<String> args) throws Exception{
      this.Methods.get(class_name).add(new Method_node(method,args));
      
      
  }
  public void add_method_field(String class_name,String method,String field) throws Exception{
    ArrayList<Method_node> tlist=Methods.get(class_name);
    for (int i = 0; i < tlist.size(); i++) {
        if(tlist.get(i).method_name.equals(method)){
            tlist.get(i).add_field(field);
  
        }
        
    }
   
   
  }
  
 public void add_extend_decl(String decl){
    
     this.extend_declarations.add(decl);
     StringTokenizer st=new StringTokenizer(decl);
     String frist_class=st.nextToken();
     st.nextToken();
     String second_class=st.nextToken();
     extend_map.put( frist_class,second_class);
     parent_map.put(second_class,frist_class);
}

public boolean subtype_test(String type_a ,String type_b){
    return extend_declarations.contains(type_b+" extends "+type_a);
}
public boolean class_declared(String class_name){return declarations.contains(class_name);}


public void method_typecheck(String class_name,String method,String idnf) throws Exception{
    ArrayList<Method_node> tlist=Methods.get(class_name);
    for (int i = 0; i < tlist.size(); i++) {
        if(tlist.get(i).method_name.equals(method)){
            if(tlist.get(i).argument_names.contains(idnf)){
                System.out.println("Redeclered method argument "+idnf +" at method "+method+" of class "+class_name);
                throw new Exception("Redeclered method argument");
            }
  
      }
        
}
}
public String method_assign_check(String class_name,String method,String idntf) throws Exception{
    Method_node found=null;
    ArrayList<Method_node> tlist=Methods.get(class_name);
    for (int i = 0; i < tlist.size(); i++) {
        if(tlist.get(i).method_name.equals(method)){
            found=tlist.get(i);
            break;
        }
    }
    if(found.argument_names.contains(idntf)){
        return found.get_argument_type(idntf);

    }
    else if(found.method_field_names.contains(idntf)){
        return found.get_field_type(idntf);
    }
    else if(class_contains_field(class_name, idntf)){
        return get_field_types(class_name, idntf);
    }
    else{
        System.out.println("Undeclered variable "+idntf+" in method "+method+" in class "+class_name);
        throw new Exception("Undeclered variable");
    }



    
}


public String method_expr_check(String class_name,String method,String idntf) throws Exception{
    Method_node found=null;
    ArrayList<Method_node> tlist=Methods.get(class_name);
    for (int i = 0; i < tlist.size(); i++) {
        if(tlist.get(i).method_name.equals(method)){
            found=tlist.get(i);
            break;
        }
    }
    if(found.argument_names.contains(idntf)){
        return found.get_argument_type(idntf);

    }
    else if(found.method_field_names.contains(idntf)){
        return found.get_field_type(idntf);
    }
    else if(class_contains_field(class_name, idntf)){
        return get_field_types(class_name, idntf);
    }
    else{
        return idntf;
    }



    
}

public void add_name(String class_name,String name){
    method_names.get(class_name).add(name);
}
public void add_method_decl(String class_name,String decl){
    method_decls.get(class_name).add(decl);
}

public String name_todecl(String class_name,String name) throws Exception{
    if(!method_names.get(class_name).contains(name)){
        System.out.println("invalid arguments to -this- call "+class_name+name);
        throw new Exception("invalid arguments to -this- call");

    }
    int i=method_names.get(class_name).indexOf(name);
    return method_decls.get(class_name).get(i);
}
public String same_args(String class_name,String method, List<String> method2,String class_visited) throws Exception{
    Method_node found=null;
    int pos=0;
    ArrayList<Method_node> tlist=Methods.get(class_name);
    for (int i = 0; i < tlist.size(); i++) {
        if(tlist.get(i).method_name.equals(method)){
            found=tlist.get(i);
            pos=i;
            break;
        }
    }
    for (int i = 0; i < found.argument_types.size(); i++) {
        if(method2.get(i).equals("this")){ method2.set(i,class_visited);}
      
        if(subtype_test(found.argument_types.get(i),method2.get(i))){
            method2.set(i,found.argument_types.get(i));
            
        }
      

    
    }
    if(found.argument_types.equals(method2)){

        StringTokenizer st=new StringTokenizer(found.method_name);
        return st.nextToken();


    }

    System.out.println("not equal"+found.argument_types+method2);
    System.out.println("invalid arguments to -this- call in class "+class_name+" for "+method+" and "+found.method_name );
    throw new Exception("invalid arguments to -this- call");


}
public String check_this(String class_name,String name,List<String> method2,String class_visited)throws Exception{
    String tname=name_todecl(class_name, name);
    String res=same_args(class_name, tname, method2,class_visited);
    return res;
}
public String name_to_type(String class_name,String method) throws Exception{
    String method_name=name_todecl(class_name, method);
    Method_node found=null;
    int pos=0;
    ArrayList<Method_node> tlist=Methods.get(class_name);
    for (int i = 0; i < tlist.size(); i++) {
        if(tlist.get(i).method_name.equals(method_name)){
            found=tlist.get(i);
            pos=i;
            break;
        }
    }
    if(found.argument_names.size()==0){
        StringTokenizer st = new StringTokenizer(method_name);
        String type=st.nextToken();
        return type;
    }

return null;
}
 void check_overload(String class_name,String method_name)throws Exception{
     List <String> l=method_names.get(class_name);
    int occurrences = Collections.frequency(l,method_name);
     if(occurrences>1){
         System.out.println("overloaded "+method_name+" not allowed");
         throw new Exception("overloaded "+method_name+" not allowed");
     }

 }
public boolean same_methods(String class_a,String class_b,String method_name1,String method_name2){
    ArrayList<Method_node> tlist=Methods.get(class_a);
    Method_node first_method=null;
    Method_node second_method=null;
    // System.out.println("got names "+method_name1+method_name2);
    for (int i = 0; i < tlist.size(); i++) {
        if(tlist.get(i).method_name.equals(method_name1)){
            first_method=tlist.get(i);
            // pos=i;
            break;
        }
    }
    tlist=Methods.get(class_b);
    for (int i = 0; i < tlist.size(); i++) {
        if(tlist.get(i).method_name.equals(method_name2)){
            second_method=tlist.get(i);
            // pos=i;
            break;
        }
    }
    if(first_method.argument_types.size()==0 && second_method.argument_types.size()==0){
        return true;
    }
    return first_method.argument_types.equals(second_method.argument_types);
}
public ArrayList<String> get_class_vars(String class_name){
    ArrayList<String> res=new ArrayList<String>();
    List<String> l=classes.get(class_name);
    List<String> temp=method_decls.get(class_name);

    for(int i=0;i<l.size();i++){
        if(!temp.contains(l.get(i))){
            res.add(l.get(i));
        }
    }
    return res;
}


public void smartjoin( List<String> l,List<String> temp){


    for(int i=0;i<l.size();i++){
        if(!temp.contains(l.get(i))){
            temp.add(l.get(i));
        }
    }
   
}

public void merge_fields(String classs_name ) throws Exception{
    String mother_class=extend_map.get(classs_name);
    List <String> mother_methods=method_names.get(mother_class);
    List <String> child_methods=method_names.get(classs_name);
    List <String> mother_decls=method_decls.get(mother_class);
    List <String> child_decls=method_decls.get(classs_name);
    individual_vars.put(classs_name, get_class_vars(classs_name));
    individual_func.put(classs_name,new ArrayList<String>());
    for(int i=0;i<child_methods.size();i++){
        if(mother_methods.contains(child_methods.get(i))){
            for(int j=0 ; j<mother_methods.size();j++){
                if(mother_methods.get(j).equals(child_methods.get(i))){ 
                    if(!same_methods( mother_class,classs_name,mother_decls.get(j) ,child_decls.get(i))){
                        System.out.print("method "+child_methods.get(i)+" is invalid cause it overloads mother class method "+mother_methods.get(j));
                        throw new Exception("invalid overloading to child class");
                    }
                }
            }
        }
        else{
            individual_func.get(classs_name).add(child_methods.get(i));
        }

    }
    List <String> mother_vars=get_class_vars(mother_class);
    // System.out.println(" MOTHER VARS ARE "+mother_vars);
    List <String> mother_fields=classes.get(mother_class);
    List <String> child_fields=classes.get(classs_name);
    for(int i=0;i<mother_vars.size();i++ ){
        if(!child_fields.contains(mother_vars.get(i))){
            add_field(classs_name, mother_fields.get(i));
        }
    }
    for(int i=0;i<mother_methods.size();i++ ){
        if(!child_methods.contains(mother_methods.get(i))){
            Methods.get(classs_name).add(Methods.get(mother_class).get(i));
        }
    }
    smartjoin(method_decls.get(mother_class),method_decls.get(classs_name) );
    smartjoin(method_names.get(mother_class),method_names.get(classs_name) );
    smartjoin(classes.get(mother_class),classes.get(classs_name) );
    
    


}


public void print(){
    int var_set=0;
    int func_set=0;
    System.out.println("-------------- START OF SYMBOL TABLE ------------------------");
    for(int i=1;i<this.declarations.size();i++){
        System.out.println("-------CLASS "+declarations.get(i)+"----------");
        List<String> types=field_types.get(declarations.get(i));
        List<String> names=field_names.get(declarations.get(i));
        List<String> vars=get_class_vars(declarations.get(i));
        List<String> methods=method_names.get(declarations.get(i));
        if(extend_map.get(declarations.get(i)).equals("null")){
            String type;
            System.out.println("---VARIABLES----");
            for(int j=0;j<vars.size();j++){
              
                StringTokenizer st=new StringTokenizer(vars.get(j));
                type=st.nextToken();
                System.out.println(declarations.get(i)+"."+st.nextToken()+" "+var_set);
                if(type.equals("int")){
                    var_set+=4;
                }
                else if(type.equals("boolean")){
                    var_set+=1;
                }
                else{
                    var_set+=8;
                }

            }
            System.out.println("---METHODS----");
            for(int j=0;j<methods.size();j++){
                System.out.println(declarations.get(i)+"."+methods.get(j)+" "+func_set);
                func_set+=8;
                

            }
          
                parent_offset.put(parent_map.get(declarations.get(i)),new ArrayList<Integer>());
                parent_offset.get(parent_map.get(declarations.get(i))).add(var_set);
                parent_offset.get(parent_map.get(declarations.get(i))).add(func_set);
               
           
            func_set=0;
            var_set=0;
        }
        else{
            func_set=parent_offset.get(declarations.get(i)).get(1);
            var_set=parent_offset.get(declarations.get(i)).get(0);
            String type;
            List<String> vars1=individual_vars.get(declarations.get(i));
            List<String> methods1=individual_func.get(declarations.get(i));
            System.out.println("---VARIABLES----");
            for(int j=0;j<vars1.size();j++){
                
                StringTokenizer st=new StringTokenizer(vars1.get(j));
                type=st.nextToken();
                System.out.println(declarations.get(i)+"."+st.nextToken()+" "+var_set);
                if(type.equals("int")){
                    var_set+=4;
                }
                else if(type.equals("boolean")){
                    var_set+=1;
                }
                else{
                    var_set+=8;
                }

            }
            // var_set=0;
            System.out.println("---METHODS----");
            for(int j=0;j<methods1.size();j++){
                System.out.println(declarations.get(i)+"."+methods1.get(j)+" "+func_set);
                func_set+=8;
                

            }
        

        }
    }
    System.out.println("-------------- END OF SYMBOL TABLE ------------------------");

}



}