import com.onresolve.scriptrunner.runner.rest.common.CustomEndpointDelegate
import groovy.json.JsonOutput
import groovy.transform.BaseScript
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.Response
import java.net.HttpURLConnection
import java.net.URL

@BaseScript CustomEndpointDelegate delegate

transitionIssue(httpMethod: 'GET') { MultivaluedMap queryParams ->

    def issueKey = queryParams.getFirst("issueKey") as String
    //def transitionId = queryParams.getFirst("transitionId") // Extracting transition ID from query parameters

    if (!issueKey) {
        return Response.status(Response.Status.BAD_REQUEST)
                      .entity(JsonOutput.toJson([error: "issueKey parameter is required"]))
                      .build()
    }


    try {
        String apiUrl = "https://sepjira.sep.ir/rest/api/2/issue/${issueKey}/transitions"
        String authToken = "Bearer 123" // Replace with your actual auth token

        def transitionBody = new JsonOutput().toJson([
            transition: [id: 71]
        ])

        URL url = new URL(apiUrl)
        HttpURLConnection conn = (HttpURLConnection) url.openConnection()
        conn.setRequestMethod("POST")
        conn.setRequestProperty("Content-Type", "application/json")
        conn.setRequestProperty("Authorization", authToken)
        conn.setDoOutput(true)
        conn.getOutputStream().write(transitionBody.getBytes("UTF-8"))

        int responseCode = conn.getResponseCode()
        String responseMessage = conn.getResponseMessage()

        def responseContent = JsonOutput.toJson([status: responseCode, message: responseMessage])
        
        '<script type="text/javascript">setTimeout(function() { window.location.reload(); }, 1000);</script>'

        def flag = [
            type : 'success',
            title: "Issue Transition ${issueKey}",
            close: 'auto',
            body : "This issue has been transitioned to the next step"
        ]

    

        return Response.ok(JsonOutput.toJson(flag) ).build()
    } catch (Exception e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(JsonOutput.toJson(e.message)).build()
    }

}
