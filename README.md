SeleniumJT Core
===============
The Core project comes in 3 parts. The core code, a test server and unit tests. If you are to do any work on the core then you need to download all 3 projects as you will need to run the unit tests against the test server to ensure you haven’t broken anything. 

<h2>seleniumjt-core</h2>
This is the main core framework that is used to build and deploy to a maven repository.
<h2>seleniumjt-core-server</h2>
This is a stand-alone application that runs on a jetty-server based on The Stack project. You will need to add a key/value in your eclipse’s String Substitution. Variable=M2_HOME, value={Path to your maven directory}. This server provides pages and forms that the unit-tests will test against. To start the server you run ‘Jetty start.launch’
<h2>seleniumjt-core-unit-tests</h2>
After every change to the core-framework you must start the test server and run the unit tests found under src/main/xml. Right click these and Run As -> TestNG Suite
You should have a green test run. Any changes you make to the core make sure these tests run green before you commit any changes.
<p>
The server and unit tests can be found at https://github.com/davehampton/seleniumjt-core-tests

