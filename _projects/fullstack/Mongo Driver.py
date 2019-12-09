import json
from bson import json_util
from pymongo import MongoClient
import os

connection = MongoClient('localhost', 27017)
db = connection['market']
collection = db['stocks']

# Take an input stream and load it as a JSON document, inserting into the database
# Parameters: stream (IOStream)
def insert_document(stream):
    try:
        document = json.load(stream)
        result = collection.insert_one(document)
    except Exception as e: # Insert can result in duplicate key errors, bad JSON formatting gives JSONDecodeError
        print(e)
        return False
    return True

# Update the volume value of the associated ticker
# Parameters: ticker (string), volume (float)
def update_volume(ticker, volume):
    try:
        volume = float(volume)
        query = {"Ticker" : ticker}
        document = {"$set" : {"Volume" : volume}}
        result = collection.update_one(query, document, upsert=False).raw_result # Return the update result
    except Exception as e: # Unsure what errors can be raised here
        return e
    return result

# Replace the document associated with the ticker using the input stream
# Parameters: ticker (string), stream (IOStream)
def replace_document(ticker, stream):
    try:
        query = {"Ticker" : ticker}
        document = json.load(stream)
        result = collection.replace_one(query, document, upsert=False).raw_result
    except Exception as e:
        return e
    return result

# Return the document associated with the ticker
# Parameters: ticker (string)
def read_document(ticker):
    try:
        query = {"Ticker" : ticker}
        result = collection.find_one(query) # Returns the first result or None
    except Exception as e: # Unsure what errors can be raised here
        return e
    return result

# Delete the document associated with the ticker
# Parameters: ticker (string)
def delete_document(ticker):
    try:
        query = {"Ticker" : ticker}
        result = collection.delete_one(query).raw_result # Return the delete result
    except Exception as e: # Unsure what errors can be raised here
        return e
    return result

# Return the number of documents with a Simple Moving Average between the bounds
# Parameters: low, high (float)
def FiftyDayAverage(low, high):
    try:
        query = {"50-Day Simple Moving Average" : {"$gte" : low, "$lte" : high}}
        result = collection.find(query).count() # find().count() is deprecated, this may not be consistent
    except Exception as e:
        return e
    return result

# Return a list of tickers in the specified industry
# Parameters: industry (string)
def find_industry(industry):
    try:
        query = {"Industry" : industry}
        result = [ticker for ticker in collection.find(query, {"_id" : 0, "Ticker" : 1})]
    except Exception as e:
        return e
    return result

# Return a list of industries in the specified sector, sorted by total shares
# Parameters: sector (string)
def sharesByIndustryForSector(sector):
    try:
        pipeline = [{"$match" : {"Sector" : sector}}, 
                    {"$group" : {"_id" : "$Industry", "_totalShares" : {"$sum" : "$Shares Outstanding"}}},
                    {"$project" : {"Industry" : "$_id", "Total Shares" : "$_totalShares", "_id" : 0}}]
        result = [ticker for ticker in collection.aggregate(pipeline)]
    except Exception as e:
        return e
    return result

# Return a summary of the stock associated with the ticker
# Parameters: ticker (string)
def stock_summary(ticker):
    try:
      query = {"Ticker" : ticker}
      result = collection.find_one(query, {"Ticker" : 1, "Profit Margin": 1, "Return on Assets" : 1, "P/E" : 1,
                                           "Performance (Quarter)" : 1, "52-Week High" : 1, "52-Week Low" : 1})
    except Exception as e:
        return e
    return result

# Return the quarterly performance and ticker of all stocks in the specified industry
# Parameters: industry (string)
def industry_report(industry):
    try:
        query = {"Industry" : industry}
        result = list(collection.find(query, {"Ticker" : 1, "Performance (Quarter)" : 1}).sort([["Performance (Quarter)", 1]]).limit(5))
    except Exception as e:
        return e
    return result

# Export to a .CSV file the specified fields from all stocks.
# Parameters: queryItems (list)
def export_query(queryItems):
    try:
      with open("fieldFile.txt", 'w') as fieldFile:
        fieldFile.writelines("%s\n" % item for item in queryItems)
      os.system('mongoexport --db=market --collection=stocks --csv --out=stocks.csv --fieldFile=fieldFile.txt')
    except Exception as e:
      return e
    return True

#The following main function is for advanced query testing purposes
def main():
    
    print "Return number of documents with 50 day average between -0.006 and -0.004"
    print FiftyDayAverage(-0.006, -0.005)
    
    print "Search for stocks and return tickers with Industry 'Medical Laboratories & Research'"
    print find_industry("Medical Laboratories & Research")
    
    print "Return total shares grouped by industry in the sector 'Technology'"
    print sharesByIndustryForSector("Technology")
    
    print "Export fields to CSV"
    print export_query(["Ticker", "Company", "Sector", "Industry", "Profit Margin", "Return on Assets", "P/E", 
                        "Performance (Quarter)", "52-Week High", "52-Week Low"])
    
    
if __name__ == '__main__':
    main()