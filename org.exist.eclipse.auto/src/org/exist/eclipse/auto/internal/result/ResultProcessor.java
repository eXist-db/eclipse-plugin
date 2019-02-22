/**
 * 
 */
package org.exist.eclipse.auto.internal.result;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;

import org.exist.eclipse.auto.data.AutoTags;
import org.exist.eclipse.auto.internal.model.QueryOrderType;
import org.exist.eclipse.auto.internal.result.view.QueryGroup;
import org.exist.eclipse.auto.query.IQueryResult;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * Processes the automation results and generates an according output xml.
 * 
 * @author Markus Tanner
 */
public class ResultProcessor implements AutoTags {

	private Map<Integer, QueryGroup> _results;
	private int _threadCount;
	private int _queryCount;
	private QueryOrderType _queryOrderType;
	private String _autoNote;

	/**
	 * @param results
	 * @param threadCount
	 * @param type
	 * @param autoNote
	 * @param queryCount
	 */
	public ResultProcessor(Map<Integer, QueryGroup> results, int threadCount, QueryOrderType type, String autoNote,
			int queryCount) {
		_results = results;
		_threadCount = threadCount;
		_queryCount = queryCount;
		_queryOrderType = type;
		_autoNote = autoNote;
	}

	/**
	 * Returns the result as XML representation.
	 * 
	 * @param outputFile Automation Result target file
	 * @throws Exception
	 */
	public void writeResultXml(File outputFile) throws Exception {
		XMLOutputter outputter = new XMLOutputter();
		try (Writer out = new FileWriter(outputFile); PrintWriter writer = new PrintWriter(out)) {
			writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			writer.println("<" + AUTOMATIONRESULT + ">");

			Element element = new Element(THREADCOUNT);
			element.setText(Integer.toString(_threadCount));
			outputter.setFormat(Format.getPrettyFormat());
			writer.println("\t\t" + outputter.outputString(element));

			element = new Element(QUERYCOUNT);
			element.setText(Integer.toString(_queryCount));
			outputter.setFormat(Format.getPrettyFormat());
			writer.println("\t\t" + outputter.outputString(element));

			element = new Element(QUERYORDERTYPE);
			element.setText(_queryOrderType.toString());
			outputter.setFormat(Format.getPrettyFormat());
			writer.println("\t\t" + outputter.outputString(element));

			element = new Element(AUTONOTE);
			element.setText(_autoNote);
			outputter.setFormat(Format.getPrettyFormat());
			writer.println("\t\t" + outputter.outputString(element));

			writer.println("<" + QUERIES + ">");

			for (QueryGroup group : _results.values()) {
				analyzeQueryResults(group, writer);
			}

			writer.println("</" + QUERIES + ">");
			writer.print("</" + AUTOMATIONRESULT + ">");
		}
	}

	// -------------------------------------------------------------------------
	// Private Methods
	// -------------------------------------------------------------------------

	/**
	 * Iterates through the result data and puts together the according xml.
	 */
	private void analyzeQueryResults(QueryGroup group, PrintWriter writer) {

		StringBuilder query = new StringBuilder();
		query.append('<').append(QUERY).append(' ').append(NAME).append("=\"").append(group.getQuery().getName())
				.append("\">");
		writer.println(query.toString());

		XMLOutputter outputter = new XMLOutputter();
		outputter.setFormat(Format.getPrettyFormat());
		Element element = new Element(NOTE);
		element.setText(group.getQuery().getNotes());
		writer.println("\t\t" + outputter.outputString(element));

		element = new Element(QUANTITY);
		element.setText(Integer.toString(group.getQuery().getQuantity()));
		writer.println("\t\t" + outputter.outputString(element));

		element = new Element(CONTENT);
		element.setText(group.getQuery().getQuery());
		writer.println("\t\t" + outputter.outputString(element));

		element = new Element(AVGCOMPILATION);
		element.setText(Long.toString(group.getAverageCompilation()));
		writer.println("\t\t" + outputter.outputString(element));

		element = new Element(AVGEXECUTION);
		element.setText(Long.toString(group.getAverageExecution()));
		writer.println("\t\t" + outputter.outputString(element));

		query = new StringBuilder();
		query.append('<').append(RUNS).append('>');
		writer.println(query.toString());

		for (IQueryResult queryResult : group.getResults()) {

			Element runElement = new Element(RUN);
			Element stateElement = new Element(STATE);
			stateElement.setText(queryResult.getQueryState().toString());

			Element compilationElement = new Element(COMPILATION);
			Long compileTime = Long.valueOf(queryResult.getCompileTime());
			compilationElement.setText(compileTime.toString());

			Element executionElement = new Element(EXECUTION);
			Long execTime = Long.valueOf(queryResult.getExecutionTime());
			executionElement.setText(execTime.toString());

			Element countElement = new Element(RESULT_COUNT);
			long resultCount = queryResult.getResultCount();
			countElement.setText(Long.toString(resultCount));

			runElement.addContent(stateElement);
			runElement.addContent(compilationElement);
			runElement.addContent(executionElement);
			runElement.addContent(countElement);

			writer.println(outputter.outputString(runElement));
		}
		writer.append("</").append(RUNS).append('>').println();
		writer.append("</").append(QUERY).append('>');
	}
}
