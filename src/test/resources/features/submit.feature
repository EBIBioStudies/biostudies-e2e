Feature: make a submission

  Background: Here is the setup for the scenarios
    Given the setup information
      | environmentUrl   | ftpUrl   | userName             | userPassword |
      | environment/path | ftp/path | admin_user@ebi.ac.uk | 123456       |
    And a http request with body:
      """
      {
        "login":"$userName",
        "password":"$userPassword"
      }
      """
    * url path "$environmentUrl/auth/login"
    * http method "POST"
    When request is performed
    Then http status code "200" is returned
    And http response the JSONPath value "$.sessid" is saved into "token"

  Scenario: submit a submission with a file
    Given the file "example.txt" with content
    """
      Sample content
    """
    And a http request with form-data body:
      | key   | value        |
      | files | $example.txt |
    * url path "$environmentUrl/files/user"
    * http method "POST"
    * headers
      | key             | value      |
      | X-Session-Token | $token     |
      | Submission_Type | text/plain |
    When request is performed
    Then http status code "200" is returned

    Given a http request with body:
      """
      Submission	S-BSST123
      Title	Sample Submission

      Study

      File	example.txt
      """
    * url path "$environmentUrl/submissions"
    * http method "POST"
    When request is performed
    Then http status code "200" is returned with body:
    """
       {
          "accno": "S-BSST123",
          "attributes": [
            {
              "name": "Title",
              "value": "Simple Submission with a file"
            }
          ],
          "section": {
            "type": "Study",
            "files": [

                {
                  "path": "example.txt",
                  "size": 14,
                  "attributes": [
                      {
                          "name": "md5",
                          "value": "NOT_CALCULATED"
                      }
                  ],
                  "type": "file"
                }
             ]
          },
          "type": "submission"
       }
       """
    And the file "$ftpUrl/S-BSST/123/S-BSST123/Files/example.txt" contains:
      """
        Sample content
      """
    And the file "$ftpUrl/S-BSST/123/S-BSST123/S-BSST123.json" contains:
      """
      {
        "accno" : "S-BSST123",
        "attributes" : [ {
          "name" : "Title",
          "value" : "Simple Submission with a file"
        } ],
        "section" : {
          "type" : "Study",
          "files" : [ {
            "path" : "example.txt",
            "size" : 14,
            "attributes" : [ {
              "name" : "md5",
              "value" : "01A33621980DDF27A6538708D5E93C53"
            } ],
            "type" : "file"
          } ]
        },
        "type" : "submission"
      }
      """
    And the file "$ftpUrl/S-BSST/123/S-BSST123/S-BSST123.tsv" contains:
      """
      Submission	S-BSST123
      Title	Simple Submission with a file

      Study

      File	example.txt
      md5	01A33621980DDF27A6538708D5E93C53

      """
    And the file "$ftpUrl/S-BSST/123/S-BSST123/S-BSST123.xml" contains:
      """
      <?xml version='1.0' encoding='UTF-8'?><submission accno="S-BSST123">
      <attributes>
        <attribute>
          <name>Title</name>
          <value>Simple Submission with a file</value>
        </attribute>
      </attributes>
      <section type="Study">
        <files>
          <file size="14">
            <path>example.txt</path>
            <type>file</type>
            <attributes>
                <attribute>
                  <name>md5</name>
                  <value>01A33621980DDF27A6538708D5E93C53</value>
                </attribute>
              </attributes>
            </file>
          </files>
        </section>
      </submission>

      """