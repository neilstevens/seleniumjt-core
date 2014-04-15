package com.jt.selenium.page;

import com.jt.selenium.SeleniumJT;

/**
 * The Page Object pattern represents the screens of your web app as a series of objects 
 * 
 * PageObjects can be thought of as facing in two directions simultaneously. 
 * Facing towards the developer of a test, they represent the services offered by a particular page. 
 * Facing away from the developer, they should be the only thing that has a deep knowledge of the structure 
 * of the HTML of a page (or part of a page) It's simplest to think of the methods on a Page Object as 
 * offering the "services" that a page offers rather than exposing the details and mechanics of the page.
 * 
 * @see https://code.google.com/p/selenium/wiki/PageObjects
 * 
 * @author Dave.Hampton
 *
 */
public abstract class PageObject
{

	protected SeleniumJT test = null;

	public PageObject(SeleniumJT test) {
		this.test=test;
	}


}
