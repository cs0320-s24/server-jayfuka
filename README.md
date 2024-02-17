
# Project Details

Project created by: Fuka Ikeda (fikeda) and Jay Gopal (jgopal)

Time Spent: 21 Hours

GitHub Link: https://github.com/cs0320-s24/server-jayfuka

A server that utilizes the American Community Survey (ACS) 1-Year Application Programming Interface (API) to access and analyze Census data.

Our package also allows one to use API calls to load, view, and search through `ACS_Five_Year.csv`, which is stored on the server.

Built for the Mesh Network Community Coalition in order to quickly identify how much access to the internet each area has.

# Design Choices

We chose to create one class (`ACSDataSource`) in which there are two clear options for fetching broadband data:
1. No caching (`broadbandNoCache`)
2. With caching (`broadbandWithCache`)

This allows flexibility with respect to requests. We made separate API endpoints for both of the above (see the `How To`) section.
If someone wants to prioritize speed, the option with caching is preferable. If, on the other hand, they always want the most up-to-date information, they can disable caching.

We set the CSV to be the ACS five year file. However, given the context of the Mesh Network Community Coalition having many knowledgeable coders, we made this easy to access and modify. See `csv_file_path` in `CSVHandler.java`

We made sure to build in redundancy into our package. When one uses `/broadband` or `/broadbandNoCache`, the result will not only give the percentage of people in a state or county with broadband access, but also:
- The datetime of the interaction with the ACS API
- The state requested
- The county requested
- Any additional information the ACS API returns at the given time, for the given state and county, along with broadband information.
This is because the additional information can always be filtered away, but dealing with a lack of information is difficult. We give this flexibility to the end user.


# Errors/Bugs

Please report any bugs you encounter via the `Issues` tab above! As of 2/16/24, there are no known bugs or errors.

Here are some errors that we have patched, but could lead to a bug in the future:

1. Newlines: We manually process line endings when interfacing with the ACS API for our tests. This is because some machines may have `\n`, some may use `\r`, and others may use `\r\n`. We saw this as acceptable since the newline characters are not meant to communicate information (at least not based on the ACS API documentation).
2. Spark Setup: We needed to synchronize the setup of the Spark server (see `setup_before_everything` in `TestCSVHandler.java`). If one wanted to run multiple instances, this would need to be changed.
3. Caching: Our cache is highly efficient, functional, and rapid. We know that Java keeps the load factor below 0.75, ensuring near-constant lookup times. However, an area for improvement would be incorporating Guava.


# Tests

Our tests are broken into four files:

1. `TestACSDataSource`: API requests from the ACS API are tested in this file. Specifically, the following are tested:
- The use of the broadband requests, both with and without cache
- Sequential requests, with and without cache
- The datetime returned by each request
- 

2. `TestBroadbandHandler`: The focus is on the following:
- Ability of the API to return any information successfully
- Correctly returning information when a valid state is given
- Behaving correctly when an incorrect state is given (including not crashing the server)
  See `BroadbandHandler` for the implementation of the methods tested.

3. `TestBroadbandHandlerNoCache`: The focus is on the same as the above, but the broadband handler without caching is used.
See `BroadbandHandlerNoCache` for the implementation of the methods tested.

4. `TestCSVHandler`: Separate from the interface with the ACS API, our package provides the ability to search through the ACS five year CSV file, and that is tested here.
The following are included in the test suite:
- `testLoad`: tests the ability of `loadCSV` to successfully create a `CSVParser` based on the given (hard-coded) CSV file.
- `testView`: ensures that the correct CSV file contents are returned, in their entirety.
- `testSearch`: looks into a specific `searchCSV` API query to ensure searching returns exactly what is desired, and nothing more.


# How to

Click Run to start the server. Server will start at http://localhost:3232. All other input is through the endpoints and query parameters.

Endpoints:

`/loadcsv`: Loads a CSV file into memory.

`/viewcsv`: Users are able to view the loaded CSV data. If no CSV is loaded, an error response is returned.

`/searchcsv`: Performs a search operation on the loaded CSV data based on specified search criteria. Search query parameters are `searchValue`, `hasHeader`, `columnIndex`, `colIdentifierIsIndex`, and `columnName`.
To see details and examples of CSV search, please visit the CSV parser package used for the current package: https://github.com/cs0320-s24/csv-JayRGopal

`/broadband`: Uses the ACS API to fetch broadband information, with caching.

`/broadbandNoCache`: Uses the ACS API to fetch broadband information. No caching (every request gets the information again)



