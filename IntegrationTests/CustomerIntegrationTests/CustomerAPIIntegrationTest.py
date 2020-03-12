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
    with open('CustomerIntegrationTest.properties') as file:
        for line in file:
            if CONFIG_SEPARATOR in line:
                name, val = line.split(CONFIG_SEPARATOR, 1)
                config[name] = val.rstrip()

def checkDatabaseInitial():
    try:
        response = requests.get(baseURL + "/customer")
        if response.status_code == 200:
            customers = response.json()
            if customers == []:
                firstName = "Paul"
                lastName = "Blart"
                queryPerams = "?firstName=" + firstName + "&lastName=" + lastName
                response = requests.post(baseURL + "/customer" + queryPerams)
                if response.status_code == 201:
                    response = requests.get(baseURL + "/customer/1")
                    if response.status_code == 200:
                        customer = response.json()
                        if customer["firstName"] == firstName and customer["lastName"] == lastName:
                            response = requests.delete(baseURL + "/customer/1")
                            if response.status_code == 200:
                                response = requests.get(baseURL + "/customer")
                                if response.status_code == 200:
                                    customers = response.json()
                                    if customers == []:
                                        print("TEST PASSED - checkDatabaseInitial")
                                        return
            raise AbortTest("TEST ABORTED - Data base was not empty at the start of integration testing. Tests will fail even if working correctly. Start new container of database and run again.")
        raise AbortTest("TEST FAILED - checkDatabaseInitial: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - checkDatabaseInitial: Error - " + str(e))

