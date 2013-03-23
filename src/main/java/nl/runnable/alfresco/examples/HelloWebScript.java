package nl.runnable.alfresco.examples;

import java.io.IOException;
import java.util.Arrays;

import javax.annotation.ManagedBean;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.runnable.alfresco.webscripts.annotations.Attribute;
import nl.runnable.alfresco.webscripts.annotations.RequestParam;
import nl.runnable.alfresco.webscripts.annotations.Uri;
import nl.runnable.alfresco.webscripts.annotations.UriVariable;
import nl.runnable.alfresco.webscripts.annotations.WebScript;

import org.alfresco.service.namespace.QName;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;
import org.springframework.web.bind.ServletRequestDataBinder;

/**
 * Simplest annotated Web Script.
 * <p>
 * See the documentation in {@link CategoriesWebScript} for more information on annotated Web Scripts.
 * 
 * @author Laurens Fridael
 * 
 */
@ManagedBean
@WebScript
public class HelloWebScript {

	/**
	 * Handles the "hello" request. Note the use of Spring MVC-style annotations to map the Web Script URI configuration
	 * and request handling objects.
	 * 
	 * @param name
	 * @param response
	 * @throws IOException
	 */
	@Uri("/dynamic-extensions/examples/hello")
	public void handleHello(@RequestParam final String name, final WebScriptResponse response) throws IOException {
		final String message = String.format("Hello, %s", name);
		response.getWriter().write(message);
	}

	/**
	 * Illustrates that {@link QName} can now be resolved as {@link RequestParam} arguments.
	 * <p>
	 * Note also the use of the delimiter to split a single parameter into an array of values.
	 * 
	 * @param name
	 * @param response
	 * @throws IOException
	 */
	@Uri("/dynamic-extensions/examples/hello-qname")
	public void handleHelloQNameParameter(@RequestParam(delimiter = ",") final QName[] names,
			final WebScriptResponse response) throws IOException {
		final String message = String.format("Hello, %s", Arrays.asList(names));
		response.getWriter().write(message);
	}

	/**
	 * Illustrates that {@link QName} can now be resolved as {@link UriVariable} arguments.
	 * 
	 * @param name
	 * @param response
	 * @throws IOException
	 */
	@Uri("/dynamic-extensions/examples/hello/{name}")
	public void handleHelloQNameVariable(@UriVariable final QName name, final WebScriptResponse response)
			throws IOException {
		final String message = String.format("Hello, %s", name);
		response.getWriter().write(message);
	}

	/**
	 * This example shows how you can obtain the original {@link HttpServletRequest} and {@link HttpServletResponse}.
	 * This, in turn, enables integration with libraries that use the Servlet API.
	 * <p>
	 * The example shows how you can use a Spring MVC {@link ServletRequestDataBinder} to bind request parameters to
	 * JavaBeans-style properties.
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@Uri("/dynamic-extensions/examples/hello-servlet")
	public void handleHelloServlet(final HttpServletRequest request, final HttpServletResponse response)
			throws IOException {
		final Person person = new Person();
		final ServletRequestDataBinder dataBinder = new ServletRequestDataBinder(person);
		dataBinder.bind(request);
		final String message = String.format("Hello, %s", person.getName());
		response.getWriter().write(message);
	}

	/**
	 * Methods annotated with {@link Attribute} provide reference data for arguments in {@link Uri} handling methods.
	 * These metho arguments must be annotated with {@link Attribute} as well.
	 * <p>
	 * In this case, the method provides a String attribute named 'name'.
	 * 
	 * @see #handleHelloAttribute(String, WebScriptResponse)
	 * 
	 * @param request
	 * @return
	 */
	@Attribute
	protected String getName(final WebScriptRequest request) {
		return request.getParameter("name");
	}

	/**
	 * Illustrates request handling using {@link Attribute}.
	 * 
	 * @param name
	 * @param response
	 * @throws IOException
	 * @see {@link #getName(WebScriptRequest)}
	 */
	@Uri("/dynamic-extensions/examples/hello-attribute")
	public void handleHelloAttribute(@Attribute final String name, final WebScriptResponse response) throws IOException {
		final String message = String.format("Hello, %s", name);
		response.getWriter().write(message);
	}

	/**
	 * Example class used to illustrate data binding.
	 * 
	 * @author Laurens Fridael
	 * 
	 */
	public static class Person {

		private String name;

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}

	}
}
