<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd" >

<suite name="Selenium Development Tests" verbose="5">

	<parameter name="browserType" value="@browser@"/>
	
	@tests@
	
</suite>
