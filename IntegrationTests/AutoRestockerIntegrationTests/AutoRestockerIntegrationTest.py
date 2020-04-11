import requests
import json
import time
from datetime import datetime

CONFIG_SEPARATOR = "="
config = {}
baseURL = ""

def loadConfig():
    with open('AutoRestockerIntegrationTest.properties') as file:
        for line in file:
            if CONFIG_SEPARATOR in line:
                name, val = line.split(CONFIG_SEPARATOR, 1)
                config[name] = val.rstrip()

def compareRequestAndExpected(testNumber, returned, expected):
    if (returned == expected):
        print("TEST " + str(testNumber) + " PASSED")
    else:
        print("TEST " + str(testNumber) + " FAILED: returned=" + returned + " expected=" + expected)


def main():
    #items
    one = {"name": "AutoRestockerIntegrationTest1", "units": 10, "unitType": "COUNT", "restockAt": 16, "restockAmount": 4}
    two = {"name": "AutoRestockerIntegrationTest2", "units": 10.2, "unitType": "COUNT", "restockAt": 10.2, "restockAmount": 4.3}
    three = {"name": "AutoRestockerIntegrationTest3", "units": 24.1, "unitType": "COUNT", "restockAt": 18.6, "restockAmount": 64}

    #create items by name
    requests.post(baseURL + "/api/inventory", json=one)
    requests.post(baseURL + "/api/inventory", json=two)
    requests.post(baseURL + "/api/inventory", json=three)

    #wait for auto restock
    time.sleep(int(config["restock.restockFrequencySeconds"]) + 2)

    #check with get by name
    returnedUnites = requests.get(baseURL + "/api/inventory/" + one["name"]).json()["units"]
    compareRequestAndExpected(1, returnedUnites, one['units'] + one['restockAmount'])
    returnedUnites = requests.get(baseURL + "/api/inventory/" + two["name"]).json()["units"]
    compareRequestAndExpected(2, returnedUnites, two['units'] + two['restockAmount'])
    returnedUnites = requests.get(baseURL + "/api/inventory/" + three["name"]).json()["units"]
    compareRequestAndExpected(3, returnedUnites, three['units'])

    #wait for auto restock
    time.sleep(int(config["restock.restockFrequencySeconds"]))

    #check with get by name
    returnedUnites = requests.get(baseURL + "/api/inventory/" + one["name"]).json()["units"]
    compareRequestAndExpected(4, returnedUnites, one['units'] + (one['restockAmount'] * 2))
    returnedUnites = requests.get(baseURL + "/api/inventory/" + two["name"]).json()["units"]
    compareRequestAndExpected(5, returnedUnites, two['units'] + two['restockAmount'])

    #deleting items used for test
    requests.delete(baseURL + "/api/inventory/" + one["name"])
    requests.delete(baseURL + "/api/inventory/" + two["name"])
    requests.delete(baseURL + "/api/inventory/" + three["name"])


if __name__ == '__main__':
    try:
        loadConfig()
        baseURL = "http://" + config['inventoryAPI.host'] + ":" + config['inventoryAPI.port']
        main()
    except (KeyboardInterrupt, SystemExit) as e:
        raise e