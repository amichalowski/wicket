/*
 * $Id$
 * $Revision$ $Date$
 * 
 * ==================================================================== Licensed
 * under the Apache License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the
 * License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package wicket.ajax.markup.html.ajaxLink;

import wicket.WicketTestCase;

/**
 * 
 */
public class AjaxLinkTest extends WicketTestCase
{
	/**
	 * Construct.
	 * 
	 * @param name
	 */
	public AjaxLinkTest(String name)
	{
		super(name);
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testRenderHomePage_1() throws Exception
	{
		executeTest(AjaxLinkPage.class, "AjaxLinkPageExpectedResult.html");
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void testRenderHomePage_2() throws Exception
	{
		// TODO Bordered pages are not yet working properly with ajax
//		executeTest(AjaxLinkWithBorderPage.class, "AjaxLinkWithBorderPageExpectedResult.html");
	}
}
