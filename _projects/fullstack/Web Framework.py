from Final_Project_1 import *
from io import StringIO
import bottle
from bottle import route, run, request, abort, static_file

"""
Example Command:
curl -H "Content-Type: application/json" -X PSOT -d '<json document>' /stocks/api/v1.0/createStock/<ticker>
"""
@route('/stocks/api/v1.0/createStock/<ticker>', method='POST')
def createStock(ticker):
    document = request.json
    document["Ticker"] = ticker
    io = StringIO(unicode(json.dumps(document)))
    status = insert_document(io)
    if status:
        return "201"
    return "200"
  
"""
Example Command:
curl /stocks/api/v1.0/getStock/<ticker>
"""    
@route('/stocks/api/v1.0/getStock/<ticker>', method='GET')
def getStock(ticker):
    document = read_document(ticker)
    if document is None:
        return "404"
    return json.loads(json_util.dumps(document))

"""
Example Command:
curl -H "Content-Type: application/json" -X PUT -d '<json document>' /stocks/api/v1.0/updateStock/<ticker>
"""
@route('/stocks/api/v1.0/updateStock/<ticker>', method='PUT')
def updateStock(ticker):
    document = request.json
    document["Ticker"] = ticker
    io = StringIO(unicode(json.dumps(document)))
    result = replace_document(ticker, io)
    if result[u'nModified'] == 0:
        return "404"
    else:
        return "200"

"""
Example Command:
curl -X DELETE /stocks/api/v1.0/deleteStock/<ticker>
"""
@route('/stocks/api/v1.0/deleteStock/<ticker>', method='DELETE')
def deleteStock(ticker):
    result = delete_document(ticker)
    if result[u'n'] == 0:
        return "404"
    else:
        return "200"

"""
Example Command:
curl -H "Content-Type: application/json" -X PUT -d '{"list" : ["Ticker 1", "Ticker 2"]}' /stocks/api/v1.0/stockReport
"""
@route('/stocks/api/v1.0/stockReport', method='POST')
def stockReport():
  stockList = request.json["list"]
  return json.loads(json_util.dumps({"Summary" : [stock_summary(stock) for stock in stockList]}))

"""
Example Command:
curl -H "Content-Type: application/json" -X POST -d '{"list" : ["Ticker", "Company", "Sector", "Industry", "Profit Margin", "Return on Assets", 
"P/E", "Performance (Quarter)", "52-Week High", "52-Week Low"]}' /stocks/api/v1.0/exportStockSummary --output stocks2.csv
"""
@route('/stocks/api/v1.0/exportStockSummary', method='POST')
def exportStockSummary():
  stockList = request.json["list"]
  export_query(stockList)
  return static_file("stocks.csv", root="./", download="stocks.csv")

"""
Example Command:
curl /stocks/api/v1.0/industryReport/Medical%20Laboratories%20%26%20Research
"""
@route('/stocks/api/v1.0/industryReport/<industry>', method='GET')
def industryReport(industry):
    result = industry_report(industry)
    print(result)
    return json.loads(json_util.dumps({"Report" : result}))

# Run the bottle application on port 8080
if __name__ == '__main__':
    #app.run(debug=True)
    run(host='localhost', port=8080)