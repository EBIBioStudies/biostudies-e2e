Feature: Submit submissions with file lists

  Shows JSON submissions containing different fileList formats.
  Also submission considering empty files list and reusing previous version fileLists.

  Background:
    Given the setup information
      | environmentUrl | http://localhost:8080        |
      | ftpUrl         | /Users/miguel/Biostudies/ftp |
      | userName       | admin_user@ebi.ac.uk         |
      | userPassword   | 123456                       |
    And a http request with body:
      """
      {
        "login":"$userName",
        "password":"$userPassword"
      }
      """
    * header
      | Content-Type | application/json |
    * url path "$environmentUrl/auth/login"
    * http method "POST"
    When request is performed
    Then http status code "200" is returned
    And the JSONPath value "$.sessid" from response is saved into "token"

  Scenario: Submit a JSON submission with a TSV file list
    Given the file "fileList" named "fileList.tsv" with content
    """
    Files	GEN
    file4.txt	ABC
    """
    * the file "fileListFile" named "file4.txt" with content
    """
    File content
    """
    * the variable "submission" with value
    """
    {"accno": "S-TEST4", "attributes": [{"name": "Title", "value": "Test Submission"}], "section": {"accno": "SECT-001", "type": "Study", "attributes": [{"name": "Title", "value": "Root Section"}, {"name": "File List", "value": "FileList.tsv"}]}}
    """
    And a http request with form-data body:
      | files      | $fileList   | $fileListFile |
      | submission | $submission |               |
    * url path "$environmentUrl/submissions"
    * http method "POST"
    * headers
      | X-Session-Token | $token              |
      | Content-Type    | multipart/form-data |
      | Submission_Type | application/json    |

    When multipart request is performed
    Then http status code "200" is returned



