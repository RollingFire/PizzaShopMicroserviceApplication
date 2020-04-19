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

def createMenu(menu):
    try:
        requestBody = {"menuName": menu['menuName'], "items": menu['items']}
        response = requests.post(baseURL + "/api/menu", json=requestBody)
        if response.status_code == 201:
            response = requests.get(baseURL + "/api/menu")
            if response.status_code == 200:
                menu = response.json()
                print("TEST PASSED - createMenu")
                return menu[-1]["id"]
            raise AbortTest("TEST ABORTED - Data base was not empty at the start of integration testing. Tests will fail even if working correctly. Start new container of database and run again.")
        raise AbortTest("TEST FAILED - createMenu: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - createMenu: Error - " + str(e))
     
def checkInTable(expected):
    try:
        response = requests.get(baseURL + "/api/menu")
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

def getMenuById(expected, id):
    try:
        response = requests.get(baseURL + "/api/menu/" + str(id))
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

def getMenuByName(expected, name):
    try:
        response = requests.get(baseURL + "/api/menu/" + name)
        if response.status_code == 200:
            item = response.json()
            if item == expected:
                print("TEST PASSED - getMenuByName")
            else:
                print("TEST FAILED - getMenuByName: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            print("TEST FAILED - getMenuByName: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - getMenuByName: Error - " + str(e))

def getMenuHistroyById(expected, id):
    try:
        response = requests.get(baseURL + "/api/menu/" + str(id) + "/history")
        if response.status_code == 200:
            items = response.json()
            if items == expected:
                print("TEST PASSED - getMenuHistroyById")
            else:
                print("TEST FAILED - getMenuHistroyById: \nExpected - " + str(expected) + " \nActual   - " + str(items))
        else:
            print("TEST FAILED - getMenuHistroyById: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - getMenuHistroyById: Error - " + str(e))

def getMenuHistroyByName(expected, name):
    try:
        response = requests.get(baseURL + "/api/menu/" + name + "/history")
        if response.status_code == 200:
            items = response.json()
            if items == expected:
                print("TEST PASSED - getMenuHistroyByName")
            else:
                print("TEST FAILED - getMenuHistroyByName: \nExpected - " + str(expected) + " \nActual   - " + str(items))
        else:
            print("TEST FAILED - getMenuHistroyByName: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - getMenuHistroyByName: Error - " + str(e))

def updateMenuById(expected, id, newItem):
    try:
        response = requests.put(baseURL + "/api/menu/" + str(id), json={"items": newItem})
        if response.status_code == 200:
            item = response.json()
            if item == expected:
                print("TEST PASSED - updateMenuById")
            else:
                print("TEST FAILED - updateMenuById: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            print("TEST FAILED - updateMenuById: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - updateMenuById: Error - " + str(e))

def updateMenuByName(expected, name, newItem):
    try:
        response = requests.put(baseURL + "/api/menu/" + name, json={"items": newItem})
        if response.status_code == 200:
            item = response.json()
            if item == expected:
                print("TEST PASSED - updateMenuByName")
            else:
                print("TEST FAILED - updateMenuByName: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            print("TEST FAILED - updateMenuByName: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - updateMenuByName: Error - " + str(e))

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
            if requests.get(baseURL + "/api/menu/IntegrationTestMenu").status_code != 404:
                raise AbortTest("Integration abouted. IntegrationTestMenu already exists. Remove and try again.")
            menu = {"id": None, "menuName": "IntegrationTestMenu", "items": [], "revisionDate": dateFormated}

            #post
            intID = createMenu(menu)
            menu['id'] = intID
            #menu['id'] = 2

            #get
            checkInTable(menu)
            getMenuById(menu, menu["id"])
            getMenuByName(menu, menu["menuName"])
            getMenuHistroyById([menu], menu["id"])
            getMenuHistroyByName([menu], menu["menuName"])

            #update
            origenalMenu = menu

            lastMenu = origenalMenu.copy()
            newItem = [1,2,4]
            lastMenu['items'] = newItem
            updateMenuById(lastMenu, menu["id"], newItem)
            getMenuById(lastMenu, menu["id"])
            
            currentMenu = lastMenu.copy()
            newItem = [4,5,6]
            currentMenu['items'] = newItem
            updateMenuByName(currentMenu, menu["menuName"], newItem)
            getMenuByName(currentMenu, menu["menuName"])

            checkInTable(currentMenu)
            getMenuHistroyById([currentMenu, lastMenu, origenalMenu], menu["id"])
            getMenuHistroyByName([currentMenu, lastMenu, origenalMenu], menu["menuName"])

            #404 checks
            requestBody = {"items": menu['items']}
            testFor404(requests.get(baseURL + "/api/menu/" + str(menu["id"] + 9999)))
            testFor404(requests.get(baseURL + "/api/menu/" + "asdfawetlhnijbvn k;jdf asdfahe"))
            testFor404(requests.put(baseURL + "/api/menu/" + str(menu["id"] + 9999), json=requestBody))
            testFor404(requests.put(baseURL + "/api/menu/" + "asdfawetlhnijbvn k;jdf asdfahe", json=requestBody))
            testFor404(requests.get(baseURL + "/api/menu/" + str(menu["id"] + 9999) + "/history"))
            testFor404(requests.get(baseURL + "/api/menu/" + "asdfawetlhnijbvn k;jdf asdfahe" + "/history"))
            
        except AbortTest as err:
            print(err)
    except (KeyboardInterrupt, SystemExit) as e:
        raise e
