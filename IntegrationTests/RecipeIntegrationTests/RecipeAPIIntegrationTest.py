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
    with open('RecipeIntegrationTest.properties') as file:
        for line in file:
            if CONFIG_SEPARATOR in line:
                name, val = line.split(CONFIG_SEPARATOR, 1)
                config[name] = val.rstrip()

def getMenuItems():
    try:
        response = requests.post(menuBaseURL + "/api/menuItem", json={"catagory": "TestCat", "itemName": "TestItem", "discription": "Menu Item created to be used in testing.", "cost": 0})
        if response.status_code == 201:
            return response.json()["id"]
        else:
            raise AbortTest("TEST ABORTED - getMenuItems: Failed to create menuItems. Status code: " + str(response.status_code))
    except requests.ConnectionError as e:
        raise AbortTest("TEST ABORTED - getMenuItems: Error - " + str(e))

def getInventoryItems():
    try:
        itemIdOne = None
        response = requests.get(inventoryBaseURL + "/api/inventory/TestItem1")
        if response.status_code == 200:
            itemIdOne = response.json()["id"]
            response = requests.put(inventoryBaseURL + "/api/inventory/TestItem1", json={"name": "TestItem1","units": 100,"unitType": "Test","restockAt": 0,"restockAmount": 0})
            if response.status_code != 200:
                raise AbortTest("getInventoryItems: Update inventory item. - " + str(response))
        elif response.status_code == 404:
            response = requests.post(inventoryBaseURL + "/api/inventory", json={"name": "TestItem1","units": 100,"unitType": "Test","restockAt": 0,"restockAmount": 0})
            if response.status_code == 201:
                inventoryItemsCreated.append(response.json()["id"])
                itemIdOne = response.json()["id"]
            else:
                raise AbortTest("getInventoryItems: Failed to create inventory item. - " + str(response))
        else:
            raise AbortTest("getInventoryItems: Inventory Error - " + str(response))



        response = requests.get(inventoryBaseURL + "/api/inventory/TestItem2")
        if response.status_code == 200:
            response = requests.put(inventoryBaseURL + "/api/inventory/TestItem2", json={"name": "TestItem2","units": 100,"unitType": "Test","restockAt": 0,"restockAmount": 0})
            if response.status_code != 200:
                raise AbortTest("getInventoryItems: Update inventory item. - " + str(response))
            else:
                return itemIdOne, response.json()["id"]
        elif response.status_code == 404:
            response = requests.post(inventoryBaseURL + "/api/inventory", json={"name": "TestItem2","units": 100,"unitType": "Test","restockAt": 0,"restockAmount": 0})
            if response.status_code == 201:
                inventoryItemsCreated.append(response.json()["id"])
                return itemIdOne, response.json()["id"]
            else:
                raise AbortTest("getInventoryItems: Failed to create inventory item. - " + str(response))
        else:
            raise AbortTest("getInventoryItems: Inventory Error - " + str(response))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - getInventoryItems: Error - " + str(e))

def cleanUpCreatedInventoryItems():
    for id in inventoryItemsCreated:
        requests.delete(inventoryBaseURL + "/api/inventory/" + str(id))

