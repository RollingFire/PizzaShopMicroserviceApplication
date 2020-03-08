import requests
import json
import time
from datetime import datetime

CONFIG_SEPARATOR = "="
config = {}
baseURL = ""

class AbortTest(Exception):
    def __init__(self, message):
        self.message = message
    def __str__(self):
        return self.message

def loadConfig():
    with open('InventoryIntegrationTest.properties') as file:
        for line in file:
            if CONFIG_SEPARATOR in line:
                name, val = line.split(CONFIG_SEPARATOR, 1)
                config[name] = val.rstrip()

def checkDatabaseInitial():
    try:
        response = requests.get(baseURL + "/inventory")
        if response.status_code == 200:
            inventory = response.json()
            if inventory == []:
                response = requests.post(baseURL + "/inventory/Test")
                if response.status_code == 201:
                    response = requests.get(baseURL + "/inventory")
                    if response.status_code == 200:
                        inventory = response.json()
                        if inventory[0]["id"] == 1:
                            response = requests.delete(baseURL + "/inventory/" + str(1))
                            if response.status_code == 200:
                                response = requests.get(baseURL + "/inventory")
                                if response.status_code == 200:
                                    inventory = response.json()
                                    if inventory == []:
                                        print("TEST PASSED - checkDatabaseInitial")
                                        return
            raise AbortTest("TEST ABORTED - Data base was not empty at the start of integration testing. Tests will fail even if working correctly. Start new container of database and run again.")
        raise AbortTest("TEST FAILED - checkDatabaseInitial: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - checkDatabaseInitial: Error - " + str(e))

def getInventory(expected):
    try:
        response = requests.get(baseURL + "/inventory")
        if response.status_code == 200:
            inventory = response.json()
            if inventory == expected:
                print("TEST PASSED - getInventory")
            else:
                print("TEST FAILED - getInventory: \nExpected - " + str(expected) + " \nActual   - " + str(inventory))
        else:
            print("TEST FAILED - getInventory: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - getInventory: Error - " + str(e))