def addCustomer(expected, firstName=None, lastName=None, wantedStatus=None):
    try:
        queryPeram = ""
        if firstName != None:
            queryPeram = "/?firstName=" + firstName
        if lastName != None:
            if queryPeram == "":
                queryPeram = "/?"
            queryPeram += "&lastName=" + lastName

        response = requests.post(baseURL + "/customer" + queryPeram)
        if response.status_code == 201 and wantedStatus == None:
            item = response.json()
            if item == expected:
                print("TEST PASSED - addCustomer")
            else:
                print("TEST FAILED - addCustomer: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            if response.status_code == wantedStatus:
                print("TEST PASSED - addCustomer status code " + str(response.status_code))
            else:
                print("TEST FAILED - addCustomer: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - addCustomer: Error - " + str(e))

def getInventory(expected, firstName=None, lastName=None, wantedStatus=None):
    try:
        queryPeram = ""
        if firstName != None:
            queryPeram = "/?firstName=" + firstName
        if lastName != None:
            if queryPeram == "":
                queryPeram = "/?"
            queryPeram += "&lastName=" + lastName

        response = requests.get(baseURL + "/customer" + queryPeram)
        if response.status_code == 200:
            item = response.json()
            if item == expected:
                print("TEST PASSED - getInventory")
            else:
                print("TEST FAILED - getInventory: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            if response.status_code == wantedStatus:
                print("TEST PASSED - getInventory status code " + str(response.status_code))
            else:
                print("TEST FAILED - getInventory: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - getInventory: Error - " + str(e))

def getCustomerByID(expected, id, wantedStatus=None):
    try:
        response = requests.get(baseURL + "/customer/" + str(id))
        if response.status_code == 200:
            item = response.json()
            if item == expected:
                print("TEST PASSED - getCustomerByID")
            else:
                print("TEST FAILED - getCustomerByID: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            if response.status_code == wantedStatus:
                print("TEST PASSED - getCustomerByID status code " + str(response.status_code))
            else:
                print("TEST FAILED - getCustomerByID: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - getCustomerByID: Error - " + str(e))

def incrementOrderCount(expected, id, wantedStatus=None):
    try:
        response = requests.post(baseURL + "/customer/" + str(id))
        if response.status_code == 200:
            item = response.json()
            if item == expected:
                print("TEST PASSED - incrementOrderCount")
            else:
                print("TEST FAILED - incrementOrderCount: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            if response.status_code == wantedStatus:
                print("TEST PASSED - incrementOrderCount status code " + str(response.status_code))
            else:
                print("TEST FAILED - incrementOrderCount: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - incrementOrderCount: Error - " + str(e))

def deleteCustomer(id, wantedStatus=None):
    try:
        response = requests.delete(baseURL + "/customer/" + str(id))
        if response.status_code == 200:
            response = requests.get(baseURL + "/customer/" + str(id))
            if response.status_code == 404:
                print("TEST PASSED - deleteCustomer")
            else:
                print("TEST FAILED - deleteCustomer: \nExpected - " + str(404) + " \nActual   - " + str(response))
        else:
            if response.status_code == wantedStatus:
                print("TEST PASSED - deleteCustomer status code " + str(response.status_code))
            else:
                print("TEST FAILED - deleteCustomer: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - deleteCustomer: Error - " + str(e))

def changeName(expected, id, firstName=None, lastName=None, wantedStatus=None):
    try:
        queryPeram = ""
        if firstName != None:
            queryPeram = "?firstName=" + firstName
        if lastName != None:
            if queryPeram == "":
                queryPeram = "/?"
            queryPeram += "&lastName=" + lastName

        response = requests.put(baseURL + "/customer/" + str(id) + queryPeram)
        if response.status_code == 200:
            item = response.text
            if item == expected:
                print("TEST PASSED - changeName")
            else:
                print("TEST FAILED - changeName: \nExpected - " + str(expected) + " \nActual   - " + str(item))
        else:
            if response.status_code == wantedStatus:
                print("TEST PASSED - changeName status code " + str(response.status_code))
            else:
                print("TEST FAILED - changeName: Response status code was " + str(response.status_code) + " Message: " + str(response.json()["message"]))
    except requests.ConnectionError as e:
        raise AbortTest("TEST FAILED - changeName: Error - " + str(e))

if __name__ == "__main__":
    #Extencivly test getCustomersByName and /customer get
    try:
        today = datetime.utcnow()
        dateFormated = today.strftime("%Y-%m-%d")
        loadConfig()
        baseURL = "http://" + config['customerAPI.host'] + ":" + config['customerAPI.port']
        try:
            checkDatabaseInitial()
            #Add customers
            customer1 = {"id": 2, "firstName": "John", "lastName": "Doe", "numberOfOrders": 0, "memberSince": dateFormated}
            customer2 = {"id": 3, "firstName": "Jane", "lastName": "Doe", "numberOfOrders": 0, "memberSince": dateFormated}
            customer3 = {"id": 4, "firstName": "Jane", "lastName": "Doerson", "numberOfOrders": 0, "memberSince": dateFormated}
            customer4 = {"id": 5, "firstName": None, "lastName": None, "numberOfOrders": 0, "memberSince": dateFormated}
            customer5 = {"id": 6, "firstName": "Bill", "lastName": None, "numberOfOrders": 0, "memberSince": dateFormated}
            customer6 = {"id": 7, "firstName": None, "lastName": "Doeberman", "numberOfOrders": 0, "memberSince": dateFormated}
            addCustomer(customer1, customer1["firstName"], customer1["lastName"])
            addCustomer(customer2, customer2["firstName"], customer2["lastName"])
            addCustomer(customer3, customer3["firstName"], customer3["lastName"])
            addCustomer(customer4)
            addCustomer(customer5, firstName=customer5["firstName"])
            addCustomer(customer6, lastName=customer6["lastName"])

            #Get cusotmers name
            getInventory([customer1, customer2, customer3, customer4, customer5, customer6])
            getInventory([customer1], firstName=customer1["firstName"])
            getInventory([customer1], firstName="johN")
            getInventory([customer1, customer2], lastName="Doe")
            getInventory([customer2, customer3], firstName="Jane")
            getInventory([customer5], firstName="Bill")
            getInventory([customer1], firstName="John", lastName="Doe")

            #Get customer id
            getCustomerByID(customer4, customer4["id"])
            getCustomerByID(customer3, customer3["id"])
            getCustomerByID({}, 195, wantedStatus=404)

            #Increment
            incrementOrderCount(customer5["numberOfOrders"]+1, customer5["id"])
            customer5["numberOfOrders"] += 1
            incrementOrderCount(customer5["numberOfOrders"]+1, customer5["id"])
            customer5["numberOfOrders"] += 1
            incrementOrderCount(customer5["numberOfOrders"]+1, customer5["id"])
            customer5["numberOfOrders"] += 1
            incrementOrderCount(customer5["numberOfOrders"]+1, customer5["id"])
            incrementOrderCount(None, 195, wantedStatus=404)

            #Change name
            changeName("Bill", customer5["id"])
            changeName("Last Name: Doeberman", customer6["id"])
            customer4["firstName"] = "John"
            changeName("John", customer4["id"], firstName=customer4["firstName"])
            customer4["lastName"] = "Johnson"
            changeName("John Johnson", customer4["id"], lastName=customer4["lastName"])
            customer6["firstName"] = "Jane"
            customer6["lastName"] = "Johnson"
            changeName("Jane Johnson", customer6["id"], firstName=customer6["firstName"], lastName=customer6["lastName"])
            changeName({}, 195, wantedStatus=404)

            getInventory([customer4, customer6], lastName="Johnson")

            #Delete
            deleteCustomer(2)
            deleteCustomer(3)
            deleteCustomer(4)
            deleteCustomer(5)
            deleteCustomer(6)
            deleteCustomer(7)
            deleteCustomer(195, wantedStatus=404)

            #final test
            getInventory([])

        except AbortTest as err:
            print(err)
    except (KeyboardInterrupt, SystemExit) as e:
        raise e