# StreamToGrid
============

## Functionality:

Capture twitter stream sample to data grid and analyze trending of hashtags and frequent users utilizing simple, fast
rule sessions to persist trends so that they may be further searched against the API and analyzed.

## Stack:
 - Hazelcast in-memory data grid (persistence)
 - RabbitMQ (AMQP, queue incoming stream traffic and analysis jobs - prevent stalling in API)
 - Drools core (business rules engine, simple trend analysis)
 - Twitter4J ([Twitter API](https://dev.twitter.com/), stream and search)
 - Mockito (testing)
 - EC2 Instances & Load Balancing for Hazelcast & RabbitMQ (optional)

## Getting Started:
[StreamStatusListenerIntegrationTest](/src/test/java/jary/twitter/listener/StreamStatusListenerIntegrationTest.java) carries out the basic functionality of opening a twitter API stream for a given period of time with an attached listener that passes input on to queueing, starting the persist/analyze/trend flow. At the end of the initial stream window, an additional, much smaller allowance is given to allow queue processors time to catch up (in case the stream remained open for a long period of time or processing backed up for some other reason) and then reports map information indicative of what's been captured in the data grid.

## Current State:
All basic elements have been put into place. From here, rule sessions can be grown to further analyze Status data. The 
Twitter client could be used to search for more information on detected trending hashtag and user information 
to supplement what's available within the limitation of 1% of total twitter traffic available via the stream.

## Implementation:
If you would like to fork and/or work with this repository, you will need to add an additional properties file not found
here (.gitignore) containing your twitter Oauth information. 
  
  Add file `/src/main/resources/twitter4j.properties` and include the following:  
  
    debug=false  
    oauth.consumerKey=[your_key]  
    oauth.consumerSecret[your_secret]  
    oauth.accessToken=[your_token]  
    oauth.accessTokenSecret[your_token_secret]  
    
## Contact:
I can be reached at:
 - jeremy.ary@gmail.com
 - http://twitter.com/jeremyary
 - http://linkedIn.com/in/jeremyary
