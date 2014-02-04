package cn.edu.fudan.se.helpseeking.astparser;

import java.io.BufferedInputStream;  
import java.io.FileInputStream;  
  



import java.util.Iterator;
import java.util.List;

import org.eclipse.jdt.core.dom.*;


public class MyASTParser {
	
//	要解析Java源码，首先要建立ASTParser的实例。此过程最重要的一点是，你要告诉parser需要解析的内容类型。ASTParser支持对以下四种内容的解析：
//	1. K_COMPILATION_UNIT： 一个编译单元，一般就是完整的Java文件
//	2. K_STATEMENTS： Java statements，比如赋值语句，或是if语句块，while语句块等。此类型不需要文件是完整的编译单元，但需要是完整的statements。比如if语句块要作为一个完整的statement输入，否则会报错。
//	3. K_EXPRESSION: Java expressions
//	4. K_CLASS_BODY_DECLARATIONS: Java class里的元素
	
	CompilationUnit ASTresult = null;
	
	

	public void  initialize(String content, int parserType) {
		// Initialize ASTParser
				ASTParser parser = ASTParser.newParser(AST.JLS3); //initialize	
				parser.setKind(parserType);	  //to parse compilation unit   K_COMPILATION_UNIT
				parser.setSource(content.toCharArray()); 		  //content is a string which stores the java source
				parser.setResolveBindings(true);
				
				
				switch (parserType) {
				case ASTParser.K_COMPILATION_UNIT:
					ASTresult=(CompilationUnit) parser.createAST(null);
					break;
//				case ASTParser.K_STATEMENTS:
//					ASTresult=(Statement) parser.createAST(null);
//					break;
//				case ASTParser.K_EXPRESSION:
//					ASTresult=(Expression) parser.createAST(null);
//					break;
//				case ASTParser.K_CLASS_BODY_DECLARATIONS:
//					ASTresult=(BodyDeclaration) parser.createAST(null);
//					break;

				default:
					
					break;
				}
				
							
	}
	
	public List  getImportList(CompilationUnit result) {
		//show import declarations in order
				List importList = result.imports();  
				System.out.println("import:");
				for(Object obj : importList) {
					ImportDeclaration importDec = (ImportDeclaration)obj;
					System.out.println(importDec.getName());
				}
		return importList;
	}
	
	public List  getType(CompilationUnit result) {
		//show class name
				List types = result.types();  
				TypeDeclaration typeDec = (TypeDeclaration) types.get(0); 
				System.out.println("className:"+typeDec.getName());
		return types;
	}
	
	public FieldDeclaration [] getFieldDeclaration(TypeDeclaration typeDec) {
		//show fields
		       
				FieldDeclaration fieldDec[]=typeDec.getFields();
				System.out.println("Fields:");
				for(FieldDeclaration field: fieldDec)
				{
					System.out.println("Field fragment:"+field.fragments());
					System.out.println("Field type:"+field.getType());
					
				}
		
		return fieldDec;
		
	}
	
	public MethodDeclaration [] getMethodDeclarations(TypeDeclaration typeDec) {
		//show methods
				MethodDeclaration methodDec[] = typeDec.getMethods();  
				System.out.println("Method:");  
				for (MethodDeclaration method : methodDec)  
				{  
					//get method name
					SimpleName methodName=method.getName();
					System.out.println("method name:"+methodName);
					
					//get method parameters
					List param=method.parameters();
					System.out.println("method parameters:"+param);
					
					//get method return type
					Type returnType=method.getReturnType2();
					System.out.println("method return type:"+returnType);
	}
				return methodDec;
	
	}

	
	public Block getBlock(MethodDeclaration method) {
//		一个方法的内容对应一个block，可以用getBody()得到；一个block又可以被分解成一系列statements，可以用statements()方法得到：
		//get method body
		Block body=method.getBody();
		List statements=body.statements();   //get the statements of the method body
		Iterator iter=statements.iterator();
		while(iter.hasNext())
		{
			//get each statement
			Statement stmt=(Statement)iter.next();
		}
		return body;
	}

	public void showStatement(Statement stmt) {
		
		
		if(stmt instanceof ExpressionStatement)
		{
			ExpressionStatement expressStmt=(ExpressionStatement) stmt;
			Expression express=expressStmt.getExpression();
			
			if(express instanceof Assignment)
			{
				Assignment assign=(Assignment)express;
				System.out.println("LHS:"+assign.getLeftHandSide()+"; ");
				System.out.println("Op:"+assign.getOperator()+"; ");
				System.out.println("RHS:"+assign.getRightHandSide());
				
			}
			else if(express instanceof MethodInvocation)
			{
				MethodInvocation mi=(MethodInvocation) express;
				System.out.println("invocation name:"+mi.getName());
				System.out.println("invocation exp:"+mi.getExpression());
				System.out.println("invocation arg:"+mi.arguments());
				
			}
			System.out.println();
			
		}
		else if(stmt instanceof IfStatement)  
        {  
            IfStatement ifstmt=(IfStatement) stmt;  
            InfixExpression wex=(InfixExpression) ifstmt.getExpression();  
            System.out.println("if-LHS:"+wex.getLeftOperand()+"; ");  
            System.out.println("if-op:"+wex.getOperator()+"; ");  
            System.out.println("if-RHS:"+wex.getRightOperand());  
            System.out.println();  
        } 
		else if(stmt instanceof VariableDeclarationStatement)  
        {  
            VariableDeclarationStatement var=(VariableDeclarationStatement) stmt;  
            System.out.println("Type of variable:"+var.getType());  
            System.out.println("Name of variable:"+var.fragments());  
            System.out.println();  
              
        } 
		else if(stmt instanceof ReturnStatement)  
        {  
            ReturnStatement rtstmt=(ReturnStatement) stmt;  
            System.out.println("return:"+rtstmt.getExpression());  
            System.out.println();  
        }  
		

		
	}
	
	
	
	
}
