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
    with open('MenuIntegrationTest.properties') as file:
        for line in file:
            if CONFIG_SEPARATOR in line:
                name, val = line.split(CONFIG_SEPARATOR, 1)
                config[name] = val.rstrip()

def createMenuItem(menuItem):
    try:
        requestBody = {  "catagory": menuItem["catagory"], "itemName": menuItem["itemName"], "discription": menuItem["discription"], "cost": menuItem["cost"]}
        response = requests.post(baseURL + "/menuItem", json=requestBody)
        if response.status_code == 201:
            response = requests.get(baseURL + "/menuItem")
            if response.status_code == 200:
                menuItem = response.json()
                print("TEST PASSED - createMenuItem")
                return menuItem[-1]["id"]
            raise AbortTest("TEST ABORTED - Data base was not empty at the start of integration testing. Tests will fail even if working correctly. Start new container of database and run again.")
        raise AbortTest("TEST FAILED - createMenuItem: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - createMenuItem: Error - " + str(e))
     
def checkInTable(expected):
    try:
        response = requests.get(baseURL + "/menuItem")
        if response.status_code == 200:
            items = response.json()
            if expected in items:
                print("TEST PASSED - checkInTable")
            else:
                print("TEST FAILED - checkInTable: \nExpected - " + str(expected) + " \nActual   - " + str(items))
        else:
            print("TEST FAILED - checkInTable: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - checkInTable: Error - " + str(e))

def getMenuItemById(expected, id):
    try:
        response = requests.get(baseURL + "/menuItem/" + str(id))
        if response.status_code == 200:
            item = response.json()
            if item == expected:
                print("TEST PASSED - getMunuById")
            else:
                print("TEST FAILED - getMunuById: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            print("TEST FAILED - getMunuById: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - getMunuById: Error - " + str(e))

def getMenuItemHistroyById(expected, id):
    try:
        response = requests.get(baseURL + "/menuItem/" + str(id) + "/history")
        if response.status_code == 200:
            items = response.json()
            if items == expected:
                print("TEST PASSED - getMenuItemHistroyById")
            else:
                print("TEST FAILED - getMenuItemHistroyById: \nExpected - " + str(expected) + " \nActual   - " + str(items))
        else:
            print("TEST FAILED - getMenuItemHistroyById: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - getMenuItemHistroyById: Error - " + str(e))

def updateMenuItemById(expected, id, query):
    try:
        response = requests.put(baseURL + "/menuItem/" + str(id) + query)
        if response.status_code == 200:
            item = response.json()
            if item == expected:
                print("TEST PASSED - updateMenuItemById")
            else:
                print("TEST FAILED - updateMenuItemById: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            print("TEST FAILED - updateMenuItemById: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - updateMenuItemById: Error - " + str(e))

        
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
        baseURL = "http://" + config['menuAPI.host'] + ":" + config['menuAPI.port']
        try:
            menuItem = {"id": None, "catagory": "string", "discription": "string", "cost": 0.0, "revisionDate": dateFormated, "itemName": "string"}

            #post
            intID = createMenuItem(menuItem)
            menuItem['id'] = intID

            #get
            checkInTable(menuItem)
            getMenuItemById(menuItem, menuItem["id"])
            getMenuItemHistroyById([menuItem], menuItem["id"])

            #update
            origenalMenuItem = menuItem

            lastMenuItem = origenalMenuItem.copy()
            newItem = "?catagory=Supper"
            lastMenuItem['catagory'] = "Supper"
            updateMenuItemById(lastMenuItem, menuItem["id"], newItem)
            getMenuItemById(lastMenuItem, menuItem["id"])
            
            currentMenuItem = lastMenuItem.copy()
            newItem = "?catagory=Entry"
            currentMenuItem['catagory'] = "Entry"
            updateMenuItemById(currentMenuItem, menuItem["id"], newItem)

            checkInTable(currentMenuItem)
            getMenuItemHistroyById([currentMenuItem, lastMenuItem, origenalMenuItem], menuItem["id"])

            #404 checks
            requestBody = {"catagory": menuItem['catagory']}
            testFor404(requests.get(baseURL + "/menuItem/" + str(menuItem["id"] + 9999)))
            testFor404(requests.put(baseURL + "/menuItem/" + str(menuItem["id"] + 9999), json=requestBody))
            testFor404(requests.get(baseURL + "/menuItem/" + str(menuItem["id"] + 9999) + "/history"))
        except AbortTest as err:
            print(err)
    except (KeyboardInterrupt, SystemExit) as e:
        raise e
