<%@ page import="javax.servlet.jsp.JspWriter,
javax.servlet.jsp.PageContext,
java.io.PrintWriter" %>
<%!
public class Test2
{
    private PageContext pageContext;
    private JspWriter out;

    public Test2(PageContext pageContext)
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
        print("Test2 parseParameters");
    }

    protected void execute() throws Exception
    {
        print("Test2 execute");
    }

    protected void printContent()
    {
        print("Test2 printContent");
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
<%
    new Test2(pageContext).run();
%>