def getRecipeUniqueMenuItemIDs(expected=None):
    try:
        response = requests.get(recipeBaseURL + "/api/recipe")
        if response.status_code == 200:
            item = response.json()
            if (expected == None):
                return item
            if item == expected:
                print("TEST PASSED - getRecipeUniqueMenuItemIDs")
            else:
                print("TEST FAILED - getRecipeUniqueMenuItemIDs: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            print("TEST FAILED - getRecipeUniqueMenuItemIDs: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - getRecipeUniqueMenuItemIDs: Error - " + str(e))

def getRecipeByMenuItemID(expected, menuItemId):
    try:
        response = requests.get(recipeBaseURL + "/api/recipe/" + str(menuItemId))
        if response.status_code == 200:
            item = response.json()
            if item == expected:
                print("TEST PASSED - getRecipeByMenuItemID")
            else:
                print("TEST FAILED - getRecipeByMenuItemID: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            print("TEST FAILED - getRecipeByMenuItemID: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - getRecipeByMenuItemID: Error - " + str(e))

def getRecipeIngredientByID(expected, ingredientId):
    try:
        response = requests.get(recipeBaseURL + "/api/recipe/ingredient/" + str(ingredientId))
        if response.status_code == 200:
            item = response.json()
            if item == expected:
                print("TEST PASSED - getRecipeIngredientByID")
            else:
                print("TEST FAILED - getRecipeIngredientByID: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            print("TEST FAILED - getRecipeIngredientByID: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - getRecipeIngredientByID: Error - " + str(e))


def createRecipe(expected, menuItemId, ingredients):
    try:
        response = requests.post(recipeBaseURL + "/api/recipe/" + str(menuItemId), json=ingredients)
        if response.status_code == 201:
            item = response.json()
            expected[0]["id"] = item[0]["id"]
            if item == expected:
                print("TEST PASSED - createRecipe")
                return item[0]["id"]
            else:
                print("TEST FAILED - createRecipe: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            print("TEST FAILED - createRecipe: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - createRecipe: Error - " + str(e))


def updateIngredient(expected, ingredientId, request):
    try:
        response = requests.put(recipeBaseURL + "/api/recipe/ingredient/" + str(ingredientId), json=request)
        if response.status_code == 200:
            item = response.json()
            if item == expected:
                print("TEST PASSED - updateIngredient")
                return item["id"]
            else:
                print("TEST FAILED - updateIngredient: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            print("TEST FAILED - updateIngredient: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - updateIngredient: Error - " + str(e))

def getUnitsInInventory(inventoryItemId):
    try:
        response = requests.get(inventoryBaseURL + "/api/inventory/" + str(inventoryItemId))
        if response.status_code == 200:
            return response.json()["units"]
        else:
            raise AbortTest("TEST ABORTED - getInventoryItems: Status was " + str(response.status_code))
    except requests.ConnectionError as e:
        raise AbortTest("TEST ABORTED - getInventoryItems: Error - " + str(e))

def fireMenuItem(expectedUnits, menuItemId, inventoryItemIds):
    try:
        try:
            origenalUnits = []
            for inventoryItemId in inventoryItemIds:
                origenalUnits += [getUnitsInInventory(inventoryItemId)]

            response = requests.post(recipeBaseURL + "/api/recipe/" + str(menuItemId) + "/fire")
            if response.status_code == 200:
                for inventoryItemId, expectedUnit in zip(inventoryItemIds, expectedUnits):
                    newUnits = getUnitsInInventory(inventoryItemId)
                    if expectedUnit == newUnits:
                        continue
                    else:
                        print("TEST FAILED - fireMenuItem: New units expected " + str(expectedUnit) + " but was " + str(newUnits))
                        return
                print("TEST PASSED - fireMenuItem")
            else:
                print("TEST FAILED - fireMenuItem: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
        except AbortTest as e:
            print("TEST FAILED - fireMenuItem: Error thrown by getUnitsInInventory. Error - " + str(e))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - fireMenuItem: Error - " + str(e))

def fireIngredient(expectedUnit, ingredientId, inventoryItemId):
    try:
        try:
            origenalUnits = getUnitsInInventory(inventoryItemId)
            response = requests.post(recipeBaseURL + "/api/recipe/ingredient/" + str(ingredientId) + "/fire")
            if response.status_code == 200:
                newUnits = getUnitsInInventory(inventoryItemId)
                if expectedUnit == newUnits:
                    print("TEST PASSED - fireIngredient")
                else:
                    print("TEST FAILED - fireIngredient: New units expected " + str(expectedUnit) + " but was " + str(newUnits))
            else:
                print("TEST FAILED - fireIngredient: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
        except AbortTest as e:
            print("TEST FAILED - fireIngredient: Error thrown by getUnitsInInventory. Error - " + str(e))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - fireIngredient: Error - " + str(e))

def deleteIngredient(ingredientId):
    try:
        response = requests.delete(recipeBaseURL + "/api/recipe/ingredient/" + str(ingredientId))
        if response.status_code == 200:
            response = requests.get(recipeBaseURL + "/api/recipe/ingredient/" + str(ingredientId))
            if response.status_code == 404:
                print("TEST PASSED - deleteIngredient")
            else:
                print("TEST FAILED - deleteIngredient: Response status code of get after delete was " + str(response.status_code))
        else:
            print("TEST FAILED - deleteIngredient: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - deleteIngredient: Error - " + str(e))

def testFor404(response):
    if response.status_code == 404:
        print("TEST PASSED - testFor404")
    else:
        print("TEST FAILED - testFor404: Response was " + str(response))


if __name__ == '__main__':
    try:
        loadConfig()
        today = datetime.utcnow()
        dateFormated = today.strftime("%Y-%m-%d")
        menuBaseURL = "http://" + config['menuAPI.host'] + ":" + config['menuAPI.port']
        inventoryBaseURL = "http://" + config['inventoryAPI.host'] + ":" + config['inventoryAPI.port']
        recipeBaseURL = "http://" + config['recipeAPI.host'] + ":" + config['recipeAPI.port']
        try:
            menuItemId = getMenuItems()
            inventoryItemOneId, inventoryItemTwoId = getInventoryItems()
            initialUniqueMenuItemIDs = getRecipeUniqueMenuItemIDs()

            #create
            createRequest = [{"inventoryItemId": inventoryItemOneId, "quantityUsed": 1.0}]
            expected = {"id": 0, "menuItemId": menuItemId, "inventoryItemId": createRequest[0]["inventoryItemId"], "quantityUsed": createRequest[0]["quantityUsed"]}
            expected["id"] = createRecipe([expected], expected["menuItemId"], createRequest)

            #get
            getRecipeUniqueMenuItemIDs(initialUniqueMenuItemIDs + [menuItemId])
            getRecipeByMenuItemID([expected], expected["menuItemId"])
            getRecipeIngredientByID(expected, expected["id"])

            #put
            updateRequest = {"inventoryItemId": inventoryItemTwoId, "quantityUsed": 5.2}
            expected2 = {"id": expected["id"], "menuItemId": expected["menuItemId"], "inventoryItemId": updateRequest["inventoryItemId"], "quantityUsed": updateRequest["quantityUsed"]}
            updateIngredient(expected2, expected2["id"], updateRequest)
            getRecipeIngredientByID(expected2, expected2["id"])


            #fire
            expected["id"] = createRecipe([expected], expected["menuItemId"], createRequest)
            getRecipeByMenuItemID([expected, expected2], expected["menuItemId"])
            fireMenuItem([100 - expected["quantityUsed"], 100 - expected2["quantityUsed"]], menuItemId, [expected["inventoryItemId"], expected2["inventoryItemId"]])
            fireIngredient(100 - (expected["quantityUsed"]*2), expected["id"], expected["inventoryItemId"])
            fireMenuItem([100 - (expected["quantityUsed"]*3), 100 - (expected2["quantityUsed"]*2)], menuItemId, [expected["inventoryItemId"], expected2["inventoryItemId"]])

            #delete
            deleteIngredient(expected["id"])
            deleteIngredient(expected2["id"])

            #404
            testFor404(requests.get(recipeBaseURL + "/api/recipe/0"))
            testFor404(requests.get(recipeBaseURL + "/api/recipe/ingredient/0"))
            testFor404(requests.put(recipeBaseURL + "/api/recipe/ingredient/0", json=updateRequest))
            testFor404(requests.delete(recipeBaseURL + "/api/recipe/ingredient/0"))

            cleanUpCreatedInventoryItems()
            #reminded to delete test menuItems
            print("A menuItem has been created to be used for testing. There is no API call to remove menuItems from the database. " +
                    "Consider loging into the database and removing it manually. The menuItem's id is " + str(menuItemId))
        except AbortTest as err:
            cleanUpCreatedInventoryItems()
            print(err)
    except (KeyboardInterrupt, SystemExit) as e:
        raise e
