package com.antlr.test.sources;

import java.util.List;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import com.antlr.test.sources.MySQLParser.Column_list_clauseContext;
import com.antlr.test.sources.MySQLParser.Column_nameContext;
import com.antlr.test.sources.MySQLParser.Expr_opContext;
import com.antlr.test.sources.MySQLParser.ExpressionContext;
import com.antlr.test.sources.MySQLParser.Simple_expressionContext;

public class QueryDataSeparator {
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
			MySQLParser.Select_clauseContext selectClause = parser.select_clause();
			//table name
			MySQLParser.Table_referencesContext tableName = selectClause.table_references(); 
			//Index Options
			System.out.println("Table Name: "+tableName.getText());
			
			//getting colums to select
			List columns = selectClause.column_list_clause().children;
			//getting whereClause Details
			List whereClause = selectClause.where_clause().children;
			RuleContext ruleContext = selectClause.where_clause().getRuleContext();
			//parser.inContext(context)
			for(int i=0;i<ruleContext.getChildCount();i++)
			{
				if(ruleContext.getChild(i).getClass().getSimpleName().equals("ExpressionContext"))
				{
					List whereElements = ((ExpressionContext)ruleContext.getChild(i)).children;
					for(int j=0;j<whereElements.size();j++)
					{
						if(whereElements.get(j).getClass().getSimpleName().equals("Simple_expressionContext"))
						{
							System.out.println("LeftElement: "+((Simple_expressionContext)whereElements.get(j)).left_element().getText());
							System.out.println("Relation Operator: "+((Simple_expressionContext)whereElements.get(j)).relational_op().getText());
							System.out.println("RightElement: "+((Simple_expressionContext)whereElements.get(j)).right_element().getText());
							System.out.println("--------------------------------------");
						}
						else if(whereElements.get(j).getClass().getSimpleName().equals("Expr_opContext"))
						{
							System.out.println("Logical operator: "+((Expr_opContext)whereElements.get(j)).getText());
							System.out.println("--------------------------------------");
							System.out.println();
						}
						
						
					}
				}
			}
			
			for (int i = 0; i < columns.size(); i++) {
				//GlobalFields globalFieldsVO = new GlobalFields();
				//globalFieldsVO.setTableName(tableName.getText());
				if (columns.get(i).getClass().getSimpleName().equals("Column_nameContext")) {
					
					List<ParseTree> tempColumn = ((Column_nameContext) columns.get(i)).children;
					String functionName = "";
					String colName = "";
					if (tempColumn.size() > 2) {
						functionName = tempColumn.get(0).getText();
						colName = tempColumn.get(2).getText();
						System.out.println("colName: "+colName);
						System.out.println("functionName: "+functionName);
					} else {
						functionName = "";
						colName = tempColumn.get(0).getText();
						System.out.println("colName: "+colName);
					}
					//globalFieldsVO.setFunction(functionName);
					//globalFieldsVO.setName(colName);
					// globalFieldsVO.setTableName(tableName);
					//System.out.println(((Column_nameContext) columns.get(i)).getText());// children);
				}

			}
			MySQLParser.GroupBy_clauseContext groupByClause= selectClause.groupBy_clause();
			List groupByColName = groupByClause.children;
			for(int i=0;i<groupByColName.size();i++)
			{
				if(groupByColName.get(i).getClass().getSimpleName().equals("Column_list_clauseContext"))
				{
					List groupByColumnName = ((Column_list_clauseContext)groupByColName.get(i)).children;
					System.out.println("Group By Clause: ");
					for(int j=0;j<groupByColumnName.size();j++)
					{
						if(groupByColumnName.get(j).getClass().getSimpleName().equals("Column_nameContext"))
						System.out.println(((Column_nameContext)groupByColumnName.get(j)).getText());
					}
					System.out.println("-----------------------");
				}
			}
			
			MySQLParser.OrderBy_clauseContext orderByClause= selectClause.orderBy_clause();
			List orderByColName = orderByClause.children;
			for(int i=0;i<orderByColName.size();i++)
			{
				if(orderByColName.get(i).getClass().getSimpleName().equals("Column_list_clauseContext"))
				{
					List orderByColumnName = ((Column_list_clauseContext)orderByColName.get(i)).children;
					System.out.println("Order By Clause: ");
					for(int j=0;j<orderByColumnName.size();j++)
					{
						if(orderByColumnName.get(j).getClass().getSimpleName().equals("Column_nameContext"))
						System.out.println(((Column_nameContext)orderByColumnName.get(j)).getText());
					}
					System.out.println("-----------------------");
				}
			}
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
