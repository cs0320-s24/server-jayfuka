
# Project Details

Project created by: Fuka Ikeda (fikeda) and Jay Gopal (jgopal)

Time Spent: 15 Hours

GitHub Link: https://github.com/cs0320-s24/server-jayfuka

A server that utilizes the American Community Survey (ACS) 1-Year Application Programming Interface (API) to access and analyze Census data.

Built for the Mesh Network Community Coalition in order to quickly identify how much access to the internet each area has.

# Design Choices



# Errors/Bugs

N/A

# Tests

# How to

Click Run to start the server. Server will start at http://localhost:3232. All other input is through the endpoints and query parameters.

Endpoints:

/loadcsv: Loads a CSV file into memory. 

/viewcsv: Users are able to view the loaded CSV data. If no CSV is loaded, an error response is returned.

/searchcsv: Performs a search operation on the loaded CSV data based on specified search criteria. Searchquery parameters are 'searchValue', 'hasHeader', 'columnIndex', 'colIdentifierIsIndex', and 'columnName'.

/broadband: Uses the ACS API to fetch broadband information. 


