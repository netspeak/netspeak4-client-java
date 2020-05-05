# Netspeak 4 client

This is a client of the Netspeak 4 web API written in Java.


<details>
<summary><b>Usage example</b></summary>

```java
import java.io.IOException;
import java.util.concurrent.Future;

import org.netspeak.ErrorCode;
import org.netspeak.NetspeakUtil;
import org.netspeak.client.NetspeakClient;
import org.netspeak.client.Request;
import org.netspeak.generated.NetspeakMessages.Phrase;
import org.netspeak.generated.NetspeakMessages.Phrase.Word;
import org.netspeak.generated.NetspeakMessages.Response;

public class NetspeakUsage {

    public static void main(String[] args) throws IOException {

        // Instantiate the Netspeak client once in your setup code.
        NetspeakClient netspeak = new NetspeakClient();

        // Create a request object and fill it with key/value pairs which
        // correspond to the parameters described by Netspeak's REST interface.
        Request request = new Request();
        request.put(Request.QUERY, "waiting ? * #response");
        request.put(Request.TOPK, String.valueOf(30));
        // Add more parameters here ...

        // Request the Netspeak service synchronously, i.e. the search method
        // blocks until the response was received or some exception occurred.
        Response response = netspeak.search(request);

        // Even if a response was received successfully, there may have
        // encountered an error while processing the request. Check this first.
        // Please always cast the integer error code to some proper ErrorCode!
        ErrorCode errorCode = ErrorCode.fromCode(response.getErrorCode());
        if (errorCode != ErrorCode.NO_ERROR) {
            System.err.println("Error code: " + errorCode);
            System.err.println("Error message: " + response.getErrorMessage());
        }

        // Iterate the retrieved list of phrases.
        // Note that a phrase is actually a list of words. Use CommonUtils to
        // stringify a phrase if you are just interested in the whole string.
        for (Phrase phrase : response.getPhraseList()) {
            System.out.printf("%d\t%d\t%s\n", phrase.getId(),
                phrase.getFrequency(), NetspeakUtil.toString(phrase));
        }

        // Iterate the retrieved list of phrases again.
        // This time print each word of the phrase separately together with its
        // tag. The tag states to which part of the query this word belongs to.
        for (Phrase phrase : response.getPhraseList()) {
            for (Word word : phrase.getWordList()) {
                System.out.printf("%s (%s) ", word.getText(), word.getTag());
            }
            System.out.println();
        }

        // Request the Netspeak service again, this time asynchronously, i.e.
        // the search method does not block, but returns immediately. If the
        // response was received, the onSuccess method of the callback object
        // is called by the internal executing thread.
        request.put(Request.QUERY, "how to ? this");
        Future<Response> futureResponse = netspeak.searchAsync(request);

        try {
            for (Phrase phrase : futureResponse.get().getPhraseList()) {
                System.out.printf("%d\t%d\t%s\n", phrase.getId(),
                    phrase.getFrequency(), NetspeakUtil.toString(phrase));
            }
        } catch (Exception e) {
            System.err.println("Failure in async search: " + e);
        }
    }

}
```

</details>


## Build

```bash
gradle build
```

The compiled JAR will be located at `build/libs/`


---

## Contributors

Michael Schmidt (2018 - 2020)

Martin Trenkmann (2008 - 2013)

Martin Potthast (2008 - 2020)

Benno Stein (2008 - 2020)

