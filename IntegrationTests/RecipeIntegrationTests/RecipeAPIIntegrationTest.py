import requests
import json
import time
from datetime import datetime

CONFIG_SEPARATOR = "="
config = {}
baseURL = ""
inventoryItemsCreated = []

class AbortTest(Exception):
    def __init__(self, message):
        self.message = message
    def __str__(self):
        return self.message

def loadConfig():
    with open('MenuIntegrationTest.properties') as file:
        for line in file:
            if CONFIG_SEPARATOR in line:
                name, val = line.split(CONFIG_SEPARATOR, 1)
                config[name] = val.rstrip()

def getMenuItems():
    try:
        response = requests.get(menuBaseURL + "/api/menuItem")
        if response.status_code == 200:
            existingItems = response.json()
            #Two items already exist.
            if len(existingItems) > 2:
                return existingItems[0]["id"], existingItems[1]["id"]

            #One item exists but creating one more.
            elif len(existingItems) == 1:
                response = requests.post(menuBaseURL + "/api/menuItem", {"catagory": "TestCat", "itemName": "TestItem", "discription": "Menu Item created to be used in testing.", "cost": 0})
                if response.status_code == 200:
                    return existingItems[0]["id"], response.json()["id"]
                else:
                    raise AbortTest("TEST ABORTED - getMenuItems: Failed to create menuItems.")

            #Need to create two items.
            elif len(existingItemss) == 0:
                response = requests.post(menuBaseURL + "/api/menuItem", {"catagory": "TestCat", "itemName": "TestItem", "discription": "Menu Item created to be used in testing.", "cost": 0})
                response = requests.post(menuBaseURL + "/api/menuItem", {"catagory": "TestCat", "itemName": "TestItem", "discription": "Menu Item created to be used in testing.", "cost": 0})
                if response.status_code == 200:
                    return response.json()["id"]-1, response.json()["id"]
                else:
                    raise AbortTest("TEST ABORTED - getMenuItems: Failed to create menuItems.")
        else:
            raise AbortTest("TEST ABORTED - getMenuItems: Status code not 200")
    except requests.ConnectionError as e:
        raise AbortTest("TEST ABORTED - getMenuItems: Error - " + str(e))

def getInventoryItems():
    try:
        response = requests.get(inventoryBaseURL + "/api/inventory")
        if response.status_code == 200:
            existingItems = response.json()
            #Two items already exist.
            if len(existingItems) > 2:
                return existingItems[0]["id"], existingItems[1]["id"]

            #One item exists but creating one more.
            elif len(existingItems) == 1:
                response = requests.post(inventoryBaseURL + "/api/inventory", {"name": "TestItem","units": 0,"unitType": "Test","restockAt": 0,"restockAmount": 0})
                if response.status_code == 200:
                    inventoryItemsCreated.append(response.json()["id"])
                    return existingItems[0]["id"], response.json()["id"]
                else:
                    raise AbortTest("TEST ABORTED - getInventoryItems: Failed to create inventory item.")

            #Need to create two items.
            elif len(existingItemss) == 0:
                response = requests.post(inventoryBaseURL + "/api/inventory", {"name": "TestItem1","units": 0,"unitType": "Test","restockAt": 0,"restockAmount": 0})
                response = requests.post(inventoryBaseURL + "/api/inventory", {"name": "TestItem2","units": 0,"unitType": "Test","restockAt": 0,"restockAmount": 0})
                if response.status_code == 200:
                    inventoryItemsCreated.append(response.json()["id"]-1)
                    inventoryItemsCreated.append(response.json()["id"])
                    return response.json()["id"]-1, response.json()["id"]
                else:
                    raise AbortTest("TEST ABORTED - getInventoryItems: Failed to create inventory item.")
        else:
            raise AbortTest("TEST ABORTED - getInventoryItems: Status code not 200")
    except requests.ConnectionError as e:
        raise AbortTest("TEST ABORTED - getInventoryItems: Error - " + str(e))


def cleanUpCreatedInventoryItems():
    for id in inventoryItemsCreated:
        requests.delete(inventoryBaseURL + "/api/inventory/" + str(id))

                
def testFor404(response):
    if response.status_code == 404:
        print("TEST PASSED - testFor404")
    else:
        print("TEST FAILED - testFor404: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))


if __name__ == '__main__':
    try:
        loadConfig()
        today = datetime.utcnow()
        dateFormated = today.strftime("%Y-%m-%d")
        menuBaseURL = "http://" + config['menuAPI.host'] + ":" + config['menuAPI.port']
        inventoryBaseURL = "http://" + config['inventoryAPI.host'] + ":" + config['inventoryAPI.port']
        recipeBaseURL = "http://" + config['recipeAPI.host'] + ":" + config['recipeAPI.port']
        try:
            menuItemOne, menuItemTwo = getMenuItems()
            inventoryItemOne, inventoryItemTwo = getInventoryItems()
        except AbortTest as err:
            cleanUpCreatedInventoryItems()
            print(err)
    except (KeyboardInterrupt, SystemExit) as e:
        raise e
