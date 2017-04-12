package org.apache.wicket.util.tester;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.core.request.handler.IPageRequestHandler;
import org.apache.wicket.markup.IMarkupResourceStreamProvider;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.StringResourceStream;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

public class WicketTesterHttpHeaderTest extends WicketTestCase
{

	private final static String EXPECTED_REFERER = "http://localhost/context/servlet/wicket/bookmarkable/org.apache.wicket.util.tester.WicketTesterHttpHeaderTest$TestPage?test=value";

	private String lastRefererHeder;

	@Before
	public void addMockRequestCycleListener()
	{
		lastRefererHeder = null;
		tester.getApplication().getRequestCycleListeners().add(new MockRequestCycleListener());
	}

	@Test
	public void checkHeadersAfterPageRequest()
	{
		tester.startPage(TestPage.class);
		tester.assertRenderedPage(TestPage.class);
		assertEquals("wrong referer header", null, lastRefererHeder);
	}

	@Test
	public void checkHeadersAfterFromSubmitViaButton()
	{
		;
		assertHeadersAfterFormSubmit("form", "button", EXPECTED_REFERER);
	}

	@Test
	public void checkHeadersAfterFromSubmitViaAjaxButton()
	{
		assertHeadersAfterFormSubmit("form", "ajaxButton", EXPECTED_REFERER);
	}

	private void assertHeadersAfterFormSubmit(String formPath, String buttonId,
		String expectedReferer)
	{
		tester.startPage(TestPage.class, new PageParameters().add("test", "value"));
		FormTester formTester = tester.newFormTester(formPath);
		formTester.submit(buttonId);

		assertEquals("wrong referer header", expectedReferer, lastRefererHeder);
	}

	@Test
	public void checkHeadresAfterLinkClick()
	{
		assertHeadersAfterLinkClick("link", EXPECTED_REFERER);
	}

	@Test
	public void checkHeadresAfterAjaxLinkClick()
	{
		assertHeadersAfterLinkClick("ajaxLink", EXPECTED_REFERER);
	}

	private void assertHeadersAfterLinkClick(String linkPath, String expectedReferer)
	{
		tester.startPage(TestPage.class, new PageParameters().add("test", "value"));
		tester.clickLink(linkPath);
		assertEquals("wrong referer header", expectedReferer, lastRefererHeder);
	}

	public final static class TestPage extends WebPage implements IMarkupResourceStreamProvider
	{

		public TestPage()
		{
			Form<Void> form = new Form<>("form");
			add(form);
			form.add(new Button("button"));
			form.add(new AjaxButton("ajaxButton")
			{
			});
			add(new BookmarkablePageLink<Void>("link", TestPage.class));
			add(new AjaxLink<Void>("ajaxLink")
			{
				@Override
				public void onClick(AjaxRequestTarget ajaxRequestTarget)
				{
				}
			});
		}

		@Override
		public IResourceStream getMarkupResourceStream(MarkupContainer container,
			Class<?> containerClass)
		{
			return new StringResourceStream(
				"<html><body><form wicket:id='form'><input wicket:id='button' type='submit'/>"
					+ "<input wicket:id='ajaxButton' type='submit'/></form>"
					+ "<a wicket:id=\"link\" >link</a>\n"
					+ "<a wicket:id=\"ajaxLink\" >link</a></body></html>");
		}
	}

	private final class MockRequestCycleListener extends AbstractRequestCycleListener
	{

		@Override
		public void onRequestHandlerResolved(RequestCycle cycle, IRequestHandler handler)
		{
			if (handler instanceof IPageRequestHandler)
			{
				HttpServletRequest containerRequest = (HttpServletRequest)cycle.getRequest()
					.getContainerRequest();

				lastRefererHeder = containerRequest.getHeader(WebRequest.HEADER_REFERER);
			}
		}
	}
}