def getInventoryName(name, expected):
    try:
        response = requests.get(baseURL + "/inventory/" + name)
        if response.status_code == 200:
            item = response.json()
            if item == expected:
                print("TEST PASSED - getInventoryName")
            else:
                print("TEST FAILED - getInventoryName: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            print("TEST FAILED - getInventoryName: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - getInventoryName: Error - " + str(e))

def getInventoryID(id, expected):
    try:
        response = requests.get(baseURL + "/inventory/" + str(id))
        if response.status_code == 200:
            item = response.json()
            if item == expected:
                print("TEST PASSED - getInventoryID")
            else:
                print("TEST FAILED - getInventoryID: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            print("TEST FAILED - getInventoryID: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - getInventoryID: Error - " + str(e))

def deleteInventoryID(id):
    try:
        response = requests.delete(baseURL + "/inventory/" + str(id))
        if response.status_code == 200:
            try:
                response = requests.get(baseURL + "/inventory/" + str(id))
                response.json()
                print("TEST FAILED - deleteInventoryID: Item not deleted.")
            except ValueError:
                print("TEST PASSED - deleteInventoryID")
        else:
            print("TEST FAILED - deleteInventoryID: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - deleteInventoryID: Error - " + str(e))

def deleteInventoryName(name):
    try:
        response = requests.delete(baseURL + "/inventory/" + name)
        if response.status_code == 200:
            try:
                response = requests.get(baseURL + "/inventory/" + str(id))
                response.json()
                print("TEST FAILED - deleteInventoryID: Item not deleted.")
            except ValueError:
                print("TEST PASSED - deleteInventoryID")
        else:
            print("TEST FAILED - deleteInventoryName: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - deleteInventoryName: Error - " + str(e))

def postInventory(item, expected):
    try:
        response = requests.post(baseURL + "/inventory", json=item)
        if response.status_code == 201:
            getResponse = requests.get(baseURL + "/inventory/" + response.json()["name"])
            if getResponse.status_code == 200 and getResponse.json() == expected:
                print("TEST PASSED - postInventory")
            else:
                print("TEST FAILED - postInventory at get validation: Response status code was " + str(getResponse.status_code) + " and data \nExpected - " + str(expected) + " \nActual   - " + str(getResponse.json()))
        else:
            print("TEST FAILED - postInventory at post validation: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - postInventory: Error - " + str(e))

def postInventoryName(name, expected):
    try:
        response = requests.post(baseURL + "/inventory/" + name)
        if response.status_code == 201:
            getResponse = requests.get(baseURL + "/inventory/" + name)
            if getResponse.status_code == 200 and getResponse.json() == expected:
                print("TEST PASSED - postInventoryName")
            else:
                print("TEST FAILED - postInventoryName at get validation: Response status code was " + str(getResponse.status_code) + " and data \nExpected - " + str(expected) + " \nActual   - " + str(getResponse.json()))
        else:
            print("TEST FAILED - postInventoryName at post validation: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - postInventoryName: Error - " + str(e))

def postInventoryRestockID(id, restockAmount, expectedUnits):
    try:
        request = {"unitsAdded":restockAmount}
        response = requests.post(baseURL + "/inventory/restock/" + str(id), json=request)
        if response.status_code == 200:
            getResponse = requests.get(baseURL + "/inventory/" + str(id))
            if getResponse.status_code == 200 and getResponse.json()["units"] == expectedUnits:
                print("TEST PASSED - postInventoryRestockID")
            else:
                print("TEST FAILED - postInventoryRestockID at get validation: Response status code was " + str(getResponse.status_code) + " and data \nExpected - " + str(getResponse.json()["units"]) + " \nActual   - " + str(expectedUnits))
        else:
            print("TEST FAILED - postInventoryRestockID at post validation: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - postInventoryRestockID: Error - " + str(e))

def postInventoryRestockName(name, restockAmount, expectedUnits):
    try:
        request = {"unitsAdded":restockAmount}
        response = requests.post(baseURL + "/inventory/restock/" + name, json=request)
        if response.status_code == 200:
            getResponse = requests.get(baseURL + "/inventory/" + name)
            if getResponse.status_code == 200 and getResponse.json()["units"] == expectedUnits:
                print("TEST PASSED - postInventoryRestockName")
            else:
                print("TEST FAILED - postInventoryRestockName at get validation: Response status code was " + str(getResponse.status_code) + " and data \nExpected - " + str(getResponse.json()["units"]) + " \nActual   - " + str(expectedUnits))
        else:
            print("TEST FAILED - postInventoryRestockName at post validation: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - postInventoryRestockName: Error - " + str(e))

def putInventoryID(id, requestBody, expected):
    try:
        response = requests.put(baseURL + "/inventory/" + str(id), json=requestBody)
        if response.status_code == 200:
            getResponse = requests.get(baseURL + "/inventory/" + str(id))
            if getResponse.status_code == 200 and getResponse.json() == expected:
                print("TEST PASSED - putInventoryID")
            else:
                print("TEST FAILED - putInventoryID at get validation: Response status code was " + str(getResponse.status_code) + " and data \nExpected - " + str(getResponse.json()) + " \nActual   - " + str(expected))
        else:
            print("TEST FAILED - putInventoryID at put validation: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - putInventoryID: Error - " + str(e))

def putInventoryName(name, requestBody, expected):
    try:
        response = requests.put(baseURL + "/inventory/" + name, json=requestBody)
        if response.status_code == 200:
            getResponse = requests.get(baseURL + "/inventory/" + requestBody["name"])
            if getResponse.status_code == 200 and getResponse.json() == expected:
                print("TEST PASSED - putInventoryName")
            else:
                print("TEST FAILED - putInventoryName at get validation: Response status code was " + str(getResponse.status_code) + " and data \nExpected - " + str(getResponse.json()) + " \nActual   - " + str(expected))
        else:
            print("TEST FAILED - putInventoryName at put validation: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - putInventoryName: Error - " + str(e))

def testFor404(response):
    if response.status_code == 404:
        print("TEST PASSED - testFor404")
    else:
        print("TEST FAILED - testFor404: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))


if __name__ == '__main__':
    try:
        loadConfig()
        baseURL = "http://" + config['inventoryAPI.host'] + ":" + config['inventoryAPI.port']
        try:
            checkDatabaseInitial()

            #Creates
            itemOneExpected = {"id": 2, "name": "Sauce", "units": 16, "unitType": "PINTS", "restockAt": 4, "restockAmount": 24}
            itemOneRequest = {"name": "Sauce", "units": 16, "unitType": "PINTS", "restockAt": 4, "restockAmount": 24}
            postInventory(itemOneRequest, itemOneExpected)

            itemTwoExpected = {"id": 3, "name": "Crust", "units": 0, "unitType": "COUNT", "restockAt": 0, "restockAmount": 0}
            postInventoryName(itemTwoExpected["name"], itemTwoExpected)

            #Gets
            getInventory([itemOneExpected, itemTwoExpected])
            getInventoryName(itemOneExpected["name"], itemOneExpected)
            getInventoryID(itemTwoExpected["id"], itemTwoExpected)

            #Restocks
            postInventoryRestockID(3, 3.2, 3.2)
            postInventoryRestockID(3, -2.0, 1.2)
            postInventoryRestockID(3, -5.1, 0)
            postInventoryRestockName("Sauce", 8.0, 24.0)
            postInventoryRestockName("Sauce", -5.5, 18.5)

            #Puts
            putOneExpected = {"id": 2, "name": "Cheese", "units": 32, "unitType": "OZ", "restockAt": 16, "restockAmount": 64}
            putOneRequest = {"name": "Cheese", "units": 32, "unitType": "OZ", "restockAt": 16, "restockAmount": 64}
            putInventoryID(itemOneExpected["id"], putOneRequest, putOneExpected)
            putTwoExpected = {"id": 3, "name": "Peperoni", "units": 6, "unitType": "KG", "restockAt": 0, "restockAmount": 4}
            putTwoRequest = {"name": "Peperoni", "units": 6, "unitType": "KG", "restockAt": 0, "restockAmount": 4}
            putInventoryName(itemTwoExpected["name"], putTwoRequest, putTwoExpected)

            #Deletes
            deleteInventoryID(putTwoExpected["id"])
            deleteInventoryName(putOneExpected["name"])

            #Check 404s
            testFor404(requests.get(baseURL + "/inventory/0"))
            testFor404(requests.delete(baseURL + "/inventory/0"))
            testFor404(requests.put(baseURL + "/inventory/0"))
            testFor404(requests.get(baseURL + "/inventory/AStingThatShouldn'tBeTakenAsANameOfAnItmeBecasueThatWouldBeWeird"))
            testFor404(requests.delete(baseURL + "/inventory/AStingThatShouldn'tBeTakenAsANameOfAnItmeBecasueThatWouldBeWeird"))
            testFor404(requests.put(baseURL + "/inventory/AStingThatShouldn'tBeTakenAsANameOfAnItmeBecasueThatWouldBeWeird"))
            testFor404(requests.post(baseURL + "/inventory/restock/0"))
            testFor404(requests.post(baseURL + "/inventory/restock/AStingThatShouldn'tBeTakenAsANameOfAnItmeBecasueThatWouldBeWeird"))

            #Final check
            getInventory([])
        except AbortTest as err:
            print(err)
    except (KeyboardInterrupt, SystemExit) as e:
        raise e
