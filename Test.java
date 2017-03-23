package com.antlr.test.sources;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.antlr.test.sources.MySQLParser.Column_nameContext;
import com.antlr.test.sources.MySQLParser.Expr_opContext;
import com.antlr.test.sources.MySQLParser.ExpressionContext;
import com.antlr.test.sources.MySQLParser.Left_elementContext;
import com.antlr.test.sources.MySQLParser.Simple_expressionContext;

public class Test {
	public static void main(String[] args) {

		try {
			// lexer splits input into tokens
			String input = "select COUNT(*) CHALLAN_COUNT,TRANSACTION_DATE AS TRANSACTION_DATE,HEAD_ACCOUNT_ID1,SCROLL_NO,BSR_CODE from PYFM where FORM_REQ_STATE='SUC' and  TRANSACTION_DATE >= 'USER_INPUT_DATE' and TRANSACTION_DATE <= 'USER_INPUT_DATE'"
					+ "and DEL_FLG='N' group by TRANSACTION_DATE,HEAD_ACCOUNT_ID1,SCROLL_NO,BSR_CODE order by TRANSACTION_DATE,HEAD_ACCOUNT_ID1";
			ANTLRInputStream stringInput = new ANTLRInputStream(input);
			TokenStream tokens = new CommonTokenStream(new MySQLLexer(stringInput));

			// parser generates abstract syntax tree
			MySQLParser parser = new MySQLParser(tokens);
			//Select clause
			MySQLParser.Select_clauseContext temp = parser.select_clause();
			//table name
			MySQLParser.Table_referencesContext tableName = temp.table_references(); 
			//getting colums to select
			List columns = temp.column_list_clause().children;
			//getting whereClause Details
			List whereClause = temp.where_clause().children;
			temp.where_clause().expression().expr_op();
			for (int i = 0; i < whereClause.size(); i++) {
				System.out.println(whereClause.get(i).getClass().getSimpleName());
				if (whereClause.get(i).getClass().getSimpleName().equals("ExpressionContext")) {
					List<ParseTree> tempExpr= ((ExpressionContext)whereClause.get(i)).children;
					for(int j=0;j<tempExpr.size();j++)
					{
						//System.out.println(tempExpr.get(j).getText());
						if(tempExpr.get(j).getClass().getSimpleName().equals("Simple_expressionContext"))
						{
							System.out.println(((Simple_expressionContext)tempExpr.get(j)).left_element().getText());
							System.out.println(((Simple_expressionContext)tempExpr.get(j)).right_element().getText());
						}
						else
						{
							System.out.println(tempExpr.get(j).getText());
						}
						//System.out.println(tempExpr.get(j).getParent().getText());
						
					}
/*					MySQLParser.Left_elementContext left= ((Left_elementContext)whereClause.get(i));
					MySQLParser.Left_elementContext right= ((Left_elementContext)whereClause.get(i));*/
				}
				
			}

			ArrayList<GlobalFields> globalFields = new ArrayList<GlobalFields>();
			
			
			for (int i = 0; i < columns.size(); i++) {
				GlobalFields globalFieldsVO = new GlobalFields();
				globalFieldsVO.setTableName(tableName.getText());
				if (columns.get(i).getClass().getSimpleName().equals("Column_nameContext")) {
					
					List<ParseTree> tempColumn = ((Column_nameContext) columns.get(i)).children;
					String functionName = "";
					String colName = "";
					if (tempColumn.size() > 2) {
						functionName = tempColumn.get(0).getText();
						colName = tempColumn.get(2).getText();
					} else {
						functionName = "";
						colName = tempColumn.get(0).getText();
					}
					globalFieldsVO.setFunction(functionName);
					globalFieldsVO.setName(colName);
					// globalFieldsVO.setTableName(tableName);
					System.out.println(((Column_nameContext) columns.get(i)).getText());// children);
				}

			}
			/*
			 * for (int i = 0; i < temp.getChildCount(); i++) {
			 * System.out.println(temp.getChild(i)); }
			 */
		} catch (RecognitionException e) {
			throw new IllegalStateException("Recognition exception is never thrown, only declared.");
		}

	}

}

/*
 * select COUNT(*) AS SUC_CHALLAN_COUNT, SUM(TOTAL_TRANSACTION_AMOUNT) AS
 * SUC_TOTAL_AMOUNT,TRANSACTION_DATE AS TRANSACTION_DATE,
 * HEAD_ACCOUNT_ID1,SCROLL_NO,BSR_CODE from PYFM where FORM_NAME in ('C280') AND
 * FORM_REQ_STATE='SUC' and TRANSACTION_DATE >= '27/12/2016' and
 * TRANSACTION_DATE <= '27/12/2016' and DEL_FLG='N' group by
 * TRANSACTION_DATE,HEAD_ACCOUNT_ID1,SCROLL_NO,BSR_CODE order by
 * TRANSACTION_DATE,HEAD_ACCOUNT_ID1
 */
