import groovy.json.JsonSlurper
import groovyx.net.http.RESTClient
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import groovyx.net.http.ContentType

//addMessage(ruleContext.renderSmartValues('{{lookupAssets}}'))

// Define the JSON objects (you can replace these with actual JSON data)
def json1 = ruleContext.renderSmartValues('{{lookupAssets.attributesById}}')
def formattedData = json1
    .replaceAll(/(4596|4599|5049|5055|5100)=([^,}]+)/, '"$1":"$2"')
    .replaceAll(/(\w+)=/, '"$1":')
    .replaceAll(/, /, ',')
    .replaceAll(/},\s*\{/, '},{')
formattedData = "[${formattedData}]"

def json2 = '''[
  {
    "4596": "TEST-8535", 
    "4599": "SMO-APP01",
    "4602": 1708406989000,
    "4605": 1714204505000,
    "5049": "172.31.20.19",
    "5052" : 900,
    "5055" : "OS Sample"
  },
  {
    "4596": "TEST-8553",
    "4599": "SMO-APP02",
    "4602": 1708406989000,
    "4605": 7657567567567,
    "5049": "172.31.20.22",
    "5052" : 2
  }
]'''

def list1 = new JsonSlurper().parseText(formattedData) as List<Map>
def list2 = new JsonSlurper().parseText(json2) as List<Map>


// Function to compare two lists of objects based on IP and return a list of changed values
def compareLists(List<Map> list1, List<Map> list2) {
  def changes = []
  list1.each { obj1 ->
    def obj2 = list2.find { it."5049" == obj1."5049" }
    if (obj2) {
      obj1.each { key, value ->
        if (key != "5049" && obj2[key] != value) {
          changes << ["5049": obj1."5049", "4596": obj1."4596" , attribute: key, oldValue: value, newValue: obj2[key]]
        }
      }
    }
  }
  addMessage ("changes: ${changes} ")
  return changes
}

// Function to send HTTP request to update the attribute
def updateAttribute(String key, Number keyValue , String objectId, Object newAttributeId, Object newValue) {
    def http = new HTTPBuilder("https://sepjira.sep.ir/rest/insight/1.0/object/${key}")
    //http.auth.basic('b.mohajeri', 'Luci!@#456789')  // Add your username and password here

    http.request(Method.PUT, ContentType.JSON) { req ->
        headers.'Content-Type' = 'application/json'
        headers.'Authorization' = 'Basic Yi5tb2hhamVyaTpMdWNpIUAjNDU2Nzg5'
        //headers.'Cookie' = 'JSESSIONID=57E90357CC59C15D4C0F2CAE5BE358B1.node1; SEP01fae845=017cb00b00a53b8d6186b1441af445607314963658f5d0d3a431a177e09d312ffb4197ae8b61f6c1ad39402c96c52f1e37b1512aa55b14791d903673588b47c0a23f1fa1ea2cbac6c1aacb39d9cbdfa2daa2adefb7; atlassian.xsrf.token=B3GE-Z4YK-QZKV-G61J_35e1f01903e14198d61c3a699194090408ffbadf_lin'

        body = [
            objectTypeId: keyValue,
            attributes: [
                [
                    objectTypeAttributeId: newAttributeId,  // New attribute ID
                    objectAttributeValues: [
                        [
                            value: newValue  // New value
                        ]
                    ]
                ]
            ]
        ]

    }
}

// Compare the JSON lists and get the list of changes
def result = compareLists(list1, list2)

// Iterate over the result and update the attributes via API
result.each { change ->
    def key = change["4596"] as String
    def ip = change["5049"] as String
    def attribute = change["attribute"] as Object
    def newValue = change["newValue"] as Object

    // Extract the numeric part of the key
    def numericKey = key.replaceAll("[^\\d]", "").toInteger()

    // Call the updateAttribute method with the correct parameters
    def response = updateAttribute(key , numericKey, ip, attribute, newValue)
    addMessage("Updated ${attribute} for ip ${ip} with new value: ${newValue} and key: ${numericKey}")
}
