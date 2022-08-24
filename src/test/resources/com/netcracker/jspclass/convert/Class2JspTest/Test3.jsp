<%@ page import="javax.servlet.jsp.JspWriter" %>
<%@ page import="javax.servlet.jsp.PageContext" %>
<%@ page import="java.io.PrintWriter" %>
<%!
public class Test3
{
    private PageContext pageContext;
    private JspWriter out;

    public Test3(PageContext pageContext)
    {
        this.pageContext = pageContext;
        out = pageContext.getOut();
    }

    public void run()
    {
        try
        {
            parseParameters();
            execute();
            printContent();
        }
        catch (Exception e)
        {
            printException(e);
        }
    }

    protected void parseParameters() throws Exception
    {
        print("Test3 parseParameters");
    }

    protected void execute() throws Exception
    {
        print("Test3 execute");
    }

    protected void printContent()
    {
        print("Test3 printContent");
    }

    private void print(Object obj)
    {
        try
        {
            out.print(obj);
            out.print("<br/>");
        }
        catch (Exception ignored)
        {
        }
    }

    private void printException(Throwable obj)
    {
        try
        {
            out.print("<pre>");
            obj.printStackTrace(new PrintWriter(out));
            out.print("</pre>");
            out.print("<br/>");
        }
        catch (Exception ignored)
        {
        }
    }
}
%>
<%!
public class Test4
{
}
%>
<%
Test3 test = new Test3(pageContext);
test.run();
%>